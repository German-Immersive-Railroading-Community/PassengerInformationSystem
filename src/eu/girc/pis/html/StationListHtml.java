package eu.girc.pis.html;

import eu.girc.pis.components.Station;
import eu.girc.pis.main.Main;
import eu.girc.pis.resources.Resource;
import eu.girc.pis.util.EntityList;

public class StationListHtml extends Html {

	private static String stationPreview = Resource.getTextFile("station-preview.html");
	
	public StationListHtml() {
		super("Stations", buildStationList(Main.getStations()));
	}
	
	public static String buildStationList(EntityList<Station> stations) {
		if (stations.size() > 0) {
			String string = "";
			for (Station station : stations.sort()) {
				string += buildStationPreviewHtml(station);
			}
			return string;
		} else {
			return new HtmlTag("div", "No stations found!", "box").toString();
		}
	}
	
	public static String buildStationPreviewHtml(Station station) {
		String string = stationPreview;
		string = string.replace("{name}", station.getName());
		string = string.replace("{displayName}", station.getDisplayName());
		return string;
	}

}
