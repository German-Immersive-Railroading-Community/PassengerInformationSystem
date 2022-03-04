package eu.girc.informationsystem.html;

import eu.girc.informationsystem.components.Station;
import eu.girc.informationsystem.main.Main;
import eu.girc.informationsystem.resources.Resource;
import eu.girc.informationsystem.util.EntityList;

public class StationListHtml extends Html {

	private static String linePreview = Resource.getTextFile("station-preview.html");
	
	public StationListHtml() {
		super("stations", buildStationList(Main.getStations()));
	}
	
	public static String buildStationList(EntityList<Station> stations) {
		String string = "";
		for (Station station : stations) {
			string += buildStationComponentHtml(station);
		}
		return string;
	}
	
	public static String buildStationComponentHtml(Station station) {
		String string = linePreview;
		string = string.replace("{name}", station.getName());
		string = string.replace("{displayName}", station.getDisplayName());
		return string;
	}

}
