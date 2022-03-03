package eu.girc.informationsystem.util;

import eu.derzauberer.javautils.util.Console;

public class CommandAssistant {

	private Console console;
	private String args[];
	
	public CommandAssistant(Console console, String args[]) {
		this.console = console;
		this.args = args;
	}
	
	public boolean hasMinLenght(int lenght) {
		if (args.length >= lenght) {
			return true;
		} else {
			console.sendMessage("Not enough arguments!");
			return false;
		}
	}
	
	public boolean isInteger(String integer) {
		try {
			Integer.parseInt(integer);
			return true;
		} catch (NumberFormatException exception) {
			console.sendMessage("The argument {} is not an integer!", integer);
			return false;
		}
		
	}
	
	public boolean isTime(String time) {
		try {
			new Time(time);
			return true;
		} catch (IllegalArgumentException exception) {
			console.sendMessage("The argument {} is not a time!", time);
			return false;
		}
		
	}
	
	public boolean containsEntity(EntityList<?> entities, String entity, String type) {
		if (entities.contains(entity)) {
			return true;
		} else {
			console.sendMessage("The {} {} does not exist!", type, entity);
			return false;
		}
	}
	
	public static String getStringList(EntityList<?> entities) {
		StringBuilder string = new StringBuilder();
		for (Entity entity : entities) {
			string.append(entity.getName() + " (" + entity.getDisplayName() + "), ");
		}
		if (string.charAt(string.length() - 1) == ' ' && string.charAt(string.length() - 2) == ',') {
			string.delete(string.length() - 2, string.length() - 1);
		}
		return string.toString();
	}

}
