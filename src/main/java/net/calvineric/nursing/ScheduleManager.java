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
import java.util.StringTokenizer;
import net.calvineric.nursing.rules.RulesEngine;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ScheduleManager {
	
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
					YearlySchedule yearlySchedule = employee.getYearlySchedule(Integer.parseInt(year));
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
		YearlySchedule yearlySchedule = employee.getYearlySchedule(year);
		if(yearlySchedule == null){
			yearlySchedule = new YearlySchedule(year);
			employee.addYearlySchedule(yearlySchedule, year);
		}
		MonthlySchedule monthSchedule = yearlySchedule.getScheduleForMonth(month);
		RulesEngine.applyRules(yearlySchedule, monthSchedule);
		success = saveEmployeeToFile(employee, year);
		return success;
		
	}
	
	public static boolean saveEmployeeToFile(Employee employee, int year) throws IOException{
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
			String output = employee.getYearlySchedule(year).toString();
		    writer.write(output, 0, output.length());
		} catch (IOException x) {
		    x.printStackTrace();
		}
		return success;
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
	
	

}
