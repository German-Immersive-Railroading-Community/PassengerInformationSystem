package eu.girc.informationsystem.html;

import eu.girc.informationsystem.components.Station;
import eu.girc.informationsystem.main.Main;
import eu.girc.informationsystem.resources.Resource;
import eu.girc.informationsystem.util.EntityList;

public class StationListHtml extends Html {

	private static String stationPreview = Resource.getTextFile("station-preview.html");
	
	public StationListHtml() {
		super("stations", buildStationList(Main.getStations()));
	}
	
	public static String buildStationList(EntityList<Station> stations) {
		if (stations.size() > 0) {
			String string = "";
			for (Station station : stations.alphabeticalSort()) {
				string += buildStationPreviewHtml(station);
			}
			return string;
		} else {
			return new BoxHtml("No stations found!").toString();
		}
	}
	
	public static String buildStationPreviewHtml(Station station) {
		String string = stationPreview;
		string = string.replace("{name}", station.getName());
		string = string.replace("{displayName}", station.getDisplayName());
		return string;
	}

}
