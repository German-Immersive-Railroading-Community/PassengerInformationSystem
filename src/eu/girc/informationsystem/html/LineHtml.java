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
		string = string.replace("{type}", line.getType());
		string = string.replace("{operator}", line.getOperator().toString());
		string = string.replace("{driver}", line.getDriver().toString());
		string = string.replace("{departure}", line.getDeparture().toString());
		string = string.replace("{delay}", buildDelay(line.getDelay()));
		line.calculateDepartueTimes();
		if (line.getStations().getFirst() != null) string = string.replace("{firstStation}", line.getStations().getFirst().getDisplayName());
		if (line.getStations().getLast() != null) {
			string = string.replace("{arrival}", line.getStations().get(line.getStations().size() - 1).getDeparture().toString());
			string = string.replace("{last}", line.getStations().getLast().getName());
			string = string.replace("{lastStation}", line.getStations().getLast().getDisplayName());
			string = string.replace("{plattform}", Integer.toString(line.getStations().getLast().getPlattform()));
		}
		string = string.replace("{content}", buildStationListHtml(line.getStations()));
		return string;
	}
	
	public static String buildDelay(int delay) {
		if (delay < 3) {
			return "";
		} else if (delay < 6) {
			return "<label class=\"yellow\">+" + delay + "</label>&nbsp;&nbsp;";
		} else {
			return "<label class=\"red\">+" + delay + "</label>&nbsp;&nbsp;";
		}
	}
	
	private static String buildStationListHtml(EntityList<LineStation> stations) {
		String string = "";
		for (int i = 0; i < stations.size() - 1; i++) {
			String stationComponent = LineHtml.stationComponent;
			stationComponent = stationComponent.replace("{departure}",  stations.get(i).getDeparture().toString());
			stationComponent = stationComponent.replace("{name}", stations.get(i).getName());
			stationComponent = stationComponent.replace("{displayName}", stations.get(i).getDisplayName());
			stationComponent = stationComponent.replace("{plattform}", Integer.toString(stations.get(i).getPlattform()));
			string += stationComponent;
		}
		return string;
	}

}
