package net.calvineric.nursing;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonthlySchedule {
	
	private int yearValue;
	private int monthValue;
	private Map<Integer, DailySchedule> dailySchedule = new HashMap<Integer, DailySchedule>();;

	public MonthlySchedule(int monthValue, int yearValue) {
		this.monthValue = monthValue;
		this.yearValue = yearValue;
		populateDays();
	}
	
	private void populateDays() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, this.yearValue);
		calendar.set(Calendar.MONTH, this.monthValue);
		calendar.set(Calendar.DATE, 1);
		int numDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		int daysToGenerate = numDays;
		// POPULATE ALL DAYS FOR THE MONTH
		for(int i=1;i<=daysToGenerate;i++){
			this.dailySchedule.put(i, new DailySchedule(i, this.monthValue, this.yearValue));
		}
	}



	public MonthlySchedule(HashMap<Integer, DailySchedule> dailySchedule) {
		this.dailySchedule = dailySchedule;
	}
	
	public String toString() {
		return dailySchedule.toString();
	}

	public Map<Integer, DailySchedule> getDailySchedule() {
		return dailySchedule;
	}

	public void setDailySchedule(Map<Integer, DailySchedule> dailySchedule) {
		this.dailySchedule = dailySchedule;
	}

	public int getMonthValue() {
		return monthValue;
	}

	public void setMonthValue(int monthValue) {
		this.monthValue = monthValue;
	}

	public int getYearValue() {
		return yearValue;
	}

	public void setYearValue(int yearValue) {
		this.yearValue = yearValue;
	}
	
	

}
