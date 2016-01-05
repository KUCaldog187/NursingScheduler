package net.calvineric.nursing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import net.calvineric.nursing.constants.WorkCodeConstants;
import net.calvineric.nursing.rules.RulesEngine;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

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
				String month = (String) st.nextElement();

		        while (st.hasMoreElements()) {
		        	String s = (String) st.nextElement();
					String[] lineArray = s.split("=");
					DailySchedule schedule = employee.getYearlySchedule().getScheduleForMonth(Integer.parseInt(month)).getDailySchedule().get(Integer.parseInt(lineArray[0]));
					if(schedule == null){
						schedule = new DailySchedule();
						employee.getYearlySchedule().getScheduleForMonth(Integer.parseInt(month)).getDailySchedule().put(Integer.parseInt(lineArray[0]), schedule);						
					}
					schedule.setValue(lineArray[1].split(":")[0]);
					if(lineArray[1].split(":").length == 2){
						schedule.setLocked(true);
					}
				}		        
		    }
		} catch (IOException x) {
			success = false;
		    System.err.format("IOException: %s%n", x);
		}
		
		return success;
	}
	
	public static boolean generateScheduleForEmployee(Employee employee, int month) throws IOException{
		boolean success = false;
		NursingCalender monthSchedule = employee.getYearlySchedule().getScheduleForMonth(month);
		RulesEngine.applyRules(month, monthSchedule);
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
			String output = employee.getYearlySchedule().toString();
		    writer.write(output, 0, output.length());
		} catch (IOException x) {
		    x.printStackTrace();
		}
		return success;
	}
	
	public static void main(String[] args) throws IOException, ParseException{
		
		ApplicationContext context = new ClassPathXmlApplicationContext("net/calvineric/nursing/bean/Nurses.xml");
	  
	  	NursingCollection nurses = (NursingCollection) context.getBean("nursingCollection");
	  	
	  	List<Employee> employeeList = new ArrayList<Employee>();
	  	employeeList.add(nurses.getNurseMap().get(1));
	  	employeeList.add(nurses.getNurseMap().get(2));
	  	
	  	int month = 0;
	  	int day = 4;
	  	calculateDailyCount(employeeList, month, day);
	}
	
	public static void calculateDailyCount(List<Employee> employeeList, int month, int day) throws IOException{
		int minimumRequired = 2;
		int employeesWorking = 0;
		do{
			employeesWorking = 0;
			
			for(Employee employee: employeeList){
				DailySchedule dailySchedule = employee.getYearlySchedule().getMonthlySchedule().get(month).getDailySchedule().get(day);
				if(dailySchedule.getValue().equals(WorkCodeConstants.WORKING)){
					employeesWorking++;
				}
			}
			if(employeesWorking < minimumRequired){
				for(Employee employee: employeeList){
					generateScheduleForEmployee(employee, month);
				}
			}
		}while(employeesWorking < minimumRequired);
	}

	public String getPersistMethod() {
		return persistMethod;
	}

	public void setPersistMethod(String persistMethod) {
		this.persistMethod = persistMethod;
	}
	
	

}
