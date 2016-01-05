package net.calvineric.nursing;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class YearlySchedule {
	
	private Map<Integer, NursingCalender> monthlySchedule;
	
	public YearlySchedule(){
		this.monthlySchedule = new HashMap<Integer, NursingCalender>(){{
			put(Calendar.JANUARY, new NursingCalender());
			put(Calendar.FEBRUARY, new NursingCalender());
			put(Calendar.MARCH, new NursingCalender());
			put(Calendar.APRIL, new NursingCalender());
			put(Calendar.MAY, new NursingCalender());
			put(Calendar.JUNE, new NursingCalender());
			put(Calendar.JULY, new NursingCalender());
			put(Calendar.AUGUST, new NursingCalender());
			put(Calendar.SEPTEMBER, new NursingCalender());
			put(Calendar.OCTOBER, new NursingCalender());
			put(Calendar.NOVEMBER, new NursingCalender());
			put(Calendar.DECEMBER, new NursingCalender());
		}};
	}

	public NursingCalender getScheduleForMonth(int month) {
		return monthlySchedule.get(month);
	}
	
	public String toString() {
		String result = "";
		Iterator<Entry<Integer, NursingCalender>> monthlyIterator = this.monthlySchedule.entrySet().iterator();
		while (monthlyIterator.hasNext()) {
			Entry<Integer, NursingCalender> entry = (Entry<Integer, NursingCalender>) monthlyIterator.next();
			result+=entry.getKey() + " " + entry.getValue().toString()+"\n";
		}

		return result;
	}

	public Map<Integer, NursingCalender> getMonthlySchedule() {
		return monthlySchedule;
	}

	public void setMonthlySchedule(Map<Integer, NursingCalender> monthlySchedule) {
		this.monthlySchedule = monthlySchedule;
	}

}
