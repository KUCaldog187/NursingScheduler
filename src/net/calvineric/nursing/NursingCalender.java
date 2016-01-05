package net.calvineric.nursing;

import java.util.HashMap;
import java.util.Map;

public class NursingCalender {
	
	private Map<Integer, DailySchedule> dailySchedule;

	public NursingCalender() {
		this.dailySchedule = new HashMap<Integer, DailySchedule>();
	}
	
	public NursingCalender(HashMap<Integer, DailySchedule> dailySchedule) {
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

}
