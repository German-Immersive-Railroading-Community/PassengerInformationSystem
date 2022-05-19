package eu.girc.pis.commands;

import eu.derzauberer.javautils.util.Command;
import eu.derzauberer.javautils.util.Sender;
import eu.girc.pis.main.Main;

public class ReleadCommand implements Command {

	@Override
	public boolean onCommand(Sender sender, String string, String[] args) throws Exception {
		Main.initializeConfig();
		sender.sendMessage("Server reloaded!");
		return true;
	}

}
