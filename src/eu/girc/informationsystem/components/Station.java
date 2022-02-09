package eu.girc.informationsystem.components;

import java.util.ArrayList;
import eu.derzauberer.javautils.annotations.JsonElement;
import eu.derzauberer.javautils.parser.JsonParser;

public class Station {
	
	@JsonElement
	private String name;
	
	@JsonElement
	private int plattforms;
	
	private static ArrayList<Station> stations = new ArrayList<>();
	
	public Station(String name, int plattforms) {
		this.name = name;
		this.plattforms = plattforms;
		stations.add(this);
	}
	
	public String getName() {
		return name;
	}
	
	public int getPlattforms() {
		return plattforms;
	}
	
	public ArrayList<Line> getInformation() {
		ArrayList<Line> lines = new ArrayList<>();
		for (Line line : Line.getLines()) {
			if (line.containStation(this)) {
				lines.add(line);
			}
		}
		return lines;
	}
	
	public static Station getStation(String name) {
		for (Station station : stations) {
			if (station.getName().equals(name)) {
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
		JsonParser parser = new JsonParser();
		parser.setClassAsJsonObject("", this);
		return parser.toString();
	}

}