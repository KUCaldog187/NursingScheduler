package net.calvineric.nursing;

import java.io.IOException;
import java.util.List;


public class Employee {
	
	private int id;
	private String lastName;
	private String firstName;
	private String position;
	private String defaultShift;
	private String specialCondition;
	private List<Integer> defaultDaysOff;
	private Schedule schedule;
	private String evenorodd;
	
	public Employee(int id, String lastName, String firstName, String position, String defaultShift, String specialCondition, String evenorodd, List<Integer> defaultDaysOff, int year) {
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.position = position;
		this.defaultShift = defaultShift;
		this.specialCondition = specialCondition;
		this.defaultDaysOff = defaultDaysOff;
		this.schedule = new Schedule(); 
		this.schedule.addYearlySchedule(new YearlySchedule(year));		
		this.evenorodd = evenorodd;
		initializeSchedule();
		initializeDefaultDaysOff();
	}
	
	private void initializeDefaultDaysOff() {
		try {
			ScheduleManager.loadDefaultDaysOff(this);
		} catch (IOException e) {
			// TODO Log this
			System.out.println("Failure Saving after loading Default Day for employee " + this.id);
			e.printStackTrace();
		}
	}

	private void initializeSchedule() {
		ScheduleManager.loadEmployeeScheduleFromFile(this);
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

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public List<Integer> getDefaultDaysOff() {
		return defaultDaysOff;
	}

	public void setDefaultDaysOff(List<Integer> defaultDaysOff) {
		this.defaultDaysOff = defaultDaysOff;
	}

	public String getSpecialCondition() {
		return specialCondition;
	}

	public void setSpecialCondition(String specialCondition) {
		this.specialCondition = specialCondition;
	}

	public String getEvenOrOdd() {
		return this.evenorodd;
	}
	
}
