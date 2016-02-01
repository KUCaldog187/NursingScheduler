package net.calvineric.nursing.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import net.calvineric.nursing.Employee;
import net.calvineric.nursing.NursingCollection;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/nurse")
public class NursingController implements ApplicationContextAware{
	
	private ApplicationContext applicationContext = null;
	
	private List<Employee> employeeListSON;
	private List<Employee> employeeListRN;
	private List<Employee> employeeListLPN;
	private List<Employee> employeeListCNA;

	@RequestMapping(value= "/{id}")
	public String getEmployee(ModelMap model, @PathVariable("id") Integer id){
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
