package eu.girc.informationsystem.commands;

import eu.derzauberer.javautils.util.Command;
import eu.derzauberer.javautils.util.Console;
import eu.derzauberer.javautils.util.Console.MessageType;
import eu.girc.informationsystem.main.Main;

public class SaveCommand implements Command {

	@Override
	public boolean onCommand(Console console, String label, String[] args) {
		Main.save();
		console.sendMessage("Saved changes to cofig file!", MessageType.SUCCESS);
		return true;
	}
	
	@Override
	public String getCommandHelp() {
		return "Save data to config file";
	}
	
}
