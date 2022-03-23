package eu.girc.pis.commands;

import eu.derzauberer.javautils.util.Command;
import eu.derzauberer.javautils.util.Console;
import eu.girc.pis.main.Main;

public class StopCommand implements Command {

	@Override
	public boolean onCommand(Console console, String label, String[] args) {
		Main.stop();
		return true;
	}
	
	@Override
	public String getCommandHelp() {
		return "Stops the server and save data to the config file";
	}

}
