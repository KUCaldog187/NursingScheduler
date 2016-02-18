package net.calvineric.nursing.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.calvineric.nursing.DailySchedule;
import net.calvineric.nursing.Employee;
import net.calvineric.nursing.NursingCollection;
import net.calvineric.nursing.Schedule;
import net.calvineric.nursing.ScheduleManager;
import net.calvineric.nursing.Staffing;

@Controller
@RequestMapping("/schedule")
public class ScheduleController implements ApplicationContextAware {
	
	private ApplicationContext applicationContext = null;
	
	private List<Employee> employeeListSON;
	private List<Employee> employeeListRN;
	private List<Employee> employeeListLPN;
	private List<Employee> employeeListCNA;
	
	HashMap<Integer, String> dateMap = new HashMap<Integer, String>(){{
		put(Calendar.MONDAY, "M");
		put(Calendar.TUESDAY, "T");
		put(Calendar.WEDNESDAY, "W");
		put(Calendar.THURSDAY, "T");
		put(Calendar.FRIDAY, "F");
		put(Calendar.SATURDAY, "S");
		put(Calendar.SUNDAY, "S");
	}};
	
	@RequestMapping(method = RequestMethod.GET, value="/")
	public String scheduleHome(ModelMap model){
		return "schedule";
	}
	
	@RequestMapping(method = RequestMethod.GET, value= "/{month}/{year}")
	public String getCalendar(ModelMap model, @PathVariable("month") Integer month, @PathVariable("year") Integer year){
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DATE, 1);
		int daystoGenerate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		String[] daysArray = new String[daystoGenerate];
		
		for(int i=0; i<daysArray.length; i++){
			calendar.set(Calendar.DATE, i+1);
			daysArray[i] = dateMap.get(calendar.get(Calendar.DAY_OF_WEEK));
		}
		
		model.addAttribute("daysArray", daysArray);
		
