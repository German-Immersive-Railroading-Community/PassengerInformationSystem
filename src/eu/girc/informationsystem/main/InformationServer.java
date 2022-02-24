package eu.girc.informationsystem.main;

import eu.derzauberer.javautils.events.ClientMessageReceiveEvent;
import eu.derzauberer.javautils.events.ConsoleInputEvent;
import eu.derzauberer.javautils.util.Console;
import eu.derzauberer.javautils.util.Console.MessageType;
import eu.derzauberer.javautils.util.Server;
import eu.girc.informationsystem.components.Line;
import eu.girc.informationsystem.components.LineStation;
import eu.girc.informationsystem.components.Station;
import eu.girc.informationsystem.handler.InformationHandler;
import eu.girc.informationsystem.handler.RequestHandler;
import eu.girc.informationsystem.util.Time;

public class InformationServer {
	
	private static Server server;
	private static Console console = new Console();
	
	public static void main(String[] args) {
		if (!isStarted(args)) System.exit(-1); 
		console.setDefaultType(MessageType.INFO);
		console.sendMessage("Server is running on port {}!", args[0]);
		console.setOnInput(InformationServer::onConsoleInput);
		server.setOnMessageReceive(InformationServer::onClientMessageReceive);
		stationTest();
	}
	
	private static boolean isStarted(String args[]) {
		try {
			if (args.length > 0) {
				server = new Server(Integer.parseInt(args[0]));
				server.setClientTimeout(10000);
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
	
	private static void onConsoleInput(ConsoleInputEvent event) {
		if (event.getInput().equalsIgnoreCase("exit")) {
			System.exit(0);
		}
	}
	
	private static void onClientMessageReceive(ClientMessageReceiveEvent event) {
		event.getClient().sendMessage(RequestHandler.sendResponse(event.getMessage()));
		console.sendMessage("Request from " + event.getClient().getAdress() + " \"" + event.getMessage() + "\"");
		event.getClient().close();
	}
	
	public static Server getServer() {
		return server;
	}
	
	public static Console getConsole() {
		return console;
	}

}