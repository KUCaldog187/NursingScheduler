package net.calvineric.nursing;

import java.util.HashMap;
import java.util.Map;

public class Employee {
	
	private int id;
	private String lastName;
	private String firstName;
	private String position;
	private String defaultShift;
	private int year;
	private Map<Integer, YearlySchedule> yearlySchedule;
	
	public Employee(int id, String lastName, String firstName, String position, String defaultShift, int year) {
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.position = position;
		this.defaultShift = defaultShift;
		this.year = year;
		this.yearlySchedule = new HashMap<Integer,YearlySchedule>(); 
		addYearlySchedule(new YearlySchedule(year), year);
		initializeSchedule(year);
	}
	
	private void initializeSchedule(int year) {
		ScheduleManager.loadEmployeeScheduleFromFile(this, year);
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getDefaultShift() {
		return defaultShift;
	}
	public void setDefaultShift(String defaultShift) {
		this.defaultShift = defaultShift;
	}

	public YearlySchedule getYearlySchedule(int year) {
		return yearlySchedule.get(year);
	}

	public void addYearlySchedule(YearlySchedule yearlySchedule, int year) {
		this.yearlySchedule.put(year, yearlySchedule);
	}

	public Map<Integer, YearlySchedule> getYearlySchedule() {
		return yearlySchedule;
	}

	public void setYearlySchedule(Map<Integer, YearlySchedule> yearlySchedule) {
		this.yearlySchedule = yearlySchedule;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

}
