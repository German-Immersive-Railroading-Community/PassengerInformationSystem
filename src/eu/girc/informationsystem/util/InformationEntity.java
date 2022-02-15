package eu.girc.informationsystem.util;

import eu.derzauberer.javautils.parser.JsonParser;

public abstract class InformationEntity {

	private String name;
	private String displayName;
	
	public InformationEntity(String name, String displayName) {
		this.name = name;
		this.displayName = displayName;
	}
	
	public String getName() {
		return name;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public abstract JsonParser toJson();
	
	@Override
	public String toString() {
		return toJson().toString();
	}
	
}