package net.calvineric;

import net.calvineric.nursing.NursingCollection;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Tester {
	
	public static void main(String[] args){
	      ApplicationContext context = new ClassPathXmlApplicationContext("net/calvineric/nursing/bean/Nurses.xml");
	      
	      NursingCollection nurses = (NursingCollection) context.getBean("nursingCollection");
	      System.out.println(nurses.getNurseMap().size());
	      
	}
}
