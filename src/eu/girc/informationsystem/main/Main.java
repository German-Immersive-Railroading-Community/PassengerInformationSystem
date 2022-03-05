package eu.girc.informationsystem.main;

import java.io.File;
import java.io.UncheckedIOException;
import eu.derzauberer.javautils.handler.CommandHandler;
import eu.derzauberer.javautils.handler.FileHandler;
import eu.derzauberer.javautils.parser.JsonParser;
import eu.derzauberer.javautils.util.Console;
import eu.derzauberer.javautils.util.Console.MessageType;
import eu.girc.informationsystem.commands.LineCommand;
import eu.girc.informationsystem.commands.StationCommand;
import eu.girc.informationsystem.commands.StopCommand;
import eu.girc.informationsystem.components.Line;
import eu.girc.informationsystem.components.Station;
import eu.girc.informationsystem.html.Error404Html;
import eu.girc.informationsystem.requests.APIIndexRequest;
import eu.girc.informationsystem.requests.APILineCallback;
import eu.girc.informationsystem.requests.APILineRequest;
import eu.girc.informationsystem.requests.ResourcesRequest;
import eu.girc.informationsystem.requests.SeachRequest;
import eu.girc.informationsystem.requests.StationRequest;
import eu.girc.informationsystem.requests.IndexRequest;
import eu.girc.informationsystem.requests.LineRequest;
import eu.girc.informationsystem.requests.APIStationCallback;
import eu.girc.informationsystem.requests.APIStationRequest;
import eu.girc.informationsystem.requests.APITemplateRequest;
import eu.girc.informationsystem.util.EntityList;
import io.undertow.Undertow;

public class Main {
	
	private static Console console = new Console();
	private static File file = new File("config.json");
	private static JsonParser parser = new JsonParser();
	private static EntityList<Station> stations = new EntityList<>();
	private static EntityList<Line> lines = new EntityList<>();
	
	public static void main(String[] args) {
		if (!isStarted(args)) System.exit(-1);
		console.sendMessage("Server is running on port {}!", MessageType.INFO, args[0]);
		initializeConfig();
		registerRequests();
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
		RequestHandler.registerRequest("search", new SeachRequest());
		RequestHandler.registerRequest("line", new LineRequest());
		RequestHandler.registerRequest("station", new StationRequest());
		RequestHandler.registerRequest("resources", new ResourcesRequest());
		RequestHandler.set404Html(new Error404Html());
		RequestHandler.setAPIIndex(new APIIndexRequest());
		RequestHandler.registerAPIRequest("station", new APIStationRequest());
		RequestHandler.registerAPIRequest("line", new APILineRequest());
		RequestHandler.registerAPIRequest("template", new APITemplateRequest());
		RequestHandler.registerAPICallback("station", new APIStationCallback());
		RequestHandler.registerAPICallback("line", new APILineCallback());
		CommandHandler.registerCommand("station", new StationCommand());
		CommandHandler.registerCommand("line", new LineCommand());
		CommandHandler.registerCommand("stop", new StopCommand());
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
	
	public static void stop() {
		save();
		System.exit(0);
	}

}
