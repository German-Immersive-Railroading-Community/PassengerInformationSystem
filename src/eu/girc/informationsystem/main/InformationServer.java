package eu.girc.informationsystem.main;

import eu.derzauberer.javautils.events.ConsoleInputEvent;
import eu.derzauberer.javautils.util.Console;
import eu.derzauberer.javautils.util.Console.MessageType;
import eu.girc.informationsystem.components.Line;
import eu.girc.informationsystem.components.LineStation;
import eu.girc.informationsystem.components.Station;
import eu.girc.informationsystem.handler.InformationHandler;
import eu.girc.informationsystem.handler.RequestHandler;
import eu.girc.informationsystem.util.Time;
import io.undertow.Undertow;
import io.undertow.server.HttpServerExchange;

public class InformationServer {
	
	private static Console console = new Console();
	
	public static void main(String[] args) {
		if (!isStarted(args)) System.exit(-1); 
		console.setDefaultType(MessageType.INFO);
		console.sendMessage("Server is running on port {}!", args[0]);
		console.setOnInput(InformationServer::onConsoleInput);
		stationTest();
	}
	
	private static boolean isStarted(String args[]) {
		try {
			if (args.length > 0) {
				Undertow server = Undertow.builder().addHttpListener(Integer.parseInt(args[0]), "localhost").setHandler(InformationServer::handleRequest).build();
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
		InformationHandler.getStations().add(roedauHbfStation);
		InformationHandler.getStations().add(roedauSuedStation);
		Line line = new Line("S5_Roedau_Sued", "S5 Rödau Süd", new Time(16, 5));
		line.getStations().add(new LineStation(roedauHbfStation, 1, 0));
		line.getStations().add(new LineStation(roedauSuedStation, 3, 3));
		line.calculateDepartueTimes();
		InformationHandler.getLines().add(line);
		InformationHandler.save();
	}
	
	private static void handleRequest(HttpServerExchange exchange) throws Exception {
		console.sendMessage("Request from {} for {} {}", exchange.getConnection().getPeerAddress().toString(), exchange.getRequestMethod().toString(), exchange.getRequestPath());
		String args[] = RequestHandler.getArgs(exchange.getRequestPath());
		if (exchange.getRequestMethod().toString().equals("GET")) {
			if (args.length == 0) {
				RequestHandler.sendJson(exchange, InformationHandler.getParser());
			} else if (RequestHandler.isPath(args, 0, "station") || RequestHandler.isPath(args, 0, "stations")) {
				if (args.length == 1) {
					RequestHandler.sendJson(exchange, InformationHandler.getStations().toJson());
				} else if (args.length == 2 && InformationHandler.getStations().get(args[1]) != null) {
					RequestHandler.sendJson(exchange, InformationHandler.getStations().get(args[1]).toJson());
				}
			} else if (RequestHandler.isPath(args, 0, "line") || RequestHandler.isPath(args, 0, "lines")) {
				if (args.length == 1) {
					RequestHandler.sendJson(exchange, InformationHandler.getLines().toJson());
				} else if (args.length == 2 && InformationHandler.getLines().get(args[1]) != null) {
					RequestHandler.sendJson(exchange, InformationHandler.getLines().get(args[1]).toJson());
				}
			}
		}
		RequestHandler.send404NotFound(exchange);
	}
	
	private static void onConsoleInput(ConsoleInputEvent event) {
		if (event.getInput().equalsIgnoreCase("exit")) {
			System.exit(0);
		}
	}
	
	public static Console getConsole() {
		return console;
	}

}