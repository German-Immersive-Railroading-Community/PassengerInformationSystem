package eu.girc.informationsystem.main;

import eu.derzauberer.javautils.annotations.EventListener;
import eu.derzauberer.javautils.events.ClientConnectEvent;
import eu.derzauberer.javautils.events.ClientDisconnectEvent;
import eu.derzauberer.javautils.events.ClientMessageRecieveEvent;
import eu.derzauberer.javautils.events.ConsoleInputEvent;
import eu.derzauberer.javautils.util.Console.MessageType;
import eu.derzauberer.javautils.util.Listener;

public class ServerListener implements Listener {
	
	@EventListener
	private static void onClientConnect(ClientConnectEvent event) {
		InformationServer.getConsole().sendMessage("Client connected!", MessageType.INFO);
	}
	
	@EventListener
	private static void onClientDisconnect(ClientDisconnectEvent event) {
		InformationServer.getConsole().sendMessage("Client disconnected!", MessageType.INFO);
	}
	
	@EventListener
	private static void onClientMessageRecieve(ClientMessageRecieveEvent event) {
		InformationServer.getConsole().sendMessage("Message recieved: {}", MessageType.INFO, event.getMessage());
		event.getClient().sendMessage("Hi");
	}
	
	@EventListener
	private static void onConsoleInput(ConsoleInputEvent event) {
		if (event.getInput().equals("exit")) {
			System.exit(0);
		}
	}

}