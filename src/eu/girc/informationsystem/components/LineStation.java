package eu.girc.informationsystem.components;

import eu.derzauberer.javautils.parser.JsonParser;
import eu.girc.informationsystem.main.Main;
import eu.girc.informationsystem.util.Time;

public class LineStation extends Station {
	
	private int plattform;
	private String departure;
	private int travelTimeFromLastStation;

	public LineStation(Station station, int plattform, int travelTimeFromLastStation) {
		super(station.getName(), station.getDisplayName(), station.getPlattforms());
		if (plattform <= station.getPlattforms() && plattform > 0) {
			this.plattform = plattform;
		} else {
			this.plattform = 0;
			throw new IllegalArgumentException("Station " + station.getName() + " only has " + station.getPlattforms() + " plattforms!");
		}
		this.travelTimeFromLastStation = travelTimeFromLastStation;
	}
	
	public LineStation(JsonParser parser) {
		this(getStationFromJson(parser), 1, 0);
		fromJson(parser);
	}

	public Station getStation() {
		if (Main.getStations().contains(getName())) {
			return Main.getStations().get(getName());
		}
		return new Station(getName(), getDisplayName(), getPlattforms());
	}
	
	public int getPlattform() {
		return plattform;
	}
	
	protected void setDeparture(Time time) {
		this.departure = time.toString();
	}
	
	public Time getDeparture() {
		return new Time(departure);
	}
	
	public int getTravelTimeFromLastStation() {
		return travelTimeFromLastStation;
	}
	
	private static Station getStationFromJson(JsonParser parser) {
		if (parser.get("station") instanceof String) {
			return Main.getStations().get(parser.getString("station"));
		} else {
			if (Main.getStations().contains(parser.getString("station.name"))) {
				return Main.getStations().get(parser.getString("station.name"));
			} else {
				Station station = new Station(parser.getJsonObject("station"));
				Main.getStations().add(station);
				Main.save();
				return station;
			}
		}
	}
	
	public void fromJson(JsonParser parser) {
		plattform = parser.getInt("plattform");
		travelTimeFromLastStation = parser.getInt("travelTimeFromLastStation");
	}
	
	public JsonParser toJson() {
		JsonParser parser = new JsonParser();
		parser.set("station", getStation().toJson());
		parser.set("plattform", getPlattform());
		parser.set("departure", departure);
		parser.set("travelTimeFromLastStation", getTravelTimeFromLastStation());
		return parser;
	}

}