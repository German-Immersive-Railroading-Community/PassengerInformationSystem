package eu.girc.pis.components;

import java.util.ArrayList;
import java.util.List;
import eu.derzauberer.javautils.parser.JsonParser;
import eu.derzauberer.javautils.util.Time;
import eu.girc.pis.util.Entity;
import eu.girc.pis.util.EntityList;

public class Line extends Entity {
	
	private String type;
	private String operator;
	private String driver;
	private Time departure;
	private boolean cancelled;
	private int delay;
	private Box box;
	
	private EntityList<LineStation> stations;
	
	public Line(String name, String displayName, Time departure) {
		super(name, displayName);
		type = "Unknown";
		operator = "Unknown";
		driver = "Unknown";
		this.departure = departure;
		cancelled = false;
		delay = 0;
		stations = new EntityList<>();
	}
	
	public Line(JsonParser parser) {
		this("Unnamed", "Unnamed", new Time(0, 0));
		stations = new EntityList<>();
		fromJson(parser);
	}
	
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getOperator() {
		return operator;
	}
	
	public void setDriver(String driver) {
		this.driver = driver;
	}
	
	public String getDriver() {
		return driver;
	}
	
	public Time getDeparture() {
		if (departure == null) calculateDepartueTimes();
		return departure;
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
	
	public LineStation getLineStation(Station station) {
		for (LineStation lineStation : stations) {
			if (lineStation.getStation().getName().equals(station.getName())) {
				return lineStation;
			}
		}
		return null;
	}
	
	public EntityList<LineStation> getStations() {
		return stations;
	}
	
	public void setBox(Box box) {
		this.box = box;
	}
	
	public Box getBox() {
		return box;
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
		operator = parser.getString("type");
		operator = parser.getString("operator");
		driver = parser.getString("driver");
		departure = new Time(parser.getString("departure"), "hh:mm");
		cancelled =  parser.getBoolean("cancelled");
		delay =  parser.getInt("delay");
		List<JsonParser> stations = parser.getJsonObjectList("stations");
		for (JsonParser station : stations) {
			getStations().add(new LineStation(this, station));
		}
		if (parser.get("box") != null) box = new Box(parser.getJsonObject("box"));
	}
	
	@Override
	public JsonParser toJson() {
		JsonParser parser = new JsonParser();
		parser.set("name", getName());
		parser.set("displayName", getDisplayName());
		parser.set("type", type);
		parser.set("operator", operator);
		parser.set("driver", driver);
		parser.set("departure", departure.toString("hh:mm"));
		parser.set("cancelled", cancelled);
		parser.set("delay", delay);
		calculateDepartueTimes();
		ArrayList<JsonParser> stations = new ArrayList<>();
		for (LineStation station : this.stations) {
			stations.add(station.toJson());
		}
		parser.set("stations", stations);
		if (box != null) parser.set("box", box.toJson());
		return parser;
	}
	
	@Override
	public boolean isValid() {
		if (super.isValid()) {
			for (LineStation station : stations) {
				if (station.getStation() != null && station.getStation().isValid()) {
					if (station.getPlatform() > 0 && station.getPlatform() <= station.getStation().getPlatforms()) {
						return true;
					}
				}
			}
		}
		return false;
	}

}