		return "calendar";
	}

	@RequestMapping(method = RequestMethod.GET, value= "generate/{id}/{month}/{year}")
	public String generateScheduleForEmployee(ModelMap model, @PathVariable("id") Integer id, @PathVariable("month") Integer month, @PathVariable("year") Integer year){
		boolean success = false;
		
		NursingCollection nurses = (NursingCollection)applicationContext.getBean("nursingCollection");
		
		try {
			success = ScheduleManager.generateScheduleForEmployee(nurses.getNurseMap().get(id), year, month);
//			ScheduleManager.loadEmployeeScheduleFromFile(nurses.getNurseMap().get(id));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.addAttribute("success", success);

		return "generate";
	}
	
	@RequestMapping(method = RequestMethod.GET, value= "staff/{type}/{month}/{year}")
	public String generateScheduleForEmployee(ModelMap model, @PathVariable("type") String type, @PathVariable("month") Integer month, @PathVariable("year") Integer year){
		NursingCollection nurses = (NursingCollection)applicationContext.getBean("nursingCollection");
		
		employeeListSON = new ArrayList<Employee>();
		employeeListRN = new ArrayList<Employee>();
		employeeListLPN = new ArrayList<Employee>();
		employeeListCNA = new ArrayList<Employee>();

		Iterator<Entry<Integer, Employee>> mapIterator = nurses.getNurseMap().entrySet().iterator();
		while(mapIterator.hasNext()){
			Employee availableEmployee = mapIterator.next().getValue();
				categorizeEmployee(availableEmployee);
		}
		
		boolean success = true;
		
		try{
			if(type.equalsIgnoreCase("all")){
				
				Staffing.staffingLogicSON(employeeListSON, month, year);
				Staffing.staffingLogicRN(employeeListRN, month, year);
				Staffing.staffingLogicLPN(employeeListLPN, month, year);
				Staffing.staffingLogicCNA(employeeListCNA, month, year);
				
			}else if (type.equalsIgnoreCase("son")) {
				Staffing.staffingLogicSON(employeeListSON, month, year);
			}else if (type.equalsIgnoreCase("rn")) {
				Staffing.staffingLogicRN(employeeListRN, month, year);
			}else if (type.equalsIgnoreCase("lpn")) {
				Staffing.staffingLogicLPN(employeeListLPN, month, year);
			}else if (type.equalsIgnoreCase("cna")) {
				Staffing.staffingLogicCNA(employeeListCNA, month, year);
			}
		}catch(IOException ex){
			success = false;
		}

		model.addAttribute("success", success );
	
		return "generate";
		
	}
	

	
	@RequestMapping(method = RequestMethod.GET, value= "save/{id}/{year}/{month}/{day}/{value}")
	public String saveScheduleForEmployee(ModelMap model, @PathVariable("id") Integer id, @PathVariable("year") Integer year, @PathVariable("month") Integer month, @PathVariable("day") Integer day, @PathVariable("value") String workCode){
		boolean success = false;
		
		NursingCollection nurses = (NursingCollection)applicationContext.getBean("nursingCollection");
		
		try {
			Schedule schedue = nurses.getNurseMap().get(id).getSchedule();
			schedue.getYearlySchedule(year).getScheduleForMonth(month).getDailySchedule().get(day).setValue(workCode);
			schedue.getYearlySchedule(year).getScheduleForMonth(month).getDailySchedule().get(day).setLocked(true);
			success = ScheduleManager.saveEmployeeToFile(nurses.getNurseMap().get(id), year);
			success = true;
		} catch (IOException e) {
			success = false;
			e.printStackTrace();
		}
		
		model.addAttribute("workerid", id);
		model.addAttribute("month", month);
		model.addAttribute("day", day);
		model.addAttribute("success", success);

		return "lockIt";
	}
	
	@RequestMapping(method = RequestMethod.GET, value= "lock/{id}/{year}/{month}/{day}")
	public String toggleLockScheduleForEmployee(ModelMap model, @PathVariable("id") Integer id, @PathVariable("year") Integer year, @PathVariable("month") Integer month, @PathVariable("day") Integer day){
		boolean success = false;
		
		NursingCollection nurses = (NursingCollection)applicationContext.getBean("nursingCollection");
		
		Schedule schedue = nurses.getNurseMap().get(id).getSchedule();
		DailySchedule dailySchedule = schedue.getYearlySchedule(year).getScheduleForMonth(month).getDailySchedule().get(day);
		
		try {
			
			if(dailySchedule.isLocked()){
				dailySchedule.setLocked(false);
				ScheduleManager.saveEmployeeToFile(nurses.getNurseMap().get(id), year);
				success = false;
			}else{
				dailySchedule.setLocked(true);
				ScheduleManager.saveEmployeeToFile(nurses.getNurseMap().get(id), year);
				success = true;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.addAttribute("workerid", id);
		model.addAttribute("month", month);
		model.addAttribute("day", day);
		model.addAttribute("success", success);

		return "lockIt";
	}
	
	@RequestMapping(method = RequestMethod.GET, value= "/{id}/{month}/{year}")
	public String getScheduleForEmployee(ModelMap model, @PathVariable("id") Integer id, @PathVariable("month") Integer month, @PathVariable("year") Integer year){
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DATE, 1);
		int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		String[] daysArray = new String[daysInMonth];
		
		for(int i=0; i<daysArray.length; i++){
			calendar.set(Calendar.DATE, i+1);
			daysArray[i] = dateMap.get(calendar.get(Calendar.DAY_OF_WEEK));
		}
			
		getEmployee(model, id);
		
		model.addAttribute("daysArray", daysArray);
		model.addAttribute("daysInMonth", daysInMonth);
		model.addAttribute("month", month);
		model.addAttribute("year", year);
		
		return "calendar";
	}


	public String getEmployee(ModelMap model, Integer id){
		NursingCollection nurses = (NursingCollection)applicationContext.getBean("nursingCollection");
		Employee employee = nurses.getNurseMap().get(id);
		
		employeeListSON = new ArrayList<Employee>();
		employeeListRN = new ArrayList<Employee>();
		employeeListLPN = new ArrayList<Employee>();
		employeeListCNA = new ArrayList<Employee>();

		categorizeEmployee(employee);
		if(employee.getPosition().equals("SON")){
			// If SON, get all available employees for same shift
			Iterator<Entry<Integer, Employee>> mapIterator = nurses.getNurseMap().entrySet().iterator();
			while(mapIterator.hasNext()){
				Employee availableEmployee = mapIterator.next().getValue();
				if(availableEmployee.getId() != employee.getId() && availableEmployee.getDefaultShift().equals(employee.getDefaultShift())){
					categorizeEmployee(availableEmployee);
				}
			}
		}
		model.addAttribute("employeeListSON", employeeListSON);
		model.addAttribute("employeeListRN", employeeListRN);
		model.addAttribute("employeeListLPN", employeeListLPN);
		model.addAttribute("employeeListCNA", employeeListCNA);
		return "employee";
	}
	
	private void categorizeEmployee(Employee employee){
		if(employee.getPosition().equals("SON")){
			employeeListSON.add(employee);
		}else if(employee.getPosition().equals("RN")){
			employeeListRN.add(employee);
		}else if(employee.getPosition().equals("LPN")){
			employeeListLPN.add(employee);
		}else if(employee.getPosition().equals("CNA")){
			employeeListCNA.add(employee);
		}
	}
	
	@Override
	public void setApplicationContext(final ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
}
