package com.example.jsf;

public class PersonBean {

	public PersonBean() {
		
	}
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	private String filteredName;

	public String getFilteredName() {
		if (filteredName == null) {
			filteredName = "";
		}
		return filteredName;
	}

	public void setFilteredName(String filteredName) {
		this.filteredName = filteredName;
	}
	
	public void call() {
		if (this.getName() != null && !this.getName().isEmpty()) {
			this.setFilteredName(this.getName());
		} else {
			this.setFilteredName("Name is blank or empty");
		}
	}
}
