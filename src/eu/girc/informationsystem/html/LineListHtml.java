package eu.girc.informationsystem.html;

import eu.girc.informationsystem.components.Line;
import eu.girc.informationsystem.components.Station;
import eu.girc.informationsystem.main.Main;
import eu.girc.informationsystem.resources.Resource;
import eu.girc.informationsystem.util.EntityList;

public class LineListHtml extends Html {
	
	private static String linePreview = Resource.getTextFile("line-preview.html");
	
	public LineListHtml() {
		super("lines", buildLineList());
	}
	
	public static String buildLineList() {
		String string = "";
		for (Line line : Main.getLines()) {
			string += buildLineComponentHtml(line);
		}
		return string;
	}

	public static String buildLineComponentHtml(Line line) {
		String string = linePreview;
		string = string.replace("{name}", line.getName());
		string = string.replace("{displayName}", line.getDisplayName());
		string = string.replace("{departure}", line.getDeparture().toString());
		string = string.replace("{delay}", LineHtml.buildDelay(line.getDelay()));
		if (line.getStations().getFirst() != null) string = string.replace("{fistStation}", line.getStations().getFirst().getDisplayName());
		string = string.replace("{stations}", buildStationListHtml(line.getStations()));
		return string;
	}
	
	public static String buildStationListHtml(EntityList<Station> stations) {
		String string = "";
		for (int i = 0; i < stations.size() - 1; i++) {
			string += stations.get(i).getDisplayName() + " - ";
		}
		string += stations.getLast().getDisplayName();
		return string;
	}
	
}
