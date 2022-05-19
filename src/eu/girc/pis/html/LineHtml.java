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
		if (line.getBox() != null) string = line.getBox().toHtml() + "\b" + string;
		string = string.replace("{name}", line.getName());
		string = string.replace("{displayName}", line.getDisplayName());
		string = string.replace("{type}", line.getType());
		string = string.replace("{operator}", line.getOperator().toString());
		string = string.replace("{driver}", line.getDriver().toString());
		string = string.replace("{departure}", line.getDeparture().toString("hh:mm"));
		string = string.replace("{delay}", buildLineDelay(line.getDelay()));
		line.calculateDepartueTimes();
		if (line.getStations().getFirst() != null) string = string.replace("{first.displayName}", line.getStations().getFirst().getDisplayName());
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
		string = string.replace("{delay}", LineHtml.buildLineDelay(line.getDelay()));
		if (line.getStations().getFirst() != null) string = string.replace("{first.displayName}", line.getStations().getFirst().getDisplayName());
		String stationList = "";
		for (int i = 0; i < line.getStations().size() - 1; i++) {
			stationList += line.getStations().get(i).getDisplayName() + " - ";
		}
		stationList += line.getStations().getLast().getDisplayName();
		string = string.replace("{stations}", stationList);
		return string;
	}
	
	private static String buildLineDelay(int delay) {
		if (delay < 3) {
			return "";
		} else if (delay < 6) {
			return "<label class=\"yellow\">+" + delay + "</label>&nbsp;&nbsp;";
		} else {
			return "<label class=\"red\">+" + delay + "</label>&nbsp;&nbsp;";
		}
	}
	
	private static String buildStationDelay(LineStation station) {
		int delay = station.getDelay();
		if (station.isCancelled() || delay == 0) {
			return "";
		} else if (delay < 6) {
			return "<span class=\"yellow-text\">+" + delay + "</span>";
		} else {
			return "<span class=\"red-text\">+" + delay + "</span>";
		}
	}
	
	private static String buildLineString(String string, LineStation station, EntityList<LineStation> stations) {
		if (station == stations.getLast()) string = "<b>" + string + "</b>";
		if (station.isCancelled()) string = "<s class=\"red-text\">" + string + "</s>";
		return string;
	}
	
	private static String buildStationList(EntityList<LineStation> stations) {
		String string = "";
		for (LineStation station : stations) {
			String stationComponent = LineHtml.stationComponent;
			stationComponent = stationComponent.replace("{departure}", buildLineString(station.getDeparture().toString("hh:mm"), station, stations));
			stationComponent = stationComponent.replace("{delay}", buildStationDelay(station));
			stationComponent = stationComponent.replace("{name}", station.getName());
			stationComponent = stationComponent.replace("{displayName}", buildLineString(station.getDisplayName(), station, stations));
			if (station.isCancelled()) {
				stationComponent = stationComponent.replace("{plattform}", "<s class=\"red-text\"> Pl." + station.getPlatform() + "</s>");
			} else if (station.getChangedPlatform() != 0 && station.getChangedPlatform() != station.getPlatform()) {
				stationComponent = stationComponent.replace("{plattform}", "<s> Pl." + station.getPlatform() + "</s><span style=\"color: #e60000 !important\"> Pl. " + station.getChangedPlatform() + "</span>");
			} else {
				stationComponent = stationComponent.replace("{plattform}", "Pl. " + station.getPlatform());
			}
			string += stationComponent;
		}
		return string;
	}

}
