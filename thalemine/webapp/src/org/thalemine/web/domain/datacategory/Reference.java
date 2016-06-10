package org.thalemine.web.domain.datacategory;

public class Reference {

	public Reference(){
		
	}
	private String name;
	private String url;
	
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	@Override
	public String toString() {
		return "Reference [name=" + name + ", url=" + url + "]";
	}
	
	
}
