package eu.girc.informationsystem.html;

import eu.girc.informationsystem.components.Station;
import eu.girc.informationsystem.resources.Resource;

public class StationHtml extends Html {

	private static String stationComponent = Resource.getTextFile("station-component.html");
	
	public StationHtml(Station station) {
		super("stations", buildStationHtml(station));
	}

	public static String buildStationHtml(Station station) {
		String string = stationComponent;
		string = string.replace("{name}", station.getName());
		string = string.replace("{displayName}", station.getDisplayName());
		string = string.replace("{plattforms}", Integer.toString(station.getPlattforms()));
		string += new HeadlineHtml("Lines via station " + station.getDisplayName());
		string += LineListHtml.buildLineList(station.getLines(), station);
		return string;
	}
	
}
