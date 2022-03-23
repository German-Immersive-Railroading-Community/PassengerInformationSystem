package eu.girc.pis.commands;

import eu.derzauberer.javautils.util.Command;
import eu.derzauberer.javautils.util.Console;
import eu.girc.pis.components.Line;
import eu.girc.pis.main.Main;
import eu.girc.pis.util.CommandAssistant;
import eu.girc.pis.util.Time;

public class LineCommand implements Command {

	@Override
	public boolean onCommand(Console console, String label, String[] args) {
		if (args.length == 0) {
			console.sendMessage(getCommandHelp());
		} else {
			if (args[0].equals("list")) {
				console.sendMessage(CommandAssistant.getStringList(Main.getLines()));
				return true;
			} else {
				CommandAssistant assistant = new CommandAssistant(console, args);
				if (assistant.hasMinLenght(2) && (args[1].equals("create") || assistant.containsEntity(Main.getLines(), args[0], "line"))) {
					switch (args[1]) {
					case "create": 
						if (assistant.hasMinLenght(4) && assistant.isTime(args[3])) {
							Line line = new Line(args[0], args[2], new Time(args[3]));
							Main.getLines().add(line);
							Main.save();
							console.sendMessage("Added line {}!", args[0]);
						}
						return true;
					case "remove": 
						Main.getLines().remove(args[0]);
						Main.save();
						console.sendMessage("Removed line {}!", args[0]);
						return true;
					case "info": 
						Line line = Main.getLines().get(args[0]);
						String info = "";
						info += "------------\n";
						info += line.getName() + " (" + line.getDisplayName() + ")\n";
						info += "Type: Unknown\n";
						info += "Operator: " + line.getOperator() + "\n";
						info += "Driver: " + line.getDriver() + "\n";
						info += "Departure: " + line.getDeparture() + "\n";
						info += "Delay: " + line.getDelay() + "\n";
						info += "------------";
						console.sendMessage(info);
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
