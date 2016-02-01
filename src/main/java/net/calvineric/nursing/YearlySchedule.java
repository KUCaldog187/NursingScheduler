package net.calvineric.nursing;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class YearlySchedule {
	
	private int yearValue;
	private Map<Integer, MonthlySchedule> monthlySchedule;
	private Map<Integer, Set<DailySchedule>> weeks = new HashMap<Integer, Set<DailySchedule>>();
	
	public YearlySchedule(int year){
		this.yearValue = year;
		this.monthlySchedule = new HashMap<Integer, MonthlySchedule>(){{
			put(Calendar.JANUARY, new MonthlySchedule(Calendar.JANUARY, yearValue));
			put(Calendar.FEBRUARY, new MonthlySchedule(Calendar.FEBRUARY, yearValue));
			put(Calendar.MARCH, new MonthlySchedule(Calendar.MARCH, yearValue));
			put(Calendar.APRIL, new MonthlySchedule(Calendar.APRIL, yearValue));
			put(Calendar.MAY, new MonthlySchedule(Calendar.MAY, yearValue));
			put(Calendar.JUNE, new MonthlySchedule(Calendar.JUNE, yearValue));
			put(Calendar.JULY, new MonthlySchedule(Calendar.JULY, yearValue));
			put(Calendar.AUGUST, new MonthlySchedule(Calendar.AUGUST, yearValue));
			put(Calendar.SEPTEMBER, new MonthlySchedule(Calendar.SEPTEMBER, yearValue));
			put(Calendar.OCTOBER, new MonthlySchedule(Calendar.OCTOBER, yearValue));
			put(Calendar.NOVEMBER, new MonthlySchedule(Calendar.NOVEMBER, yearValue));
			put(Calendar.DECEMBER, new MonthlySchedule(Calendar.DECEMBER, yearValue));
		}};
		populateWeeks(this.yearValue);
	}

	public static void main(String[] args) {
		
	}
	
	private void populateWeeks(int year) {
		Map<Integer, Set<DailySchedule>> weeksMap = getWeeks();
		
		for (Iterator<MonthlySchedule> iterator = monthlySchedule.values().iterator(); iterator.hasNext();) {
			MonthlySchedule monthSchedule = (MonthlySchedule) iterator.next();

			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, getYearValue());
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
						MonthlySchedule nextMonthScheudle = getScheduleForMonth(monthSchedule.getMonthValue()+1); // GET NEXT MONTH
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
						MonthlySchedule previousMonthScheudle = getScheduleForMonth(monthSchedule.getMonthValue()-1); // GET PREVIOUS MONTH
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

	public MonthlySchedule getScheduleForMonth(int month) {
		return monthlySchedule.get(month);
	}
	
	public String toString() {
		String result = "";
		Iterator<Entry<Integer, MonthlySchedule>> monthlyIterator = this.monthlySchedule.entrySet().iterator();
		while (monthlyIterator.hasNext()) {
			Entry<Integer, MonthlySchedule> entry = (Entry<Integer, MonthlySchedule>) monthlyIterator.next();
			result+=entry.getKey() + " " + entry.getValue().toString()+"\n";
		}

		return result;
	}

	public Map<Integer, MonthlySchedule> getMonthlySchedule() {
		return monthlySchedule;
	}

	public void setMonthlySchedule(Map<Integer, MonthlySchedule> monthlySchedule) {
		this.monthlySchedule = monthlySchedule;
	}

	public int getYearValue() {
		return yearValue;
	}

	public Map<Integer, Set<DailySchedule>> getWeeks() {
		return weeks;
	}

	public void setWeeks(Map<Integer, Set<DailySchedule>> weeks) {
		this.weeks = weeks;
	}

}
