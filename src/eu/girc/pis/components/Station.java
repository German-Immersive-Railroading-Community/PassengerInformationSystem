package eu.girc.pis.components;

import eu.derzauberer.javautils.parser.JsonParser;
import eu.girc.pis.main.Main;
import eu.girc.pis.util.Entity;
import eu.girc.pis.util.EntityList;

public class Station extends Entity {

	private int platforms;
	private Box box;
	
	public Station(String name, String displayName, int platforms) {
		super(name, displayName);
		this.platforms = platforms;
	}
	
	public Station(JsonParser parser) {
		this("Unnamed", "Unnamed", 1);
		fromJson(parser);
	}
	
	public int getPlatforms() {
		return platforms;
	}
	
	public EntityList<Line> getLines() {
		EntityList<Line> lines = new EntityList<>();
		for (Line line : Main.getLines()) {
			if (line.getStations().contains(getName())) {
				lines.add(line);
				line.calculateDepartueTimes();
			}
		}
		lines.getEntities().sort((line1, line2) -> line1.getLineStation(this).getDeparture().compareTo(line2.getLineStation(this).getDeparture()));
		return lines;
	}
	
	public EntityList<Line> getLines(int plattform) {
		EntityList<Line> lines = new EntityList<>();
		for (Line line : Main.getLines()) {
			if (line.getStations().contains(getName()) && line.getLineStation(this).getPlatform() == plattform) {
				lines.add(line);
				line.calculateDepartueTimes();
			}
		}
		lines.getEntities().sort((line1, line2) -> line1.getLineStation(this).getDeparture().compareTo(line2.getLineStation(this).getDeparture()));
		return lines;
	}
	
	public Box getBox() {
		return box;
	}
	
	public void setBox(Box box) {
		this.box = box;
	}
	
	@Override
	public void fromJson(JsonParser parser) {
		super.fromJson(parser);
		platforms = parser.getInt("platforms");
		if (parser.get("box") != null) box = new Box(parser.getJsonObject("box"));
	}
	
	@Override
	public JsonParser toJson() {
		JsonParser parser = new JsonParser();
		parser.set("name", getName());
		parser.set("displayName", getDisplayName());
		parser.set("platforms", getPlatforms());
		if (box != null) parser.set("box", box.toJson());
		return parser;
	}
	
	@Override
	public boolean isValid() {
		return super.isValid() && platforms > 0;
	}

}