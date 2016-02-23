package net.calvineric.nursing.rules;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.calvineric.nursing.DailySchedule;
import net.calvineric.nursing.MonthlySchedule;
import net.calvineric.nursing.ScheduleManager;
import net.calvineric.nursing.YearlySchedule;
import net.calvineric.nursing.constants.WorkCodeConstants;

public class RulesEngine implements WorkCodeConstants {

	public static void applyRules(YearlySchedule yearlySchedule, MonthlySchedule monthSchedule) {
		
//		everyDayLogic(monthSchedule);
//		everySatSunLogic(monthSchedule);
		random2DaysPerWeek(yearlySchedule, monthSchedule);
		
	}
	
	private static void everyDayLogic(MonthlySchedule monthSchedule){
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, monthSchedule.getYearValue());
		calendar.set(Calendar.MONTH, monthSchedule.getMonthValue());
		calendar.set(Calendar.DATE, 1);
		int numDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		int daysToGenerate = numDays;
		
		for(int i=0;i<daysToGenerate;i++){
			monthSchedule.getDailySchedule().get(i).setValue(DAY_OFF);
		}
	}
	
	private static void everySatSunLogic(MonthlySchedule monthSchedule){
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, monthSchedule.getYearValue());
		calendar.set(Calendar.MONTH, monthSchedule.getMonthValue());
		calendar.set(Calendar.DATE, 1);
		int numDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
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
	
	@Deprecated
	private static void random2DaysPerWeek(MonthlySchedule monthSchedule){
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, monthSchedule.getYearValue());
		calendar.set(Calendar.MONTH, monthSchedule.getMonthValue());
		calendar.set(Calendar.DATE, 1);
		int numDays = calendar.getActualMaximum(Calendar.DATE);
		
		int daysToGenerate = numDays;
		
		for(int i=1;i<=daysToGenerate;i++){
			calendar.set(Calendar.DATE, i);
			if(calendar.get(Calendar.DAY_OF_WEEK)== 1 && i+6 <= daysToGenerate){  // Only handle weeks with full 7 days
				int daysPicked = 0;
				// Randomly generate up to 2 days off in the week
				do{
					daysPicked = 0;
					// Pre scan week to count how many days are already locked
					for(int z=i; z<=i+6;z++){
						if(monthSchedule.getDailySchedule().get(z).isLocked() && monthSchedule.getDailySchedule().get(z).getValue().equals(DAY_OFF)){
							daysPicked++;
						}
					}

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
			}else{
//				if(!monthSchedule.getDailySchedule().get(i).isLocked()){
//			}
//				monthSchedule.getDailySchedule().get(i).setValue(WORKING);
			}
		}
	}
	
	@Deprecated
	private static void random2DaysPerWeek(MonthlySchedule monthSchedule, int dayStart){
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, monthSchedule.getYearValue());
		calendar.set(Calendar.MONTH, monthSchedule.getMonthValue());
		calendar.set(Calendar.DATE, 1);
		int numDays = calendar.getActualMaximum(Calendar.DATE);
		
		int daysToGenerate = numDays;
		
		for(int i=dayStart;i<dayStart+7;i++){ // generate for constant 7 days
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
			}else if(!monthSchedule.getDailySchedule().get(i).isLocked()&& !monthSchedule.getDailySchedule().get(i).getValue().equals(DAY_OFF)){
				monthSchedule.getDailySchedule().get(i).setValue(WORKING);
			}
		}
	}
	
	private static void random2DaysPerWeek(YearlySchedule yearlySchedule, DailySchedule dailySchedule){
		// GET ALL WEEKS FOR MONTH INCLUDING BOUNDARY WEEKS
		// DONT KNOW WHICH DAYS START WHICH WEEKS, MUST ITERATE ENTIRE MONTH AND GET ALL WEEKS
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, dailySchedule.getYearValue());
		calendar.set(Calendar.MONTH, dailySchedule.getMonthValue());
		calendar.set(Calendar.DATE, dailySchedule.getDayValue());
		
		int weekofyear = calendar.get(Calendar.WEEK_OF_YEAR);

		Set<DailySchedule> week = yearlySchedule.getWeeks().get(weekofyear);
		int daysPicked = 0;
		int daysPickedPreScan = 0;
		
		// PRESCAN FOR DAYS PICKED
		for (DailySchedule day : week) { 
			if(day.isLocked() && day.getValue().equals(DAY_OFF)){
				daysPicked = ++daysPickedPreScan;
			}
		}

		// Randomly generate up to 2 days off in the week
		do{
			daysPicked = daysPickedPreScan; // Reset daysPicked TODO fix this maybe, must pick 2 days in 1 pass or next pass will overwrite

			for (DailySchedule day : week) {
				if(!day.isLocked()){
					if(daysPicked < 2 ){
						if(Math.random() > 0.5){
							day.setValue(DAY_OFF);
							daysPicked++;
						}else{
							day.setValue(WORKING);
						}
					}else{
						day.setValue(WORKING);
					}
				}
			}
		}while(daysPicked < 2); //Although unlikely, may be possible to redo week generation if minimum days off not generated
	}
	
	private static void random2DaysPerWeek(YearlySchedule yearlySchedule, MonthlySchedule monthSchedule){
		// GET ALL WEEKS FOR MONTH INCLUDING BOUNDARY WEEKS
		// DONT KNOW WHICH DAYS START WHICH WEEKS, MUST ITERATE ENTIRE MONTH AND GET ALL WEEKS
		
		Set<Integer> weekSet = new HashSet<Integer>();
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, monthSchedule.getYearValue());
		calendar.set(Calendar.MONTH, monthSchedule.getMonthValue());
		calendar.set(Calendar.DATE, 1);
		int numDays = calendar.getActualMaximum(Calendar.DATE);
		
		for(int i=1;i<=numDays;i++){
			calendar.set(Calendar.DATE, i);
			weekSet.add(calendar.get(Calendar.WEEK_OF_YEAR));
		}
		

		for (Integer weekofyear : weekSet) {
			Set<DailySchedule> week = yearlySchedule.getWeeks().get(weekofyear);
			int daysPicked = 0;
			int daysPickedPreScan = 0;
			// PRESCAN FOR DAYS PICKED
			for (DailySchedule dailySchedule : week) { 
				if(dailySchedule.isLocked() && dailySchedule.getValue().equals(DAY_OFF)){
					daysPicked = ++daysPickedPreScan;
				}
			}
			
			// Randomly generate up to 2 days off in the week
			do{
				daysPicked = daysPickedPreScan; // Reset daysPicked TODO fix this maybe, must pick 2 days in 1 pass or next pass will overwrite
				for (DailySchedule dailySchedule : week) {
					if(!dailySchedule.isLocked()){
						if(daysPicked < 2 ){
							if(Math.random() > 0.5){
								dailySchedule.setValue(DAY_OFF);
								daysPicked++;
							}else{
								dailySchedule.setValue(WORKING);
							}
						}else{
							dailySchedule.setValue(WORKING);
						}
					}
				}
			}while(daysPicked < 2); //Although unlikely, may be possible to redo week generation if minimum days off not generated
		}
	}
	
	public static boolean obeysDailyRules(DailySchedule dailySchedule){
		boolean obeys = true;
		if(dailySchedule.isLocked() || dailySchedule.getValue().equals(DAY_OFF)){
			obeys = false;
		}
		return obeys;
	}
	
	private static int calculateWeeklyDaysOff(YearlySchedule yearlySchedule , DailySchedule dailySchedule){
		
		Set<Integer> weekSet = new HashSet<Integer>();
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, dailySchedule.getYearValue());
		calendar.set(Calendar.MONTH, dailySchedule.getMonthValue());
		calendar.set(Calendar.DATE, dailySchedule.getDayValue());
		
		weekSet.add(calendar.get(Calendar.WEEK_OF_YEAR));
		
		int daysAlreadyOffForWeek = 0;

		Set<DailySchedule> week = yearlySchedule.getWeeks().get(calendar.get(Calendar.WEEK_OF_YEAR));
		
		// PRESCAN FOR DAYS PICKED
		for (DailySchedule day : week) { 
			if(day.getValue().equals(DAY_OFF)){
				daysAlreadyOffForWeek++;
			}
		}
	
		return daysAlreadyOffForWeek;
		
	}
	
	public static boolean obeysWeeklyRules(YearlySchedule yearlySchedule, DailySchedule dailySchedule){
		boolean obeys = false;
		
		int daysAlreadyOffForWeek = calculateWeeklyDaysOff(yearlySchedule, dailySchedule);
		
		if(daysAlreadyOffForWeek == 2 ){ // SHOULD ONLY HAVE 2 DAYS OFF FOR WEEK
			obeys = true;
		}else if(daysAlreadyOffForWeek > 2){ // THIS WILL CLEAR EXTRA GENERATED DAYS
			random2DaysPerWeek(yearlySchedule, dailySchedule); 
			obeys = true;
		}else{
			do{
				random2DaysPerWeek(yearlySchedule, dailySchedule);
			}while(calculateWeeklyDaysOff(yearlySchedule, dailySchedule) != 2);
			obeys = true;
		}

		return obeys;
	}
	
	private static int calculateWeekendsOff(YearlySchedule yearlySchedule, int quater){

		
		Calendar calendar = Calendar.getInstance();
		List<Integer> weekList = ScheduleManager.buildWeekList(yearlySchedule, quater);
		java.util.Collections.shuffle(weekList);
		
		int weekendsOff = 0;
		
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
									if(dailySchedule.getValue().equals(DAY_OFF) && dailySchedule2.getValue().equals(DAY_OFF)){
										dailySchedule.setLocked(true);
										dailySchedule2.setLocked(true);
										weekendsOff++;
										break;
									}
								}
							}
						}
					}
				}
			}
		}
		return weekendsOff;
	}
	
	
	// TODO NOT THREAD SAFE!!!
	public static List<Integer> buildEveryOtherWeekendList(List<Integer> weekendList){
		List<Integer> everyOtherList = new ArrayList<Integer>();
		java.util.Collections.sort(weekendList); 
		for(int i=0; i<weekendList.size();i=+2){
			everyOtherList.add(weekendList.get(i));
		}
		return everyOtherList;
	}
	
	
	public static boolean obeysQuaterlyRules(YearlySchedule yearlySchedule, int quater){
		boolean obeys = false;
		
		Calendar calendar = Calendar.getInstance();

		int weekendsOffNeeded = 5;
		int weekendsOff = 0;
		
		weekendsOff = calculateWeekendsOff(yearlySchedule, quater);
		
		List<Integer> sortedList = ScheduleManager.buildWeekList(yearlySchedule, quater);
		
		if(weekendsOff >= weekendsOffNeeded){
			obeys = true;
		}else{
			// GENERATE DAYS OFF TO MEET WEEKEND REQUIREMENTS AND LOCK IN
			// TAKE FIRST AVAIALABLE WEEKENDS 
			//TODO RANDOMLY SELECT
			for (Integer weekofyear : sortedList) {
				Set<DailySchedule> week = yearlySchedule.getWeeks().get(weekofyear);
				for (DailySchedule dailySchedule : week) {
					if(dailySchedule.getYearValue() == yearlySchedule.getYearValue()){
						calendar.set(Calendar.YEAR, dailySchedule.getYearValue());
						calendar.set(Calendar.MONTH, dailySchedule.getMonthValue());
						calendar.set(Calendar.DATE, dailySchedule.getDayValue());
						if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
							Set<DailySchedule> week2 = yearlySchedule.getWeeks().get(weekofyear+1); // GET THE NEXT WEEK
							for (DailySchedule dailySchedule2 : week2) {
								if(dailySchedule2.getDayValue() == dailySchedule.nextDay()){
									if(!dailySchedule.getValue().equals(DAY_OFF) || !dailySchedule2.getValue().equals(DAY_OFF)){ 
										if(!dailySchedule.isLocked() && !dailySchedule2.isLocked()){
											dailySchedule.setValue(DAY_OFF);
											dailySchedule2.setValue(DAY_OFF);
											dailySchedule.setLocked(true);
											dailySchedule2.setLocked(true);
										}
										if(calculateWeekendsOff(yearlySchedule, quater) >= weekendsOffNeeded){
											return true;
										}
									}
									break;
								}
							}
						}
					}
				}
			}
		}
		return obeys;
	}
	
	public static void main(String[] args){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2015);
		calendar.set(Calendar.MONTH, 11);
		calendar.set(Calendar.DATE, 31);
		
		int dayofmonth = calendar.get(Calendar.DAY_OF_MONTH);
		int weekofmonth = calendar.get(Calendar.WEEK_OF_MONTH);
		int dayofweek = calendar.get(Calendar.DAY_OF_WEEK);
		int weeksinmonth = calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
		int weekofyear = calendar.getActualMaximum(Calendar.WEEK_OF_YEAR);

		System.out.println(dayofmonth);
		System.out.println(weekofmonth);
		System.out.println(dayofweek);
		System.out.println(weeksinmonth);
		System.out.println(weekofyear);
		

	}

	public static boolean isEligibleToWork(YearlySchedule yearlySchedule, int quater,  MonthlySchedule monthlySchedule, DailySchedule dailySchedule) {
		boolean eligible = false;
		
		if(obeysQuaterlyRules(yearlySchedule, quater)){  // CHECK IF MINIMUM WEEKENDS IN QUATER ARE MET
			if(obeysWeeklyRules(yearlySchedule, dailySchedule)){  // CHECK IF MINIMUM DAYS IN WEEK ARE MET
				eligible = obeysDailyRules(dailySchedule); // CHECK IF NOT ALREADY OFF OR LOCKED FOR OTHER REASON
			}
		}
		
		return eligible;
	}

}
