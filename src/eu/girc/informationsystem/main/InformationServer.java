package eu.girc.informationsystem.main;

import eu.derzauberer.javautils.events.ClientMessageReceiveEvent;
import eu.derzauberer.javautils.events.ConsoleInputEvent;
import eu.derzauberer.javautils.util.Console;
import eu.derzauberer.javautils.util.Console.MessageType;
import eu.derzauberer.javautils.util.Server;
import eu.girc.informationsystem.components.Line;
import eu.girc.informationsystem.components.LineStation;
import eu.girc.informationsystem.components.Station;
import eu.girc.informationsystem.util.InformationTime;

public class InformationServer {
	
	private static Server server;
	private static Console console = new Console();
	
	public static void main(String[] args) {
		if (isStarted(args)) {
			console.sendMessage("Server is running on port {}!", MessageType.INFO, args[0]);
			console.setOnInput(InformationServer::onConsoleInput);
			server.setOnMessageReceive(InformationServer::onClientMessageReceive);
			InformationHandler.initialize();
			stationTest();
		}
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
		Station roedauHbfStation = new Station("Roedau_Hbf", "R�dau Hbf", 8);
		Station roedauSuedStation = new Station("Roedau_Suedbahnhof", "R�dau S�dbahnhof", 3);
		InformationHandler.addStation(roedauHbfStation);
		InformationHandler.addStation(roedauSuedStation);
		Line line = new Line("S5_Roedau_Sued", "S5 R�dau S�d", new InformationTime(16, 5));
		line.getStations().add(new LineStation(roedauHbfStation, 1, 0));
		line.getStations().add(new LineStation(roedauSuedStation, 3, 3));
		line.calculateDepartueTimes();
		InformationHandler.addLine(line);
	}
	
	private static void onConsoleInput(ConsoleInputEvent event) {
		if (event.getInput().equalsIgnoreCase("exit")) {
			System.exit(0);
		}
	}
	
	private static void onClientMessageReceive(ClientMessageReceiveEvent event) {
		event.getClient().sendMessage(RequestHandler.sendResponse(event.getMessage()));
		event.getClient().close();
	}
	
	public static Server getServer() {
		return server;
	}
	
	public static Console getConsole() {
		return console;
	}

}