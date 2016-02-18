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
	}
	
	public MonthlySchedule getScheduleForMonth(int month) {
		return monthlySchedule.get(month);
	}
	
	public String toString() {
		String result = "";
		Iterator<Entry<Integer, MonthlySchedule>> monthlyIterator = this.monthlySchedule.entrySet().iterator();
		while (monthlyIterator.hasNext()) {
			Entry<Integer, MonthlySchedule> entry = (Entry<Integer, MonthlySchedule>) monthlyIterator.next();
			result+= entry.getValue().getYearValue()+ " " +entry.getKey() + " " + entry.getValue().toString()+"\n";
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
