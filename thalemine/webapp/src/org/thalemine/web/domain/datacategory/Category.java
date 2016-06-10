package org.thalemine.web.domain.datacategory;

import java.util.ArrayList;
import java.util.List;

public class Category {

	private String name;
	private String description;
	private List<DataSetSO> dataSets = new ArrayList<DataSetSO>();
	
	
	public Category(){
		
	}
	
	public Category(String name){
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<DataSetSO> getDataSets() {
		return dataSets;
	}

	public void setDataSets(List<DataSetSO> dataSets) {
		this.dataSets = dataSets;
	}

	public void addDataSet(DataSetSO dataset){
		dataSets.add(dataset);
	}

	@Override
	public String toString() {
		return "Category [name=" + name + ", description=" + description + ", dataSets=" + dataSets + "]";
	}
	
	
		
}
