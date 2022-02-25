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
import eu.girc.informationsystem.util.EntityList;
import eu.girc.informationsystem.util.Time;
import io.undertow.Undertow;
import io.undertow.server.HttpServerExchange;

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
		stationTest();
	}
	
	private static boolean isStarted(String args[]) {
		try {
			if (args.length > 0) {
				Undertow server = Undertow.builder().addHttpListener(Integer.parseInt(args[0]), "localhost").setHandler(Main::handleRequest).build();
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
	
	private static void handleRequest(HttpServerExchange exchange) throws Exception {
		console.sendMessage("Request from {} for {} {}", exchange.getConnection().getPeerAddress().toString(), exchange.getRequestMethod().toString(), exchange.getRequestPath());
		String args[] = RequestHandler.getArgs(exchange.getRequestPath());
		if (exchange.getRequestMethod().toString().equals("GET")) {
			if (args.length == 0) {
				RequestHandler.sendJson(exchange, Main.getParser());
			} else if (RequestHandler.isPath(args, 0, "station") || RequestHandler.isPath(args, 0, "stations")) {
				if (args.length == 1) {
					RequestHandler.sendJson(exchange, Main.getStations().toJson());
				} else if (args.length >= 2 && Main.getStations().get(args[1]) != null) {
					if (args.length == 2) {
						RequestHandler.sendJson(exchange, Main.getStations().get(args[1]).toJson());
					} else if (args.length == 3 && RequestHandler.isPath(args, 2, "lines")) {
						RequestHandler.sendJson(exchange, Main.getStations().get(args[1]).getLines().toJson());
					}
				}
			} else if (RequestHandler.isPath(args, 0, "line") || RequestHandler.isPath(args, 0, "lines")) {
				if (args.length == 1) {
					RequestHandler.sendJson(exchange, Main.getLines().toJson());
				} else if (args.length == 2 && Main.getLines().get(args[1]) != null) {
					RequestHandler.sendJson(exchange, Main.getLines().get(args[1]).toJson());
				}
			}
		}
		RequestHandler.send404NotFound(exchange);
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