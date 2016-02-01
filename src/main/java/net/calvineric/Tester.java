package net.calvineric;

import java.util.Calendar;
import java.util.GregorianCalendar;

import net.calvineric.nursing.NursingCollection;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Tester {
	
	public static void main(String[] args){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2015);
		calendar.set(Calendar.MONTH, Calendar.FEBRUARY);
		calendar.set(Calendar.DATE, 1);
		int numDays = calendar.getActualMaximum(Calendar.DATE);
		
		System.out.println(numDays);
	}
}
