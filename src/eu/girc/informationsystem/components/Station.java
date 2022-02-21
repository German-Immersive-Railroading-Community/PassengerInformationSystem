package eu.girc.informationsystem.components;

import eu.derzauberer.javautils.parser.JsonParser;
import eu.girc.informationsystem.util.Entity;

public class Station extends Entity {

	private int plattforms;
	
	public Station(String name, String displayName, int plattforms) {
		super(name, displayName);
		this.plattforms = plattforms;
	}
	
	public Station(JsonParser parser) {
		this("Unnamed", "Unnamed", 1);
		fromJson(parser);
	}
	
	public int getPlattforms() {
		return plattforms;
	}
	
	@Override
	public void fromJson(JsonParser parser) {
		super.fromJson(parser);
		plattforms = parser.getInt("plattforms");
	}
	
	@Override
	public JsonParser toJson() {
		JsonParser parser = new JsonParser();
		parser.set("name", getName());
		parser.set("displayName", getDisplayName());
		parser.set("plattforms", getPlattforms());
		return parser;
	}

}