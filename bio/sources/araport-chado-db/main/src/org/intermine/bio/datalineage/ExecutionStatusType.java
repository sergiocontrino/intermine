package org.intermine.bio.datalineage;

public enum ExecutionStatusType {

	SUCCESS (0, "Success"),
	FAILURE (1, "Failure"),
	IN_PROGRESS (2, "In Progress"),
	PARTIAL_SUCCESS(3, "Partial Success");
	
	private int code;
	private String name;

	private ExecutionStatusType(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getValue() {
		return this.name;
	}
	
	public int getCode(){
		return this.code;
	}
	
	
}
