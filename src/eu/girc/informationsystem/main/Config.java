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
import eu.girc.informationsystem.components.Time;

public class Config {

	public static File file = new File("config.json");
	public static JsonParser parser = new JsonParser();
	
	public static void initialize() {
		String string = "";
		try {
			string = FileHandler.readString(file);
		} catch (UncheckedIOException exception) {
			InformationServer.getConsole().sendMessage("Can't access config.json, this file will be ignored!", MessageType.WARNING);
		}
		parser = new JsonParser(string);
	}
	
	public static void saveStations() {
		List<JsonParser> stations = new ArrayList<>();
		Station.getStations().forEach(station -> stations.add(new JsonParser(station)));
		parser.set("stations", stations);
		save();
	}
	
	public static void loadStations() {
		for (JsonParser object : parser.getJsonObjectList("stations")) {
			Station station = new Station("", "", 0);
			object.getClassAsJsonObject("", station);
			Station.addStation(station);
		}
	}
	
	public static void saveLines() {
		List<JsonParser> lines = new ArrayList<>();
		Line.getLines().forEach(station -> lines.add(new JsonParser(station)));
		parser.set("lines", lines);
		save();
	}
	
	public static void loadLines() {
		Line.getLines().clear();
		for (JsonParser object : parser.getJsonObjectList("lines")) {
			Line line = new Line("", "", new Time(0, 0));
			object.getClassAsJsonObject("", line);
			Line.addLine(line);
		}
	}
	
	private static void save() {
		try {
			FileHandler.writeString(file, parser.toString());
		} catch (Exception exception) {
			InformationServer.getConsole().sendMessage("Can't access config.json!", MessageType.ERROR);
		}
	}
	
}
