package eu.girc.informationsystem.handler;

import java.io.File;
import java.io.UncheckedIOException;
import eu.derzauberer.javautils.handler.FileHandler;
import eu.derzauberer.javautils.parser.JsonParser;
import eu.derzauberer.javautils.util.Console.MessageType;
import eu.girc.informationsystem.components.Line;
import eu.girc.informationsystem.components.Station;
import eu.girc.informationsystem.main.InformationServer;
import eu.girc.informationsystem.util.EntityList;

public class InformationHandler {

	private static File file = new File("config.json");
	private static JsonParser parser = new JsonParser();
	
	private static EntityList<Station> stations = new EntityList<>();
	private static EntityList<Line> lines = new EntityList<>();
	
	static {
		try {
			parser = new JsonParser(FileHandler.readString(file));
		} catch (UncheckedIOException exception) {
			InformationServer.getConsole().sendMessage("Can't access config.json, this file will be ignored!", MessageType.WARNING);
		}
		stations.load("stations", parser, Station.class);
		lines.load("lines", parser, Line.class);
	}
	
	public static EntityList<Station> getStations() {
		return stations;
	}
	
	public static EntityList<Line> getLines() {
		return lines;
	}
	
	public static JsonParser getParser() {
		return parser;
	}
	
	public static void save() {
		stations.save("stations", parser);
		lines.save("lines", parser);
		try {
			FileHandler.writeString(file, parser.toString());
		} catch (Exception exception) {
			InformationServer.getConsole().sendMessage("Can't access config.json!", MessageType.ERROR);
		}
	}
	
}