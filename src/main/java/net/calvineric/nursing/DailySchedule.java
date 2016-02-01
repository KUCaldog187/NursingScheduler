package net.calvineric.nursing;

import net.calvineric.nursing.constants.WorkCodeConstants;

public class DailySchedule {
	
	private int monthValue;
	private int yearValue;
	private int dayValue;
	private String value = WorkCodeConstants.WORKING;
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
	
}
