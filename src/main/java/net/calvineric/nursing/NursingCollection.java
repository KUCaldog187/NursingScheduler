package net.calvineric.nursing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NursingCollection {
	
	private Map<Integer, Employee> nurseMap;
	
	public NursingCollection(File nurseFile){
		populateNurseSet(nurseFile);
	}

	private void populateNurseSet(File nurseFile) {
		// TODO Auto-generated method stub
		String nurseName = null;
		try (BufferedReader br = new BufferedReader(new FileReader(nurseFile))) {
			while((nurseName = br.readLine()) != null){
				String[] nurseInfo = nurseName.split("\\s");
				if(nurseMap == null){
					nurseMap = new HashMap<Integer, Employee>();
				}
				int year = 2015;
				int id = Integer.parseInt(nurseInfo[0]);
				String lastName = nurseInfo[1].split(",")[0];
				String firstName = nurseInfo[1].split(",")[1];
				String position = nurseInfo[2];
				String defaultShift = nurseInfo[3];
				String specialCondition = "";
				List<Integer> defaultDaysOff = new ArrayList<Integer>();
				for(int i=4;i<nurseInfo.length;i++){
					try{
						defaultDaysOff.add(Integer.parseInt(nurseInfo[i]));
					}catch(NumberFormatException ex){
						specialCondition = nurseInfo[i]; 
						System.out.println("Special Condition Configured for employee " + id + " Condition: " + specialCondition);
					}
				}
				nurseMap.put(id, new Employee(id, lastName, firstName, position, defaultShift, specialCondition, defaultDaysOff, year ));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Map<Integer,Employee> getNurseMap() {
		return nurseMap;
	}

	public void setNurseMap(Map<Integer, Employee> nurseMap) {
		this.nurseMap = nurseMap;
	}
	
	

}
