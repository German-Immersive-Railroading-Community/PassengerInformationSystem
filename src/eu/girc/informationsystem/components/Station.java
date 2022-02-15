package eu.girc.informationsystem.components;

import eu.derzauberer.javautils.parser.JsonParser;
import eu.girc.informationsystem.util.InformationEntity;

public class Station extends InformationEntity {

	private int plattforms;
	
	public Station(String name, String displayName, int plattforms) {
		super(name, displayName);
		this.plattforms = plattforms;
	}
	
	public Station(JsonParser parser) {
		this(parser.getString("name"), parser.getString("displayname"), parser.getInt("plattforms"));
	}
	
	public int getPlattforms() {
		return plattforms;
	}
	
	@Override
	public JsonParser toJson() {
		JsonParser parser = new JsonParser();
		parser.set("name", getName());
		parser.set("displayname", getDisplayName());
		parser.set("plattforms", getPlattforms());
		return parser;
	}

}