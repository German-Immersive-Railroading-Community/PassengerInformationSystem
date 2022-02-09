package eu.girc.informationsystem.main;

import eu.derzauberer.javautils.handler.EventHandler;
import eu.derzauberer.javautils.util.Console;
import eu.derzauberer.javautils.util.Console.MessageType;
import eu.derzauberer.javautils.util.Server;
import eu.girc.informationsystem.components.Line;
import eu.girc.informationsystem.components.LineStation;
import eu.girc.informationsystem.components.Station;

public class InformationServer {
	
	private static Server server;
	private static Console console = new Console();
	
	public static void main(String[] args) {
		try {
			if (args.length > 0) {
				server = new Server(Integer.parseInt(args[0]));
			} else {
				console.sendMessage("Can't start server! Please use use 'java -jar <jarfile> <port>'", MessageType.ERROR);
			}
		} catch (Exception exception) {
			console.sendMessage("Can't start server! Please use use 'java -jar <jarfile> <port>'", MessageType.ERROR);
			console.sendMessage(exception.getMessage(), MessageType.ERROR);
			System.exit(-2);
		}
		console.sendMessage("Server is running on port {}!", MessageType.INFO, args[0]);
		EventHandler.registerEvents(new ServerListener());
		stationTest();
	}
	
	private static void stationTest() {
		Station roedauHbfStation = new Station("Rödau Hbf", 8);
		Station roedauSuedStation = new Station("Rödau Südbahnhof", 3);
		Line line = new Line("S5 Rödau Süd");
		line.getStations().add(new LineStation(roedauHbfStation, 1, 5));
		line.getStations().add(new LineStation(roedauSuedStation, 3, 0));
		System.out.println(line);
	}
	
	public static Server getServer() {
		return server;
	}
	
	public static Console getConsole() {
		return console;
	}

}