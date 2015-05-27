package org.intermine.bio.domain.source;

public class SourceGenotype {

	private String name;
	private String uniqueName;
	private String uniqueAccession;
	private String description;
	
	public SourceGenotype(){
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUniqueName() {
		return uniqueName;
	}
	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}
	public String getUniqueAccession() {
		return uniqueAccession;
	}
	public void setUniqueAccession(String uniqueAccession) {
		this.uniqueAccession = uniqueAccession;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "SourceGenotype [name=" + name + ", uniquename=" + uniqueName + ", uniqueAccession=" + uniqueAccession
				+ ", description=" + description + "]";
	}
	
	
}
