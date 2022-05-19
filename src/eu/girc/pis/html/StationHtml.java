package eu.girc.pis.html;

import eu.girc.pis.components.Station;
import eu.girc.pis.resources.Resource;
import eu.girc.pis.util.EntityList;

public class StationHtml {

	private static String stationPreview = Resource.getTextFile("station-preview.html");
	private static String stationComponent = Resource.getTextFile("station-component.html");
	

	public static String buildStation(Station station) {
		String string = stationComponent;
		if (station.getBox() != null) string = station.getBox().toHtml() + "\b" + string;
		string = string.replace("{name}", station.getName());
		string = string.replace("{displayName}", station.getDisplayName());
		string = string.replace("{plattforms}", Integer.toString(station.getPlatforms()));
		string += new HtmlTag("h2", "Lines via station " + station.getDisplayName());
		string += LineHtml.buildLineList(station);
		return Html.buildHtml("stations", string, true);
	}
	
	public static String buildStationList(EntityList<Station> stations) {
		if (stations.size() > 0) {
			String string = "";
			for (Station station : stations.sort()) {
				string += buildStationPreview(station);
			}
			return Html.buildHtml("stations", string, true);
		} else {
			return new HtmlTag("div", "No stations found!", "box").toString();
		}
	}
	
	public static String buildStationPreview(Station station) {
		String string = stationPreview;
		string = string.replace("{name}", station.getName());
		string = string.replace("{displayName}", station.getDisplayName());
		return string;
	}
	
}
