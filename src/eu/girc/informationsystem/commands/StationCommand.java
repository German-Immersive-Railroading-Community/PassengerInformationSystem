package eu.girc.informationsystem.commands;

import eu.derzauberer.javautils.util.Command;
import eu.derzauberer.javautils.util.Console;
import eu.girc.informationsystem.components.Station;
import eu.girc.informationsystem.main.Main;
import eu.girc.informationsystem.util.CommandAssistant;

public class StationCommand implements Command {

	@Override
	public boolean onCommand(Console console, String label, String[] args) {
		if (args.length == 0) {
			console.sendMessage(getCommandHelp());
		} else {
			if (args[0].equals("list")) {
				console.sendMessage(CommandAssistant.getStringList(Main.getStations()));
				return true;
			} else {
				CommandAssistant assistant = new CommandAssistant(console, args);
				if (assistant.hasMinLenght(2) && (args[1].equals("create") || assistant.containsEntity(Main.getStations(), args[0], "station"))) {
					switch (args[1]) {
					case "create": 
						if (assistant.hasMinLenght(4) && assistant.isInteger(args[3])) {
							Station station = new Station(args[0], args[2], Integer.parseInt(args[3]));
							Main.getStations().add(station);
							Main.save();
							console.sendMessage("Added station {}!", args[0]);
						}
						return true;
					case "remove": 
						Main.getStations().remove(args[0]);
						Main.save();
						console.sendMessage("Removed station {}!", args[0]);
						return true;
					case "info": 
						Station station = Main.getStations().get(args[0]);
						console.sendMessage("------------\n{} ({})\nPlattforms: {}\n------------", station.getName(), station.getDisplayName(), Integer.toString(station.getPlattforms()));
						return true;
					default: console.sendMessage("The option {} does not exist!", args[1]); return true;
					}
				}
			}
		}
		return true;
	}
	
	@Override
	public String getCommandHelp() {
		return "";
	}

}
