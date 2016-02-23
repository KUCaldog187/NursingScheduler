package net.calvineric.nursing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import net.calvineric.nursing.constants.WorkCodeConstants;
import net.calvineric.nursing.rules.RulesEngine;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ScheduleManager {
	
	private static final String EVERY_OTHER_WEEKEND = "EOW";
	private String persistMethod = "FILE";
	
	public static boolean loadEmployeeScheduleFromFile(Employee employee){
		boolean success = true;
		
		Path basePath = Paths.get("C:\\schedules");
		Path file = Paths.get(employee.getId()+".schedule");
		Path fullFile = basePath.resolve(file);
		
		Charset charset = Charset.forName("US-ASCII");
		try (BufferedReader reader = Files.newBufferedReader(fullFile, charset)) {
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		    	
				StringTokenizer st = new StringTokenizer(line , "{, }", false);
				String year = (String) st.nextElement();
				String month = (String) st.nextElement();

		        while (st.hasMoreElements()) {
		        	String s = (String) st.nextElement();
					String[] lineArray = s.split("=");
					Schedule schedule = employee.getSchedule();
					YearlySchedule yearlySchedule = schedule.getYearlySchedule(Integer.parseInt(year));
					MonthlySchedule monthlySchedule = yearlySchedule.getScheduleForMonth(Integer.parseInt(month)); 
					DailySchedule dailySchedule = monthlySchedule.getDailySchedule().get(Integer.parseInt(lineArray[0]));
					dailySchedule.setValue(lineArray[1].split(":")[0]);
					if(lineArray[1].split(":").length == 2){
						dailySchedule.setLocked(true);
					}
				}		        
		    }
		} catch (IOException x) {
			success = false;
		    System.err.format("IOException: %s%n", x);
		}
		
		return success;
	}
	
	public static boolean generateScheduleForEmployee(Employee employee, int year, int month) throws IOException{
		boolean success = false;
		Schedule schedule = employee.getSchedule();
		YearlySchedule yearlySchedule = schedule.getYearlySchedule(year);
		if(yearlySchedule == null){
			yearlySchedule = new YearlySchedule(year);
			schedule.addYearlySchedule(yearlySchedule);
		}
		MonthlySchedule monthSchedule = yearlySchedule.getScheduleForMonth(month);
		RulesEngine.applyRules(yearlySchedule, monthSchedule);
		success = saveEmployeeToFile(employee);
		return success;
		
	}
	
	public static boolean saveEmployeeToFile(Employee employee) throws IOException{
		boolean success = false;
		
		Charset charset = Charset.forName("US-ASCII");

		Path basePath = Paths.get("C:\\schedules");
		if(Files.exists(basePath) == false && Files.notExists(basePath) == false){
			// SOMETHING WRONG!!!
			// DONT SET BASEPATH
			basePath = Paths.get("");
		}else if(Files.notExists(basePath)){
			Files.createDirectory(basePath);
		}
		Path file = Paths.get(employee.getId()+".schedule");
		Path fullFile = basePath.resolve(file);
		
		try (BufferedWriter writer = Files.newBufferedWriter(fullFile, charset, StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING )) {
			Schedule schedule = employee.getSchedule();
			String output = schedule.toString();
		    writer.write(output, 0, output.length());
		} catch (IOException x) {
		    x.printStackTrace();
		}
		return success;
	}
	
	public static void loadDefaultDaysOff(Employee employee) throws IOException {
		Calendar calendar = Calendar.getInstance();
		if(employee.getSpecialCondition().equals(EVERY_OTHER_WEEKEND)){
			for(YearlySchedule yearlySchedule : employee.getSchedule().getYearlySchedule().values()){
				List<DailySchedule> weekendList = new ArrayList<DailySchedule>();
				List<Integer> weekList = buildWeekList(yearlySchedule);
				Collections.sort(weekList);
				
				for (Integer weekofyear : weekList) {
					Set<DailySchedule> week = yearlySchedule.getWeeks().get(weekofyear);
					for (DailySchedule dailySchedule : week) {
						if(dailySchedule.getYearValue() == yearlySchedule.getYearValue()){
							calendar.set(Calendar.YEAR, dailySchedule.getYearValue());
							calendar.set(Calendar.MONTH, dailySchedule.getMonthValue());
							calendar.set(Calendar.DATE, dailySchedule.getDayValue());
							if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
								int weeksinyear = calendar.getActualMaximum(Calendar.WEEK_OF_YEAR);
								int nextWeek = weekofyear+1 > weeksinyear ? 1:weekofyear+1; // HANDLE WEEK 53 LOGIC
								if(nextWeek == 1){
									// TODO HANDLE NEXT YEAR 
								}else{
									Set<DailySchedule> week2 = yearlySchedule.getWeeks().get(nextWeek);
									int nextDay = dailySchedule.nextDay(); // HANDLE LAST DAY OF MONTH INTO FIRST DAY OF NEXT MONTH
									for (DailySchedule dailySchedule2 : week2) {
										if(dailySchedule2.getDayValue() == nextDay){ 
											if(dailySchedule.getValue().equals(WorkCodeConstants.NEUTRAL) && dailySchedule2.getValue().equals(WorkCodeConstants.NEUTRAL)){
												weekendList.add(dailySchedule);
												weekendList.add(dailySchedule2);
												break;
											}
										}
									}
								}
							}
						}
					}
				}
				
				for(int i=0; i<weekendList.size();i+=4){ //Every other weekend must increment +4 
					if(i+1 < weekendList.size()){
						DailySchedule saturday = weekendList.get(i);
						DailySchedule sunday = weekendList.get(i+1);
						if(canSetDefaultWeekendPair(saturday, sunday)){
							saturday.setValue(WorkCodeConstants.DAY_OFF);
							sunday.setValue(WorkCodeConstants.DAY_OFF);
							saturday.setLocked(true);
							sunday.setLocked(true);
						}
					}
				}
				saveEmployeeToFile(employee);
			}
		}else{
			for(YearlySchedule yearlySchedule : employee.getSchedule().getYearlySchedule().values()){
				List<Integer> weekList = buildWeekList(yearlySchedule);
				Collections.sort(weekList);
				
				for (Integer weekofyear : weekList) {
					Set<DailySchedule> week = yearlySchedule.getWeeks().get(weekofyear);
					for (DailySchedule dailySchedule : week) {
						if(dailySchedule.getYearValue() == yearlySchedule.getYearValue()){
							calendar.set(Calendar.YEAR, dailySchedule.getYearValue());
							calendar.set(Calendar.MONTH, dailySchedule.getMonthValue());
							calendar.set(Calendar.DATE, dailySchedule.getDayValue());
							if(employee.getDefaultDaysOff().contains(calendar.get(Calendar.DAY_OF_WEEK))){
								if(!dailySchedule.isLocked() && dailySchedule.getValue().equals(WorkCodeConstants.NEUTRAL)){
									dailySchedule.setValue(WorkCodeConstants.DAY_OFF);
									dailySchedule.setLocked(true);
								}
							}
						}
					}
				}
			}
			saveEmployeeToFile(employee);
		}
	}
	
	private static boolean canSetDefaultWeekendPair(DailySchedule saturday, DailySchedule sunday){
		boolean canSet = false;
		if(!saturday.isLocked() && !sunday.isLocked()){
			if(saturday.getValue().equals(WorkCodeConstants.NEUTRAL) && sunday.getValue().equals(WorkCodeConstants.NEUTRAL)){
				canSet = true;
			}
		}

		return canSet;
	}
	
	public static void main(String[] args) throws IOException, ParseException{
		
		ApplicationContext context = new ClassPathXmlApplicationContext("net/calvineric/nursing/bean/Nurses.xml");
	  
	  	NursingCollection nurses = (NursingCollection) context.getBean("nursingCollection");
	}

	public String getPersistMethod() {
		return persistMethod;
	}

	public void setPersistMethod(String persistMethod) {
		this.persistMethod = persistMethod;
	}	
	
	public static List<Integer> buildWeekList(YearlySchedule yearlySchedule, int quater){
		
		int startingMonth = 0;
		int endingMonth = 0;
		
		switch (quater) {
		case 1:
			startingMonth = 0;
			endingMonth = 2;
			break;
		case 2:
			startingMonth = 3;
			endingMonth = 5;
			break;
		case 3:
			startingMonth = 6;
			endingMonth = 8;
			break;
		case 4:
			startingMonth = 9;
			endingMonth = 11;
			break;
		default:
			startingMonth = 0;
			endingMonth = 2;
			break;
		}
		
		Set<Integer> weekSet = new HashSet<Integer>();
		Calendar calendar = Calendar.getInstance();
		// CHECK WEEKEND PAIRS TO SEE IF BOTH SAT AND SUN ARE OFF. 
		for(int i=startingMonth; i<=endingMonth; i++){			
			calendar.set(Calendar.YEAR, yearlySchedule.getYearValue());
			calendar.set(Calendar.MONTH, i);
			calendar.set(Calendar.DATE, 1);
			int numDays = calendar.getActualMaximum(Calendar.DATE);
			
			for(int x=1;x<=numDays;x++){
				calendar.set(Calendar.DATE, x);
				weekSet.add(calendar.get(Calendar.WEEK_OF_YEAR));
			}
		}
		
		List<Integer> list = new ArrayList<Integer>();
		list.addAll(weekSet);
		
		
		return list;
	}
	
	public static List<Integer> buildWeekList(YearlySchedule yearlySchedule){
		
		int startingMonth = 0;
		int endingMonth = 11;
		
		Set<Integer> weekSet = new HashSet<Integer>();
		Calendar calendar = Calendar.getInstance();
 
		for(int i=startingMonth; i<=endingMonth; i++){			
			calendar.set(Calendar.YEAR, yearlySchedule.getYearValue());
			calendar.set(Calendar.MONTH, i);
			calendar.set(Calendar.DATE, 1);
			int numDays = calendar.getActualMaximum(Calendar.DATE);
			
			for(int x=1;x<=numDays;x++){
				calendar.set(Calendar.DATE, x);
				weekSet.add(calendar.get(Calendar.WEEK_OF_YEAR));
			}
		}
		
		List<Integer> list = new ArrayList<Integer>();
		list.addAll(weekSet);
		
		
		return list;
	}

}
