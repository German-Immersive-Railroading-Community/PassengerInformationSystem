package eu.girc.pis.commands;

import eu.derzauberer.javautils.util.Command;
import eu.derzauberer.javautils.util.Sender;
import eu.girc.pis.components.Station;
import eu.girc.pis.main.Main;
import eu.girc.pis.util.CommandAssistant;

public class StationCommand implements Command {

	@Override
	public boolean onCommand(Sender sender, String label, String[] args) throws Exception {
		if (args.length == 0) {
			sender.sendMessage(getCommandHelp());
		} else {
			if (args[0].equals("list")) {
				sender.sendMessage(CommandAssistant.getStringList(Main.getStations()));
				return true;
			} else {
				CommandAssistant assistant = new CommandAssistant(sender, args);
				if (assistant.hasMinLenght(2) && (args[1].equals("create") || assistant.containsEntity(Main.getStations(), args[0], "station"))) {
					switch (args[1]) {
					case "create": 
						if (assistant.hasMinLenght(4) && assistant.isInteger(args[3])) {
							Station station = new Station(args[0], args[2], Integer.parseInt(args[3]));
							Main.getStations().add(station);
							Main.save();
							sender.sendMessage("Added station {}!", args[0]);
						}
						return true;
					case "remove": 
						Main.getStations().remove(args[0]);
						Main.save();
						sender.sendMessage("Removed station {}!", args[0]);
						return true;
					case "info": 
						Station station = Main.getStations().get(args[0]);
						sender.sendMessage("------------\n{} ({})\nPlattforms: {}\n------------", station.getName(), station.getDisplayName(), Integer.toString(station.getPlattforms()));
						return true;
					default: sender.sendMessage("The option {} does not exist!", args[1]); return true;
					}
				}
			}
		}
		return true;
	}
	
	public String getCommandHelp() {
		return "";
	}

}
