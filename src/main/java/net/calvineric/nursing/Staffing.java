package net.calvineric.nursing;

import java.io.IOException;
import java.util.List;
import java.util.Calendar;

import net.calvineric.nursing.constants.WorkCodeConstants;
import net.calvineric.nursing.rules.RulesEngine;

public class Staffing {

	public static void staffingLogicSON(List<Employee> employeeListSON, int month, int year) throws IOException{
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DATE, 1);
		int daysToGenerate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		boolean nursesStillNeeded = true;
		do{
			for(int i=1;i<=daysToGenerate;i++){
				for(Employee employee : employeeListSON){
					DailySchedule dailySchedule = employee.getYearlySchedule(year).getScheduleForMonth(month).getDailySchedule().get(i);
					if(!dailySchedule.isLocked()){
						dailySchedule.setValue(WorkCodeConstants.WORKING);
						ScheduleManager.saveEmployeeToFile(employee,year);
					}
				}
			}
			nursesStillNeeded = false;
		}while(nursesStillNeeded);
	}
	
	public static void staffingLogicRN(List<Employee> employeeList, int month, int year) throws IOException{
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DATE, 1);
		int daysToGenerate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		boolean nursesStillNeeded = true;
		do{
			for(int i=1;i<=daysToGenerate;i++){
				for(Employee employee : employeeList){
					DailySchedule dailySchedule = employee.getYearlySchedule(year).getScheduleForMonth(month).getDailySchedule().get(i);
					if(!dailySchedule.isLocked()){
						dailySchedule.setValue(WorkCodeConstants.WORKING);
						ScheduleManager.saveEmployeeToFile(employee, year);
					}
				}
			}
			nursesStillNeeded = false;
		}while(nursesStillNeeded);
	}
	
	public static void staffingLogicLPN(List<Employee> employeeList, int month, int year) throws IOException{
		
		int quater = 0;
		
		if(month >=0 && month <=2){
			quater = 1;
		}else if(month >=3 && month <=5){
			quater = 2;
		}else if(month >=6 && month <=8){
			quater = 3;
		}else if(month >=9 && month <=11){
			quater = 4;
		}else{
			quater = 1;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DATE, 1);
		int daysToGenerate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		boolean nursesStillNeeded = true;
		do{
			for(int i=1;i<=daysToGenerate;i++){
				for(Employee employee : employeeList){
					YearlySchedule yearlySchedule = employee.getYearlySchedule(year);
					MonthlySchedule monthlySchedule = yearlySchedule.getScheduleForMonth(month);
					DailySchedule dailySchedule = monthlySchedule.getDailySchedule().get(i);
					if(RulesEngine.isEligibleToWork(yearlySchedule, quater, monthlySchedule, dailySchedule)){
						dailySchedule.setValue("T");
						ScheduleManager.saveEmployeeToFile(employee, year);
					}
				}
			}
			nursesStillNeeded = false;
		}while(nursesStillNeeded);
	}
	
	public static void staffingLogicCNA(List<Employee> employeeList, int month, int year) throws IOException{
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DATE, 1);
		int daysToGenerate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		boolean nursesStillNeeded = true;
		do{
			for(int i=1;i<=daysToGenerate;i++){
				for(Employee employee : employeeList){
					DailySchedule dailySchedule = employee.getYearlySchedule(year).getScheduleForMonth(month).getDailySchedule().get(i);
					if(!dailySchedule.isLocked()){
						dailySchedule.setValue(WorkCodeConstants.WORKING);
						ScheduleManager.saveEmployeeToFile(employee, year);
					}
				}
			}
			nursesStillNeeded = false;
		}while(nursesStillNeeded);
	}
}
