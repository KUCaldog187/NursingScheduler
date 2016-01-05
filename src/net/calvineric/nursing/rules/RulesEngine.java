package net.calvineric.nursing.rules;

import java.util.Calendar;

import net.calvineric.nursing.NursingCalender;
import net.calvineric.nursing.constants.WorkCodeConstants;

public class RulesEngine implements WorkCodeConstants {

	public static void applyRules(int month, NursingCalender monthSchedule) {
		
//		everyDayLogic(month, monthSchedule);
//		everySatSunLogic(month, monthSchedule);
		random2DaysPerWeek(month, monthSchedule);
		
	}
	
	private static void everyDayLogic(int month, NursingCalender monthSchedule){
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2015);
		calendar.set(Calendar.MONTH, month);
		int numDays = calendar.getActualMaximum(Calendar.DATE);
		
		int daysToGenerate = numDays;
		
		for(int i=0;i<daysToGenerate;i++){
			monthSchedule.getDailySchedule().get(i).setValue(DAY_OFF);
		}
	}
	
	private static void everySatSunLogic(int month, NursingCalender monthSchedule){
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2015);
		calendar.set(Calendar.MONTH, month);
		int numDays = calendar.getActualMaximum(Calendar.DATE);
		
		int daysToGenerate = numDays;
		
		for(int i=1;i<=daysToGenerate;i++){
			calendar.set(Calendar.DATE, i);
			if(calendar.get(Calendar.DAY_OF_WEEK)== 1 || calendar.get(Calendar.DAY_OF_WEEK)== 7){
				monthSchedule.getDailySchedule().get(i).setValue(DAY_OFF);
			}else{
				monthSchedule.getDailySchedule().get(i).setValue(WORKING);
			}
		}
	}
	
	private static void random2DaysPerWeek(int month, NursingCalender monthSchedule){
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2015);
		calendar.set(Calendar.MONTH, month);
		int numDays = calendar.getActualMaximum(Calendar.DATE);
		
		int daysToGenerate = numDays;
		
		for(int i=1;i<=daysToGenerate;i++){
			calendar.set(Calendar.DATE, i);
			if(calendar.get(Calendar.DAY_OF_WEEK)== 1 && i+6 <= daysToGenerate){  // Only handle weeks with full 7 days
				int daysPicked = 0;
				// Pre scan week to count how many days are already locked
				for(int z=i; z<=i+6;z++){
					if(monthSchedule.getDailySchedule().get(z).isLocked() && monthSchedule.getDailySchedule().get(z).getValue().equals(DAY_OFF)){
						daysPicked++;
					}
				}
				// Randomly generate up to 2 days off in the week
				do{
					for(int z=i; z<=i+6;z++){
						if(!monthSchedule.getDailySchedule().get(z).isLocked()){
							if(daysPicked < 2 ){
								if(Math.random() > 0.5){
									monthSchedule.getDailySchedule().get(z).setValue(DAY_OFF);
									daysPicked++;
								}else{
									monthSchedule.getDailySchedule().get(z).setValue(WORKING);
								}
							}else{
								monthSchedule.getDailySchedule().get(z).setValue(WORKING);
							}
						}
					}
				}while(daysPicked < 2); //Although unlikely, may be possible to redo week generation if minimum days off not generated
				i += 6; // Increment to the next week
			}else if(!monthSchedule.getDailySchedule().get(i).isLocked()){
				monthSchedule.getDailySchedule().get(i).setValue(WORKING);
			}
		}
	}
	
	public static void main(String[] args){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2016);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DATE, 31);
		int numDays = calendar.getActualMaximum(Calendar.DATE);
		
		int dayofmonth = calendar.get(Calendar.DAY_OF_MONTH);
		int weekofmonth = calendar.get(Calendar.WEEK_OF_MONTH);
		int dayofweek = calendar.get(Calendar.DAY_OF_WEEK);

		System.out.println(dayofmonth);
		System.out.println(weekofmonth);
		System.out.println(dayofweek);

	}

}
