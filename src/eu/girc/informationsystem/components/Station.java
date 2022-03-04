package eu.girc.informationsystem.components;

import eu.derzauberer.javautils.parser.JsonParser;
import eu.girc.informationsystem.main.Main;
import eu.girc.informationsystem.util.Entity;
import eu.girc.informationsystem.util.EntityList;
import eu.girc.informationsystem.util.Time;

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
	
	public EntityList<Line> getLines() {
		EntityList<Line> lines = new EntityList<>();
		for (Line line : Main.getLines()) {
			if (line.getStations().contains(getName())) {
				lines.add(line);
			}
		}
		Time.timeSort(lines, this);
		return lines;
	}
	
	public EntityList<Line> getLines(int plattform) {
		EntityList<Line> lines = new EntityList<>();
		for (Line line : Main.getLines()) {
			if (line.getStations().contains(getName()) && line.getLineStation(this).getPlattform() == plattform) {
				lines.add(line);
			}
		}
		Time.timeSort(lines, this);
		return lines;
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
	
	@Override
	public boolean isValid() {
		return super.isValid() && plattforms > 0;
	}

}