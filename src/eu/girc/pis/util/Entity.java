package eu.girc.pis.util;

import eu.derzauberer.javautils.parser.JsonParser;

public abstract class Entity {

	private String name;
	private String displayName;
	
	public Entity(String name, String displayName) {
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
	
	public void fromJson(JsonParser parser) {
		name = parser.getString("name");
		displayName = parser.getString("displayName");
	}
	
	public abstract JsonParser toJson();
	
	public boolean isValid() {
		return getName() != null && !getName().isEmpty() && !name.contains(" ") && getDisplayName() != null && !getDisplayName().isEmpty();
	}
	
	@Override
	public String toString() {
		return toJson().toString();
	}
	
}