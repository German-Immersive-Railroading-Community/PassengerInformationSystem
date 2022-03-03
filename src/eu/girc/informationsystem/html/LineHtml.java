package eu.girc.informationsystem.html;

import eu.girc.informationsystem.components.Line;
import eu.girc.informationsystem.components.LineStation;
import eu.girc.informationsystem.resources.Resource;
import eu.girc.informationsystem.util.EntityList;

public class LineHtml extends Html {
	
	private static String lineComponent = Resource.getTextFile("line-component.html");
	private static String stationComponent = Resource.getTextFile("line-station-component.html");

	public LineHtml(Line line) {
		super("lines", buildLineHtml(line));
	}
	
	public static String buildLineHtml(Line line) {
		String string = lineComponent;
		string = string.replace("{name}", line.getName());
		string = string.replace("{displayName}", line.getDisplayName());
		string = string.replace("{departure}", line.getDeparture().toString());
		string = string.replace("{type}", "Unknown");
		string = string.replace("{operator}", line.getOperator().toString());
		string = string.replace("{driver}", line.getDriver().toString());
		line.calculateDepartueTimes();
		if (line.getStations().getFirst() != null) string = string.replace("{firstStation}", line.getStations().getFirst().getDisplayName());
		if (line.getStations().getLast() != null) {
			string = string.replace("{arrival}", line.getLineStations().get(line.getLineStations().size() - 1).getDeparture().toString());
			string = string.replace("{lastStation}", line.getStations().getLast().getDisplayName());
		}
		string = string.replace("{content}", buildStationListHtml(line.getLineStations()));
		return string;
	}
	
	private static String buildStationListHtml(EntityList<LineStation> stations) {
		String string = "";
		for (LineStation station : stations) {
			String stationComponent = LineHtml.stationComponent;
			stationComponent = stationComponent.replace("{departure}", station.getDeparture().toString());
			stationComponent = stationComponent.replace("{displayName}", station.getDisplayName());
			string += stationComponent;
		}
		return string;
	}

}
