package eu.girc.pis.commands;

import eu.derzauberer.javautils.util.Command;
import eu.derzauberer.javautils.util.Sender;
import eu.girc.pis.main.Main;

public class StopCommand implements Command {

	@Override
	public boolean onCommand(Sender sender, String label, String[] args) throws Exception {
		Main.stop();
		return true;
	}

}
