package eu.girc.informationsystem.main;

import java.io.File;
import java.io.UncheckedIOException;
import eu.derzauberer.javautils.events.ConsoleInputEvent;
import eu.derzauberer.javautils.handler.FileHandler;
import eu.derzauberer.javautils.parser.JsonParser;
import eu.derzauberer.javautils.util.Console;
import eu.derzauberer.javautils.util.Console.MessageType;
import eu.girc.informationsystem.components.Line;
import eu.girc.informationsystem.components.LineStation;
import eu.girc.informationsystem.components.Station;
import eu.girc.informationsystem.handler.RequestHandler;
import eu.girc.informationsystem.requests.HelpRequest;
import eu.girc.informationsystem.requests.IndexRequest;
import eu.girc.informationsystem.requests.LineCallback;
import eu.girc.informationsystem.requests.LineRequest;
import eu.girc.informationsystem.requests.StationCallback;
import eu.girc.informationsystem.requests.StationRequest;
import eu.girc.informationsystem.requests.TemplateRequest;
import eu.girc.informationsystem.util.EntityList;
import eu.girc.informationsystem.util.Time;
import io.undertow.Undertow;

public class Main {
	
	private static Console console = new Console();
	private static File file = new File("config.json");
	private static JsonParser parser = new JsonParser();
	private static EntityList<Station> stations = new EntityList<>();
	private static EntityList<Line> lines = new EntityList<>();
	
	public static void main(String[] args) {
		if (!isStarted(args)) System.exit(-1); 
		console.setDefaultType(MessageType.INFO);
		console.sendMessage("Server is running on port {}!", args[0]);
		console.setOnInput(Main::onConsoleInput);
		initializeConfig();
		registerRequests();
		stationTest();
	}
	
	private static boolean isStarted(String args[]) {
		try {
			if (args.length > 0) {
				Undertow server = Undertow.builder().addHttpListener(Integer.parseInt(args[0]), "localhost").setHandler(RequestHandler::execute).build();
				server.start();
				return true;
			} else {
				console.sendMessage("Can't start server! Please use use 'java -jar <jarfile> <port>'", MessageType.ERROR);
				return false;
			}
		} catch (Exception exception) {
			console.sendMessage("Can't start server! Please use use 'java -jar <jarfile> <port>'", MessageType.ERROR);
			console.sendMessage(exception.getMessage(), MessageType.ERROR);
			return false;
		}
	}
	
	private static void registerRequests() {
		RequestHandler.setIndex(new IndexRequest());
		RequestHandler.registerRequest("help", new HelpRequest());
		RequestHandler.registerRequest("station", new StationRequest());
		RequestHandler.registerRequest("line", new LineRequest());
		RequestHandler.registerRequest("template", new TemplateRequest());
		RequestHandler.registerCallback("station", new StationCallback());
		RequestHandler.registerCallback("line", new LineCallback());
	}
	
	private static void stationTest() {
		Station roedauHbfStation = new Station("Roedau_Hbf", "Rödau Hbf", 8);
		Station roedauSuedStation = new Station("Roedau_Suedbahnhof", "Rödau Südbahnhof", 3);
		Main.getStations().add(roedauHbfStation);
		Main.getStations().add(roedauSuedStation);
		Line line = new Line("S5_Roedau_Sued", "S5 Rödau Süd", new Time(16, 5));
		line.setOperator("DIRC");
		line.setDriver("Der_Zauberer");
		line.getLineStations().add(new LineStation(roedauHbfStation, 1, 0));
		line.getLineStations().add(new LineStation(roedauSuedStation, 3, 3));
		line.calculateDepartueTimes();
		Main.getLines().add(line);
		Main.save();
	}
	
	private static void initializeConfig() {
		try {
			parser = new JsonParser(FileHandler.readString(file));
		} catch (UncheckedIOException exception) {
			Main.getConsole().sendMessage("Can't access config.json, this file will be ignored!", MessageType.WARNING);
		}
		stations.load("stations", parser, Station.class);
		lines.load("lines", parser, Line.class);
	}
	
	private static void onConsoleInput(ConsoleInputEvent event) {
		if (event.getInput().equalsIgnoreCase("exit")) {
			System.exit(0);
		}
	}
	
	public static Console getConsole() {
		return console;
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
			Main.getConsole().sendMessage("Can't access config.json!", MessageType.ERROR);
		}
	}

}