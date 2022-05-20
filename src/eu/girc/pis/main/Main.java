package eu.girc.pis.main;

import java.io.File;
import java.io.UncheckedIOException;
import eu.derzauberer.javautils.handler.CommandHandler;
import eu.derzauberer.javautils.parser.JsonParser;
import eu.derzauberer.javautils.util.Console;
import eu.derzauberer.javautils.util.FileUtil;
import eu.derzauberer.javautils.util.Sender.MessageType;
import eu.girc.pis.commands.LineCommand;
import eu.girc.pis.commands.ReleadCommand;
import eu.girc.pis.commands.StationCommand;
import eu.girc.pis.commands.StopCommand;
import eu.girc.pis.components.Line;
import eu.girc.pis.components.Station;
import eu.girc.pis.requests.APIIndexRequest;
import eu.girc.pis.requests.APILineCallback;
import eu.girc.pis.requests.APILineRequest;
import eu.girc.pis.requests.APIStationCallback;
import eu.girc.pis.requests.APIStationRequest;
import eu.girc.pis.requests.APITemplateRequest;
import eu.girc.pis.requests.IndexRequest;
import eu.girc.pis.requests.LineRequest;
import eu.girc.pis.requests.ResourcesRequest;
import eu.girc.pis.requests.SeachRequest;
import eu.girc.pis.requests.StationRequest;
import eu.girc.pis.util.EntityList;
import eu.girc.pis.util.Html;
import io.undertow.Undertow;

public class Main {
	
	private static CommandHandler commands = new CommandHandler();
	private static RequestHandler requests = new RequestHandler();
	private static Console console = new Console(commands);
	private static File file = new File("config.json");
	private static JsonParser parser = new JsonParser();
	private static EntityList<Station> stations = new EntityList<>();
	private static EntityList<Line> lines = new EntityList<>();
	
	public static void main(String[] args) {
		if (!isStarted(args)) System.exit(-1);
		console.sendMessage(MessageType.INFO, "Server is running on port {}!", args[0]);
		initializeConfig();
		registerRequests();
	}
	
	private static boolean isStarted(String args[]) {
		try {
			if (args.length > 0) {
				Undertow server = Undertow.builder().addHttpListener(Integer.parseInt(args[0]), "0.0.0.0").setHandler(requests::execute).build();
				server.start();
				return true;
			} else {
				console.sendMessage(MessageType.ERROR, "Can't start server! Please use use 'java -jar <jarfile> <port>'");
				return false;
			}
		} catch (Exception exception) {
			console.sendMessage(MessageType.ERROR, "Can't start server! Please use use 'java -jar <jarfile> <port>'");
			console.sendMessage(MessageType.ERROR, exception.getMessage());
			return false;
		}
	}
	
	private static void registerRequests() {
		requests.setIndex(new IndexRequest());
		requests.registerRequest("/search", new SeachRequest());
		requests.registerRequest("/line", new LineRequest());
		requests.registerRequest("/station", new StationRequest());
		requests.registerRequest("/resources", new ResourcesRequest());
		requests.registerRequest("/api", new APIIndexRequest());
		requests.registerRequest("/api/station", new APIStationRequest());
		requests.registerRequest("/api/line", new APILineRequest());
		requests.registerRequest("/api/template", new APITemplateRequest());
		requests.registerCallback("/api/station", new APIStationCallback());
		requests.registerCallback("/api/line", new APILineCallback());
		requests.set404Html(Html.build404Html());
		commands.registerCommand("line", new LineCommand());
		commands.registerCommand("reload", new ReleadCommand());
		commands.registerCommand("station", new StationCommand());
		commands.registerCommand("stop", new StopCommand());
	}
	
	public static void initializeConfig() {
		try {
			parser = new JsonParser(FileUtil.readString(file));
		} catch (UncheckedIOException exception) {
			Main.getConsole().sendMessage(MessageType.WARNING, "Can't access config.json, this file will be ignored!");
		}
		stations.load("stations", parser, Station.class);
		lines.load("lines", parser, Line.class);
	}
	
	public static Console getConsole() {
		return console;
	}
	
	public static CommandHandler getCommands() {
		return commands;
	}
	
	public static RequestHandler getRequests() {
		return requests;
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
		stations.sort().save("stations", parser);
		lines.sort().save("lines", parser);
		try {
			FileUtil.writeString(file, parser.toString());
		} catch (Exception exception) {
			Main.getConsole().sendMessage(MessageType.ERROR, "Can't access config.json!");
		}
	}
	
	public static void stop() {
		save();
		System.exit(0);
	}

}
