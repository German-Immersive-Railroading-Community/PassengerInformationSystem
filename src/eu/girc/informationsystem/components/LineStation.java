package eu.girc.informationsystem.components;

import eu.derzauberer.javautils.parser.JsonParser;
import eu.girc.informationsystem.util.InformationTime;

public class LineStation {
	
	private Station station;
	private int plattform;
	private String departure;
	private int travelTimeFromLastStation;

	public LineStation(Station station, int plattform, int travelTimeFromLastStation) {
		this.station = station;
		if (plattform <= station.getPlattforms() && plattform > 0) {
			this.plattform = plattform;
		} else {
			this.plattform = 0;
			throw new IllegalArgumentException("Station " + station.getName() + " only has " + station.getPlattforms() + " plattforms!");
		}
		this.travelTimeFromLastStation = travelTimeFromLastStation;
	}
	
	public LineStation(JsonParser parser) {
		this(new Station("Unnamed", "Unnamed", 1), 1, 0);
		fromJson(parser);
	}

	public Station getStation() {
		return station;
	}
	
	public int getPlattform() {
		return plattform;
	}
	
	protected void setDeparture(InformationTime time) {
		this.departure = time.toString();
	}
	
	public InformationTime getDeparture() {
		return new InformationTime(departure);
	}
	
	public int getTravelTimeFromLastStation() {
		return travelTimeFromLastStation;
	}
	
	public void fromJson(JsonParser parser) {
		station = new Station(parser.getJsonObject("station"));
		plattform = parser.getInt("plattform");
		travelTimeFromLastStation = parser.getInt("travelTimeFromLastStation");
	}
	
	public JsonParser toJson() {
		JsonParser parser = new JsonParser();
		parser.set("station", station.toJson());
		parser.set("plattform", getPlattform());
		parser.set("departure", departure);
		parser.set("travelTimeFromLastStation", getTravelTimeFromLastStation());
		return parser;
	}

}