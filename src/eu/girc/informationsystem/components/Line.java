package eu.girc.informationsystem.components;

import java.util.ArrayList;
import eu.derzauberer.javautils.annotations.JsonElement;
import eu.derzauberer.javautils.parser.JsonParser;

public class Line {
	
	@JsonElement
	private String name;
	
	@JsonElement
	private String displayName;
	
	@JsonElement
	private String startTime;
	
	@JsonElement
	private int delay;
	
	@JsonElement
	private ArrayList<LineStation> stations;
	
	private static ArrayList<Line> lines = new ArrayList<>();
	
	public Line(String name, String displayName, Time time) {
		this.name = name;
		this.displayName = displayName;
		stations = new ArrayList<>();
		this.startTime = time.toString();
		this.delay = 0;
		lines.add(this);
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
	
	public void calculateDepartueTimes() {
		Time time = getStartTime();
		for (LineStation station : stations) {
			station.setDeparture(time.addTime(0, station.getTravelTimeFromLastStation()));
		}
	}
	
	public Time getStartTime() {
		return new Time(startTime);
	}
	
	public int getDelay() {
		return delay;
	}
	
	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public boolean containsStation(Station station) {
		for (LineStation lineStation : stations) {
			if (lineStation.getStation() == station) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<LineStation> getStations() {
		return stations;
	}
	
	public static void addLine(Line line) {
		Line oldLine = getLine(line.getName());
		if (oldLine != null) {
			oldLine = line;
		} else {
			lines.add(line);
		}
	}
	
	public static void removeLine(Line line) {
		removeLine(line.getName());
	}
	
	public static void removeLine(String name) {
		Line oldLine = getLine(name);
		if (oldLine != null) {
			lines.remove(oldLine);
		}
	}
	
	public static Line getLine(String name) {
		for (Line line : lines) {
			if (line.getName().equalsIgnoreCase(name)) {
				return line;
			}
		}
		return null;
	}
	
	public static ArrayList<Line> getLines() {
		return lines;
	}
	
	@Override
	public String toString() {
		return new JsonParser(this).toString();
	}

}