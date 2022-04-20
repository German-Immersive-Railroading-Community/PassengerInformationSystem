package eu.girc.pis.html;

import eu.girc.pis.components.Line;
import eu.girc.pis.components.LineStation;
import eu.girc.pis.components.Station;
import eu.girc.pis.main.Main;
import eu.girc.pis.resources.Resource;
import eu.girc.pis.util.EntityList;

public class LineListHtml extends Html {
	
	private static String linePreview = Resource.getTextFile("line-preview.html");
	
	public LineListHtml() {
		super("Lines", buildLineList(Main.getLines()));
	}
	
	public static String buildLineList(EntityList<Line> lines) {
		if (lines.size() > 0) {
			String string = "";
			for (Line line : lines.sort()) {
				string += buildLinePreviewHtml(line);
			}
			return string;
		} else {
			return new HtmlTag("div", "No lines found!", "box").toString();
		}
	}
	
	public static String buildLineList(Station station) {
		EntityList<Line> lines = station.getLines();
		if (lines.size() > 0) {
			String string = "";
			for (Line line : lines) {
				string += buildLinePreviewHtml(line);
			}
			return string;
		} else {
			return new HtmlTag("div", "No lines found!", "box").toString();
		}
	}

	public static String buildLinePreviewHtml(Line line) {
		String string = linePreview;
		string = string.replace("{name}", line.getName());
		string = string.replace("{displayName}", line.getDisplayName());
		string = string.replace("{departure}", line.getDeparture().toString("hh:mm"));
		string = string.replace("{delay}", LineHtml.buildDelay(line.getDelay()));
		if (line.getStations().getFirst() != null) string = string.replace("{fistStation}", line.getStations().getFirst().getDisplayName());
		string = string.replace("{stations}", buildStationListHtml(line.getStations()));
		return string;
	}
	
	private static String buildStationListHtml(EntityList<LineStation> stations) {
		String string = "";
		for (int i = 0; i < stations.size() - 1; i++) {
			string += stations.get(i).getDisplayName() + " - ";
		}
		string += stations.getLast().getDisplayName();
		return string;
	}
	
}
