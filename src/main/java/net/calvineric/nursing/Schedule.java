package net.calvineric.nursing;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Schedule {
	
	private final static int RANGE_LIMIT = 5;
	private Map<Integer, YearlySchedule> yearlySchedule = new HashMap<Integer, YearlySchedule>();
	
	public YearlySchedule getYearlySchedule(int year) {
		if(yearlySchedule.get(year) == null && !exceedRange(year)){
			addYearlySchedule(new YearlySchedule(year));
		}
		return yearlySchedule.get(year);
	}

	// Only generate schedule for 5 years +- current year
	private boolean exceedRange(int year) {
		boolean exceeds = false;
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		int upperRange = currentYear + RANGE_LIMIT;
		int lowerRange = currentYear - RANGE_LIMIT;
		
		if(year < lowerRange ||  year > upperRange){
			exceeds = true;
		}
		return exceeds;
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
	
	// TODO REFACTOR THIS METHOD
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

				Set<DailySchedule> week = null;
				if(monthSchedule.getMonthValue() == 11 && weekofyear == 1){  // HANDLE 1st WEEK OF NEXT YEAR
					YearlySchedule nextyear = getYearlySchedule(yearlySchedule.getYearValue()+1);
					if(nextyear == null){
						// dont generate anything 
						// advance iterator to end
						day = numDays;
						continue;
					}else{
						week = nextyear.getWeeks().get(weekofyear);
					}
				}else{
					week = weeksMap.get(weekofyear);
				}
				
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
					if(monthSchedule.getMonthValue()+1 <= 11){ // IF EXCEEDS CURRENT CALENDAR HANDLE IN ESLE BLOCK 
						MonthlySchedule nextMonthSchedule = getScheduleForMonth(monthSchedule.getMonthValue()+1, yearlySchedule.getYearValue()); // GET NEXT MONTH
						for(int n = 1; n<=daysInNextMonth; n++){
							week.add(nextMonthSchedule.getDailySchedule().get(n));
						}
						
						// GET DAYS IN CURRENT MONTH
						for(int n = day; n<=numDays; n++){
							week.add(monthSchedule.getDailySchedule().get(n));
						}
					}else{
						// TODO HANDLE ROLL OVER INTO NEXT YEAR
						// TODO SHOULD SCHEDULE CONSIDER ROLLING OVER INTO NEXT AND PREVIOUS YEARS???
						if(!exceedRange(yearlySchedule.getYearValue()-1)){
							MonthlySchedule nextMonthScheudle = getScheduleForMonth(0, yearlySchedule.getYearValue()+1); // GET JANUARY  MONTH OF NEXT YEAR
							for(int n = 1; n<=daysInNextMonth; n++){
								week.add(nextMonthScheudle.getDailySchedule().get(n));
							}
							
							// GET DAYS IN CURRENT MONTH
							for(int n = day; n<=numDays; n++){
								week.add(monthSchedule.getDailySchedule().get(n));
							}
						}
					}
					break; // DONE WITH CURRENT MONTH NO NEED TO CONTINUE LOOPING OVER MONTH
				}else if(day-dayofweek < 0){ // WEEK EXTENDS INTO PREVIOUS MONTH
					int daysInPreviousMonth = dayofweek-day;
					int daysInCurrentMonth = 7-daysInPreviousMonth;
					
					// GET DAYS IN PREVIOUS MONTH
					if(monthSchedule.getMonthValue()-1 >= 0){ // IF PRECEEDS CURRENT CALENDAR HANDLE IN ESLE BLOCK
						MonthlySchedule previousMonthSchedule = getScheduleForMonth(monthSchedule.getMonthValue()-1, yearlySchedule.getYearValue()); // GET PREVIOUS MONTH
						calendar.set(Calendar.MONTH, previousMonthSchedule.getMonthValue());
						int numDaysInPreviousMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
						
						for(int n = numDaysInPreviousMonth-(daysInPreviousMonth-1); n<=numDaysInPreviousMonth; n++){ // WHICH DAY TO START ON IN PREVIOUS MONTH
							week.add(previousMonthSchedule.getDailySchedule().get(n));
						}
						calendar.set(Calendar.MONTH, monthSchedule.getMonthValue()); // RESET CALENDAR MONTH BACK TO CURRENT
						
						// GET DAYS IN CURRENT MONTH
						for(int n = day; n<day+daysInCurrentMonth; n++){
							week.add(monthSchedule.getDailySchedule().get(n));
						}
					}else{
						// TODO HANDLE ROLL OVER INTO PREVIOUS YEAR  **DONE**
						if(!exceedRange(yearlySchedule.getYearValue()-1)){
							MonthlySchedule previousMonthSchedule = getScheduleForMonth(11, yearlySchedule.getYearValue()-1); // GET DECEMEBER MONTH OF PREVIOUS YEAR
							calendar.set(Calendar.MONTH, previousMonthSchedule.getMonthValue());
							calendar.set(Calendar.YEAR, previousMonthSchedule.getYearValue());
							int numDaysInPreviousMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
							
							for(int n = numDaysInPreviousMonth-(daysInPreviousMonth-1); n<=numDaysInPreviousMonth; n++){ // WHICH DAY TO START ON IN PREVIOUS MONTH
								week.add(previousMonthSchedule.getDailySchedule().get(n));
							}
							calendar.set(Calendar.MONTH, monthSchedule.getMonthValue()); // RESET CALENDAR MONTH BACK TO CURRENT
							calendar.set(Calendar.YEAR, monthSchedule.getYearValue()); // RESET CALENDAR YEAR BACK TO CURRENT
							
							// GET DAYS IN CURRENT MONTH
							for(int n = day; n<day+daysInCurrentMonth; n++){
								week.add(monthSchedule.getDailySchedule().get(n));
							}
						}
					}
					day += (daysInCurrentMonth-1); // Increment to the next week  (-1 because ITERATION WITH DO A ++ )
				}
			}
		}
	}
	
	public MonthlySchedule getScheduleForMonth(int month, int year) {
		MonthlySchedule schedule = null;
		try{
			schedule =  this.getYearlySchedule(year).getScheduleForMonth(month);
		}catch(NullPointerException ex){
			System.out.println();
		}
		
		return schedule;
	}
	
	@Override
	public String toString() {
		String result = "";
		for(YearlySchedule schedule: this.yearlySchedule.values()){
			result += schedule.toString() + "\n";
		}
		return result;
	}

}
