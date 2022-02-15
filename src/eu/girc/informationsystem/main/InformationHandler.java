package eu.girc.informationsystem.main;

import java.io.File;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

import eu.derzauberer.javautils.handler.FileHandler;
import eu.derzauberer.javautils.parser.JsonParser;
import eu.derzauberer.javautils.util.Console.MessageType;
import eu.girc.informationsystem.components.Line;
import eu.girc.informationsystem.components.Station;

public class InformationHandler {

	public static File file = new File("config.json");
	public static JsonParser parser = new JsonParser();
	
	private static ArrayList<Station> stations = new ArrayList<>();
	private static ArrayList<Line> lines = new ArrayList<>();
	
	public static void initialize() {
		String string = "";
		try {
			string = FileHandler.readString(file);
		} catch (UncheckedIOException exception) {
			InformationServer.getConsole().sendMessage("Can't access config.json, this file will be ignored!", MessageType.WARNING);
		}
		parser = new JsonParser(string);
		loadStations();
		loadLines();
	}
	
	public static void loadStations() {
		List<JsonParser> stations = parser.getJsonObjectList("stations");
		for (JsonParser parser : stations) {
			InformationHandler.stations.add(new Station(parser));
		}
	}
	
	public static void saveStations() {
		ArrayList<JsonParser> parser = new ArrayList<>();
		for (Station station : stations) {
			parser.add(station.toJson());
		}
		InformationHandler.parser.set("stations", parser);
		save();
	}
	
	public static void addStation(Station station) {
		Station oldStation = getStation(station.getName());
		if (oldStation != null) {
			oldStation = station;
		} else {
			stations.add(station);
		}
		saveStations();
	}
	
	public static void removeStation(Line line) {
		removeStation(line.getName());
	}
	
	public static void removeStation(String name) {
		Station oldStation = getStation(name);
		if (oldStation != null) {
			stations.remove(oldStation);
		}
		saveStations();
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
	
	public static void loadLines() {
		List<JsonParser> lines = parser.getJsonObjectList("lines");
		for (JsonParser parser : lines) {
			InformationHandler.lines.add(new Line(parser));
		}
	}
	
	public static void saveLines() {
		ArrayList<JsonParser> parser = new ArrayList<>();
		for (Line line : lines) {
			parser.add(line.toJson());
		}
		InformationHandler.parser.set("lines", parser);
		save();
	}
	
	public static void addLine(Line line) {
		Line oldLine = getLine(line.getName());
		if (oldLine != null) {
			oldLine = line;
		} else {
			lines.add(line);
		}
		saveLines();
	}
	
	public static void removeLine(Line line) {
		removeLine(line.getName());
	}
	
	public static void removeLine(String name) {
		Line oldLine = getLine(name);
		if (oldLine != null) {
			lines.remove(oldLine);
		}
		saveLines();
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
	
	private static void save() {
		try {
			FileHandler.writeString(file, parser.toString());
		} catch (Exception exception) {
			InformationServer.getConsole().sendMessage("Can't access config.json!", MessageType.ERROR);
		}
	}
	
}
