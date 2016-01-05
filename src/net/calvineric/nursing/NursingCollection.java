package net.calvineric.nursing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
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
				nurseMap.put(Integer.parseInt(nurseInfo[0]), new Employee(Integer.parseInt(nurseInfo[0]), nurseInfo[1].split(",")[0], nurseInfo[1].split(",")[1], nurseInfo[2], nurseInfo[3]));
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
