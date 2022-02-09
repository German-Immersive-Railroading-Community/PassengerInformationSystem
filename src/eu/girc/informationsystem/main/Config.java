package eu.girc.informationsystem.main;

import java.io.File;
import java.io.UncheckedIOException;
import eu.derzauberer.javautils.handler.FileHandler;
import eu.derzauberer.javautils.parser.JsonParser;
import eu.derzauberer.javautils.util.Console.MessageType;

public class Config {

	public static File file = new File("config.json");
	public static JsonParser parser = new JsonParser();
	
	public static void initialize() {
		String string = "";
		try {
			string = FileHandler.readString(file);
		} catch (UncheckedIOException exception) {
			InformationServer.getConsole().sendMessage("Can't access config.json, this file will be ignored!", MessageType.WARNING);
		}
		parser = new JsonParser(string);
	}
	
	private void save() {
		try {
			FileHandler.writeString(file, parser.toString());
		} catch (Exception exception) {
			InformationServer.getConsole().sendMessage("Can't access config.json!", MessageType.ERROR);
		}
	}
	
}
