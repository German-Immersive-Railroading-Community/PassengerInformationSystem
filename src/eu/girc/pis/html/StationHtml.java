package eu.girc.pis.html;

import eu.girc.pis.components.Station;
import eu.girc.pis.resources.Resource;

public class StationHtml extends Html {

	private static String stationComponent = Resource.getTextFile("station-component.html");
	
	public StationHtml(Station station) {
		super("Stations", buildStationHtml(station));
	}

	public static String buildStationHtml(Station station) {
		String string = stationComponent;
		string = string.replace("{name}", station.getName());
		string = string.replace("{displayName}", station.getDisplayName());
		string = string.replace("{plattforms}", Integer.toString(station.getPlattforms()));
		string += new HtmlTag("h2", "Lines via station " + station.getDisplayName());
		string += LineListHtml.buildLineList(station);
		return string;
	}
	
}