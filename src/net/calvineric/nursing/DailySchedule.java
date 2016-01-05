package net.calvineric.nursing;

public class DailySchedule {
	
	private String value;
	private boolean locked;
	
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
}
