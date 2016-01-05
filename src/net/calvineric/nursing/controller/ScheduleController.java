package net.calvineric.nursing.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.calvineric.controller.BaseController;
import net.calvineric.nursing.DailySchedule;
import net.calvineric.nursing.Employee;
import net.calvineric.nursing.NursingCalender;
import net.calvineric.nursing.NursingCollection;
import net.calvineric.nursing.ScheduleManager;

@Controller
@RequestMapping("/schedule")
public class ScheduleController extends BaseController {
	
	
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
		int daystoGenerate = calendar.getActualMaximum(Calendar.DATE);
		
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
			success = ScheduleManager.generateScheduleForEmployee(nurses.getNurseMap().get(id), month);
//			ScheduleManager.loadEmployeeScheduleFromFile(nurses.getNurseMap().get(id));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.addAttribute("success", success);

		return "generate";
	}
	
	@RequestMapping(method = RequestMethod.GET, value= "save/{id}/{month}/{day}/{value}")
	public String saveScheduleForEmployee(ModelMap model, @PathVariable("id") Integer id, @PathVariable("month") Integer month, @PathVariable("day") Integer day, @PathVariable("value") String workCode){
		boolean success = false;
		
		NursingCollection nurses = (NursingCollection)applicationContext.getBean("nursingCollection");
		
		try {
			nurses.getNurseMap().get(id).getYearlySchedule().getScheduleForMonth(month).getDailySchedule().get(day).setValue(workCode);
			nurses.getNurseMap().get(id).getYearlySchedule().getScheduleForMonth(month).getDailySchedule().get(day).setLocked(true);
			success = ScheduleManager.saveEmployeeToFile(nurses.getNurseMap().get(id));
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
	
	@RequestMapping(method = RequestMethod.GET, value= "lock/{id}/{month}/{day}")
	public String toggleLockScheduleForEmployee(ModelMap model, @PathVariable("id") Integer id, @PathVariable("month") Integer month, @PathVariable("day") Integer day){
		boolean success = false;
		
		NursingCollection nurses = (NursingCollection)applicationContext.getBean("nursingCollection");
		
		DailySchedule dailySchedule = nurses.getNurseMap().get(id).getYearlySchedule().getScheduleForMonth(month).getDailySchedule().get(day);
		
		try {
			
			if(dailySchedule.isLocked()){
				dailySchedule.setLocked(false);
				ScheduleManager.saveEmployeeToFile(nurses.getNurseMap().get(id));
				success = false;
			}else{
				dailySchedule.setLocked(true);
				ScheduleManager.saveEmployeeToFile(nurses.getNurseMap().get(id));
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
		
		NursingCollection nurses = (NursingCollection)applicationContext.getBean("nursingCollection");
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		int daysInMonth = calendar.getActualMaximum(Calendar.DATE);
		
		String[] daysArray = new String[daysInMonth];
		
		for(int i=0; i<daysArray.length; i++){
			calendar.set(Calendar.DATE, i+1);
			daysArray[i] = dateMap.get(calendar.get(Calendar.DAY_OF_WEEK));
		}
			
		getEmployee(model, id);
		
		model.addAttribute("daysArray", daysArray);
		model.addAttribute("daysInMonth", daysInMonth);
		model.addAttribute("month", month);
		
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
}
