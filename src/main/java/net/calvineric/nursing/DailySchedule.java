package net.calvineric.nursing;

import java.util.Calendar;

import net.calvineric.nursing.constants.WorkCodeConstants;

public class DailySchedule {
	
	private int monthValue;
	private int yearValue;
	private int dayValue;
	private String value = WorkCodeConstants.NEUTRAL;
	private boolean locked;
	
	public DailySchedule(int dayValue, int month, int year) {
		this.dayValue = dayValue;
		this.monthValue = month;
		this.yearValue = year;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	public String toString(){
		return value +":" + (locked ? "L":"");
	}
	public int getDayValue() {
		return dayValue;
	}
	public void setDayValue(int dayValue) {
		this.dayValue = dayValue;
	}
	public int getYearValue() {
		return yearValue;
	}
	public void setYearValue(int yearValue) {
		this.yearValue = yearValue;
	}
	public int getMonthValue() {
		return monthValue;
	}
	public void setMonthValue(int monthValue) {
		this.monthValue = monthValue;
	}
	public int nextDay(){
		int nextDay = dayValue;
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, this.getYearValue());
		calendar.set(Calendar.MONTH, this.getMonthValue());
		calendar.set(Calendar.DATE, this.getDayValue());
		int numDays = calendar.getActualMaximum(Calendar.DATE);
		nextDay = this.getDayValue()+1>numDays ? 1:this.getDayValue()+1; // HANDLE LAST DAY OF MONTH INTO FIRST DAY OF NEXT MONTH
		return nextDay;
		
	}
	
	public boolean isWeekend(){
		boolean isWeekend = false;
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, this.getYearValue());
		calendar.set(Calendar.MONTH, this.getMonthValue());
		calendar.set(Calendar.DATE, this.getDayValue());
		
		if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
			isWeekend = true;
		}
		return isWeekend;
	}
	
}
