package eu.girc.pis.components;

import eu.derzauberer.javautils.parser.JsonParser;
import eu.derzauberer.javautils.util.Time;
import eu.girc.pis.main.Main;

public class LineStation extends Station {
	
	private int platform;
	private Time departure;
	private int travelTimeFromLastStation;
	private boolean cancelled;
	private int delay;
	private int changedPlatform;
	private boolean passed;
	
	private Line line;

	public LineStation(Line line, Station station, int platform, int travelTimeFromLastStation) {
		super(station.getName(), station.getDisplayName(), station.getPlatforms());
		this.line = line;
		if (platform <= station.getPlatforms() && platform > 0) {
			this.platform = platform;
		} else {
			this.platform = 0;
			throw new IllegalArgumentException("Station " + station.getName() + " only has " + station.getPlatforms() + " plattforms!");
		}
		this.travelTimeFromLastStation = travelTimeFromLastStation;
		this.cancelled = false;
		this.delay = 0;
		this.changedPlatform = 0;
		this.passed = false;
	}
	
	public LineStation(Line line, JsonParser parser) {
		this(line, getStationFromJson(parser), 1, 0);
		fromJson(parser);
	}

	public Station getStation() {
		if (Main.getStations().contains(getName())) {
			return Main.getStations().get(getName());
		}
		return new Station(getName(), getDisplayName(), getPlatforms());
	}
	
	public int getPlatform() {
		return platform;
	}
	
	protected void setDeparture(Time time) {
		this.departure = time;
	}
	
	public Time getDeparture() {
		if (departure == null) line.calculateDepartueTimes();
		return departure;
	}
	
	public int getTravelTimeFromLastStation() {
		return travelTimeFromLastStation;
	}
	
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public int getDelay() {
		return delay;
	}
	
	public void setChangedPlatform(int changedPlattform) {
		this.changedPlatform = changedPlattform;
	}
	
	public int getChangedPlatform() {
		return changedPlatform;
	}
	
	public void setPassed(boolean passed) {
		this.passed = passed;
	}
	
	public boolean hasPassed() {
		return passed;
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
		platform = parser.getInt("platform");
		travelTimeFromLastStation = parser.getInt("travelTimeFromLastStation");
		cancelled = parser.getBoolean("cancelled");
		delay = parser.getInt("delay");
		changedPlatform = parser.getInt("changedPlatform");
		passed = parser.getBoolean("passed");
	}
	
	public JsonParser toJson() {
		JsonParser parser = new JsonParser();
		parser.set("station", getStation().toJson());
		parser.set("platform", getPlatform());
		parser.set("departure", departure.toString("hh:mm"));
		parser.set("travelTimeFromLastStation", getTravelTimeFromLastStation());
		parser.set("cancelled", cancelled);
		parser.set("delay", delay);
		parser.set("changedPlatform", changedPlatform);
		parser.set("passed", passed);
		return parser;
	}

}