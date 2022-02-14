package eu.girc.informationsystem.components;

import java.util.ArrayList;
import eu.derzauberer.javautils.annotations.JsonElement;
import eu.derzauberer.javautils.parser.JsonParser;

public class Station {
	
	@JsonElement
	private String name;
	
	@JsonElement
	private String displayName;
	
	@JsonElement
	private int plattforms;
	
	private static ArrayList<Station> stations = new ArrayList<>();
	
	public Station(String name, String displayName, int plattforms) {
		this.name = name;
		this.displayName = displayName;
		this.plattforms = plattforms;
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
	
	public int getPlattforms() {
		return plattforms;
	}
	
	public ArrayList<Line> getInformation() {
		ArrayList<Line> lines = new ArrayList<>();
		for (Line line : Line.getLines()) {
			if (line.containsStation(this)) {
				lines.add(line);
			}
		}
		return lines;
	}
	
	public static void addStation(Station station) {
		Station oldStation = getStation(station.getName());
		if (oldStation != null) {
			oldStation = station;
		} else {
			stations.add(station);
		}
	}
	
	public static void removeStation(Line line) {
		removeStation(line.getName());
	}
	
	public static void removeStation(String name) {
		Station oldStation = getStation(name);
		if (oldStation != null) {
			stations.remove(oldStation);
		}
	}
	
	public static Station getStation(String name) {
		for (Station station : stations) {
			if (station.getName().equalsIgnoreCase(name)) {
				return station;
			}
		}
		return null;
	}
	
	public static ArrayList<Station> getStations() {
		return stations;
	}
	
	@Override
	public String toString() {
		return new JsonParser(this).toString();
	}

}