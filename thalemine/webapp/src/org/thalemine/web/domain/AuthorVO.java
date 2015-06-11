package org.thalemine.web.domain;

import java.util.ArrayList;
import java.util.List;

import org.intermine.api.results.ResultElement;
import org.apache.log4j.Logger;

public class AuthorVO {
	
	protected static final Logger LOG = Logger.getLogger(AuthorVO.class);
	
	private String objectId;
	private String name;
	private String lastName;
	private String firstName;
	private String initials;
	
	public AuthorVO(){
		
	}
	
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getInitials() {
		return initials;
	}
	public void setInitials(String initials) {
		this.initials = initials;
	}
	
	public String toString(){
		
		String result = null;
		
		StringBuilder strBuilder = new StringBuilder();
		
		if (lastName!=null){
			strBuilder.append(lastName);
		}
		
		if (strBuilder.length() > 0){
			strBuilder.append(" ");
		}
		
		if (initials!=null){
			strBuilder.append(initials);
		}
		
		
		return result;
		
		
	}
	

}
