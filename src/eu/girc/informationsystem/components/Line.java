package eu.girc.informationsystem.components;

import java.util.ArrayList;
import java.util.List;
import eu.derzauberer.javautils.parser.JsonParser;
import eu.girc.informationsystem.util.Entity;
import eu.girc.informationsystem.util.Time;

public class Line extends Entity {
	
	private String departure;
	private int delay;
	
	private ArrayList<LineStation> stations;
	
	public Line(String name, String displayName, Time departure) {
		super(name, displayName);
		this.departure = departure.toString();
		this.delay = 0;
		stations = new ArrayList<>();
	}
	
	public Line(JsonParser parser) {
		this("Unnamed", "Unnamed", new Time(0, 0));
		stations = new ArrayList<>();
		fromJson(parser);
	}
	
	public Time getDeparture() {
		return new Time(departure);
	}
	
	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public int getDelay() {
		return delay;
	}
	
	public ArrayList<LineStation> getStations() {
		return stations;
	}
	
	public void calculateDepartueTimes() {
		Time time = getDeparture();
		for (LineStation station : stations) {
			station.setDeparture(time.addTime(0, station.getTravelTimeFromLastStation()));
		}
	}
	
	@Override
	public void fromJson(JsonParser parser) {
		super.fromJson(parser);
		departure = parser.getString("departure");
		delay =  parser.getInt("delay");
		List<JsonParser> stations = parser.getJsonObjectList("stations");
		for (JsonParser station : stations) {
			getStations().add(new LineStation(station));
		}
	}
	
	@Override
	public JsonParser toJson() {
		JsonParser parser = new JsonParser();
		parser.set("name", getName());
		parser.set("displayName", getDisplayName());
		parser.set("departure", departure);
		parser.set("delay", getDelay());
		calculateDepartueTimes();
		ArrayList<JsonParser> stations = new ArrayList<>();
		for (LineStation station : this.stations) {
			stations.add(station.toJson());
		}
		parser.set("stations", stations);
		return parser;
	}

}