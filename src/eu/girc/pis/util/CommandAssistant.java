package eu.girc.pis.util;

import eu.derzauberer.javautils.util.Sender;
import eu.derzauberer.javautils.util.Time;

public class CommandAssistant {

	private Sender sender;
	private String args[];
	
	public CommandAssistant(Sender sender, String args[]) {
		this.sender = sender;
		this.args = args;
	}
	
	public boolean hasMinLenght(int lenght) {
		if (args.length >= lenght) {
			return true;
		} else {
			sender.sendMessage("Not enough arguments!");
			return false;
		}
	}
	
	public boolean isInteger(String integer) {
		try {
			Integer.parseInt(integer);
			return true;
		} catch (NumberFormatException exception) {
			sender.sendMessage("The argument {} is not an integer!", integer);
			return false;
		}
		
	}
	
	public boolean isTime(String time) {
		try {
			new Time(time, "hh:mm");
			return true;
		} catch (IllegalArgumentException exception) {
			sender.sendMessage("The argument {} is not a time!", time);
			return false;
		}
		
	}
	
	public boolean containsEntity(EntityList<?> entities, String entity, String type) {
		if (entities.contains(entity)) {
			return true;
		} else {
			sender.sendMessage("The {} {} does not exist!", type, entity);
			return false;
		}
	}
	
	public static String getStringList(EntityList<?> entities) {
		final StringBuilder string = new StringBuilder();
		for (Entity entity : entities) {
			string.append(entity.getName() + " (" + entity.getDisplayName() + "), ");
		}
		if (string.charAt(string.length() - 1) == ' ' && string.charAt(string.length() - 2) == ',') {
			string.delete(string.length() - 2, string.length() - 1);
		}
		return string.toString();
	}

}
