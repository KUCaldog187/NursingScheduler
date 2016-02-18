package net.calvineric.nursing;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Schedule {
	
	private Map<Integer, YearlySchedule> yearlySchedule = new HashMap<Integer, YearlySchedule>();
	
	public YearlySchedule getYearlySchedule(int year) {
		if(yearlySchedule.get(year) == null){
			addYearlySchedule(new YearlySchedule(year));
		}
		return yearlySchedule.get(year);
	}

	public void addYearlySchedule(YearlySchedule yearlySchedule) {
		this.yearlySchedule.put(yearlySchedule.getYearValue(), yearlySchedule);
		populateWeeks(yearlySchedule);
	}

	public Map<Integer, YearlySchedule> getYearlySchedule() {
		return yearlySchedule;
	}

	public void setYearlySchedule(Map<Integer, YearlySchedule> yearlySchedule) {
		this.yearlySchedule = yearlySchedule;
	}
	
	private void populateWeeks(YearlySchedule yearlySchedule) {
		Map<Integer, Set<DailySchedule>> weeksMap = yearlySchedule.getWeeks();
		
		for (Iterator<MonthlySchedule> iterator = yearlySchedule.getMonthlySchedule().values().iterator(); iterator.hasNext();) {
			MonthlySchedule monthSchedule = (MonthlySchedule) iterator.next();

			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, yearlySchedule.getYearValue());
			calendar.set(Calendar.MONTH, monthSchedule.getMonthValue());
			calendar.set(Calendar.DATE, 1);
			int numDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			
			for(int day=1;day<=numDays;day++){  // ITERATE THROUGH THE MONTH
				calendar.set(Calendar.DATE, day);
				int dayofweek = calendar.get(Calendar.DAY_OF_WEEK);
				int weekofyear = calendar.get(Calendar.WEEK_OF_YEAR);

				Set<DailySchedule> week = weeksMap.get(weekofyear);
				if(week == null){
					week = new HashSet<DailySchedule>();
					weeksMap.put(weekofyear, week);
				}
				
				if(dayofweek == 1 && day+6 <= numDays){ // FULL 7 DAYS
					for(int z=day; z<=day+6;z++){
						week.add(monthSchedule.getDailySchedule().get(z));
					}
					day += 6; // Increment to the next week
				}else if (dayofweek == 1 && day+6 > numDays){ // WEEK EXTENDS INTO NEXT MONTH
					int daysInNextMonth = (day+6) - numDays;
					
					// GET DAYS IN NEXT MONTH
					if(monthSchedule.getMonthValue()+1 < 12){ // IF EXCEEDS CURRENT CALENDAR HANDLE IN ESLE BLOCK 
						MonthlySchedule nextMonthScheudle = getScheduleForMonth(monthSchedule.getMonthValue()+1, yearlySchedule.getYearValue()); // GET NEXT MONTH
						for(int n = 1; n<=daysInNextMonth; n++){
							week.add(nextMonthScheudle.getDailySchedule().get(n));
						}
						
						// GET DAYS IN CURRENT MONTH
						for(int n = day; n<=numDays; n++){
							week.add(monthSchedule.getDailySchedule().get(n));
						}
					}else{
						// TODO HANDLE ROLL OVER INTO NEXT YEAR
					}
					break; // DONE WITH CURRENT MONTH NO NEED TO CONTINUE LOOPING OVER MONTH
				}else if(day-dayofweek < 0){ // WEEK EXTENDS INTO PREVIOUS MONTH
					int daysInPreviousMonth = dayofweek-day;
					int daysInCurrentMonth = 7-daysInPreviousMonth;
					
					// GET DAYS IN PREVIOUS MONTH
					if(monthSchedule.getMonthValue()-1 > 0){ // IF PRECEEDS CURRENT CALENDAR HANDLE IN ESLE BLOCK
						MonthlySchedule previousMonthScheudle = getScheduleForMonth(monthSchedule.getMonthValue()-1, yearlySchedule.getYearValue()); // GET PREVIOUS MONTH
						calendar.set(Calendar.MONTH, previousMonthScheudle.getMonthValue());
						int numDaysInPreviousMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
						
						for(int n = numDaysInPreviousMonth-(daysInPreviousMonth-1); n<=numDaysInPreviousMonth; n++){
							week.add(previousMonthScheudle.getDailySchedule().get(n));
						}
						calendar.set(Calendar.MONTH, monthSchedule.getMonthValue()); // RESET CALENDAR MONTH BACK TO CURRENT
						
						// GET DAYS IN CURRENT MONTH
						for(int n = day; n<day+daysInCurrentMonth; n++){
							week.add(monthSchedule.getDailySchedule().get(n));
						}
					}else{
						// TODO HANDLE ROLL OVER INTO PREVIOUS YEAR
					}
					day += (daysInCurrentMonth-1); // Increment to the next week  (-1 because ITERATION WITH DO A ++ )
				}
			}
		}
	}
	
	public MonthlySchedule getScheduleForMonth(int month, int year) {
		return yearlySchedule.get(year).getScheduleForMonth(month);
	}

}
