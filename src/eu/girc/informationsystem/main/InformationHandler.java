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
import eu.girc.informationsystem.util.InformationList;

public class InformationHandler {

	public static File file = new File("config.json");
	public static JsonParser parser = new JsonParser();
	
	private static InformationList<Station> stations = new InformationList<>();
	private static InformationList<Line> lines = new InformationList<>();
	
	public static void initialize() {
		try {
			parser = new JsonParser(FileHandler.readString(file));
		} catch (UncheckedIOException exception) {
			InformationServer.getConsole().sendMessage("Can't access config.json, this file will be ignored!", MessageType.WARNING);
		}
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
		for (Station station : stations.getEntities()) {
			parser.add(station.toJson());
		}
		InformationHandler.parser.set("stations", parser);
		save();
	}
	
	public static InformationList<Station> getStations() {
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
		for (Line line : lines.getEntities()) {
			parser.add(line.toJson());
		}
		InformationHandler.parser.set("lines", parser);
		save();
	}
	
	public static InformationList<Line> getLines() {
		return lines;
	}
	
	public static JsonParser getParser() {
		return parser;
	}
	
	private static void save() {
		try {
			FileHandler.writeString(file, parser.toString());
		} catch (Exception exception) {
			InformationServer.getConsole().sendMessage("Can't access config.json!", MessageType.ERROR);
		}
	}
	
}
