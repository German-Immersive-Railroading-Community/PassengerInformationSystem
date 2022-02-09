package eu.girc.informationsystem.components;

import java.util.ArrayList;
import eu.derzauberer.javautils.annotations.JsonElement;
import eu.derzauberer.javautils.parser.JsonParser;

public class Line {
	
	@JsonElement
	private String name;
	
	@JsonElement
	private ArrayList<LineStation> stations;
	
	@JsonElement
	private int delay;
	
	private static ArrayList<Line> lines = new ArrayList<>();
	
	public Line(String name) {
		this.name = name;
		stations = new ArrayList<>();
		this.delay = 0;
		lines.add(this);
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<LineStation> getStations() {
		return stations;
	}
	
	public boolean containStation(Station station) {
		for (LineStation lineStation : stations) {
			if (lineStation.getStation() == station) {
				return true;
			}
		}
		return false;
	}
	
	public int getDelay() {
		return delay;
	}
	
	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public static Line getLine(String name) {
		for (Line line : lines) {
			if (line.getName().equals(name)) {
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
		JsonParser parser = new JsonParser();
		parser.setClassAsJsonObject("", this);
		return parser.toString();
	}

}