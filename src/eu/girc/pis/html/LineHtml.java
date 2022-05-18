package eu.girc.pis.html;

import eu.girc.pis.components.Line;
import eu.girc.pis.components.LineStation;
import eu.girc.pis.components.Station;
import eu.girc.pis.resources.Resource;
import eu.girc.pis.util.EntityList;

public class LineHtml {
	
	private static String linePreview = Resource.getTextFile("line-preview.html");
	private static String lineComponent = Resource.getTextFile("line-component.html");
	private static String stationComponent = Resource.getTextFile("line-station-component.html");
	
	public static String buildLine(Line line) {
		String string = lineComponent;
		string = string.replace("{name}", line.getName());
		string = string.replace("{displayName}", line.getDisplayName());
		string = string.replace("{type}", line.getType());
		string = string.replace("{operator}", line.getOperator().toString());
		string = string.replace("{driver}", line.getDriver().toString());
		string = string.replace("{departure}", line.getDeparture().toString("hh:mm"));
		string = string.replace("{delay}", buildDelay(line.getDelay()));
		line.calculateDepartueTimes();
		if (line.getStations().getFirst() != null) string = string.replace("{first.displayName}", line.getStations().getFirst().getDisplayName());
		if (line.getStations().getLast() != null) {
			string = string.replace("{last.departure}", line.getStations().get(line.getStations().size() - 1).getDeparture().toString("hh:mm"));
			string = string.replace("{last.name}", line.getStations().getLast().getName());
			string = string.replace("{last.displayname}", line.getStations().getLast().getDisplayName());
			string = string.replace("{last.plattform}", Integer.toString(line.getStations().getLast().getPlattform()));
		}
		string = string.replace("{content}", buildStationList(line.getStations()));
		return Html.buildHtml("lines", string, true);
	}
	
	public static String buildLineList(EntityList<Line> lines) {
		if (lines.size() > 0) {
			String string = "";
			for (Line line : lines.sort()) {
				string += buildLinePreview(line);
			}
			return Html.buildHtml("lines", string, true);
		} else {
			return new HtmlTag("div", "No lines found!", "box").toString();
		}
	}
	
	public static String buildLineList(Station station) {
		EntityList<Line> lines = station.getLines();
		if (lines.size() > 0) {
			String string = "";
			for (Line line : lines) {
				string += buildLinePreview(line);
			}
			return string;
		} else {
			return new HtmlTag("div", "No lines found!", "box").toString();
		}
	}
	
	public static String buildLinePreview(Line line) {
		String string = linePreview;
		string = string.replace("{name}", line.getName());
		string = string.replace("{displayName}", line.getDisplayName());
		string = string.replace("{departure}", line.getDeparture().toString("hh:mm"));
		string = string.replace("{delay}", LineHtml.buildDelay(line.getDelay()));
		if (line.getStations().getFirst() != null) string = string.replace("{first.displayName}", line.getStations().getFirst().getDisplayName());
		String stationList = "";
		for (int i = 0; i < line.getStations().size() - 1; i++) {
			stationList += line.getStations().get(i).getDisplayName() + " - ";
		}
		stationList += line.getStations().getLast().getDisplayName();
		string = string.replace("{stations}", stationList);
		return string;
	}
	
	private static String buildDelay(int delay) {
		if (delay < 3) {
			return "";
		} else if (delay < 6) {
			return "<label class=\"yellow\">+" + delay + "</label>&nbsp;&nbsp;";
		} else {
			return "<label class=\"red\">+" + delay + "</label>&nbsp;&nbsp;";
		}
	}
	
	private static String buildStationList(EntityList<LineStation> stations) {
		String string = "";
		for (int i = 0; i < stations.size() - 1; i++) {
			String stationComponent = LineHtml.stationComponent;
			stationComponent = stationComponent.replace("{departure}",  stations.get(i).getDeparture().toString("hh:mm"));
			stationComponent = stationComponent.replace("{name}", stations.get(i).getName());
			stationComponent = stationComponent.replace("{displayName}", stations.get(i).getDisplayName());
			stationComponent = stationComponent.replace("{plattform}", Integer.toString(stations.get(i).getPlattform()));
			string += stationComponent;
		}
		return string;
	}

}
