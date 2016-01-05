package net.calvineric.nursing;

public class Employee {
	
	private int id;
	private String lastName;
	private String firstName;
	private String position;
	private String defaultShift;
	private YearlySchedule yearlySchedule;
	
	public Employee(int id, String lastName, String firstName, String position, String defaultShift) {
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.position = position;
		this.defaultShift = defaultShift;
		this.yearlySchedule = new YearlySchedule(); 
		initializeSchedule();
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

	public YearlySchedule getYearlySchedule() {
		return yearlySchedule;
	}

	public void setYearlySchedule(YearlySchedule yearlySchedule) {
		this.yearlySchedule = yearlySchedule;
	}

}
