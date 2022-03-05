package eu.girc.informationsystem.html;

import eu.girc.informationsystem.components.Line;
import eu.girc.informationsystem.components.LineStation;
import eu.girc.informationsystem.components.Station;
import eu.girc.informationsystem.main.Main;
import eu.girc.informationsystem.resources.Resource;
import eu.girc.informationsystem.util.EntityList;
import eu.girc.informationsystem.util.Time;

public class LineListHtml extends Html {
	
	private static String linePreview = Resource.getTextFile("line-preview.html");
	
	public LineListHtml() {
		super("lines", buildLineList(Main.getLines()));
	}
	
	public static String buildLineList(EntityList<Line> lines) {
		if (lines.size() > 0) {
			String string = "";
			for (Line line : Time.timeSort(lines)) {
				string += buildLinePreviewHtml(line);
			}
			return string;
		} else {
			return new BoxHtml("No lines found!").toString();
		}
	}
	
	public static String buildLineList(EntityList<Line> lines, Station sort) {
		if (lines.size() > 0) {
			String string = "";
			for (Line line : Time.timeSort(lines, sort)) {
				string += buildLinePreviewHtml(line);
			}
			return string;
		} else {
			return new BoxHtml("No lines found!").toString();
		}
	}

	public static String buildLinePreviewHtml(Line line) {
		String string = linePreview;
		string = string.replace("{name}", line.getName());
		string = string.replace("{displayName}", line.getDisplayName());
		string = string.replace("{departure}", line.getDeparture().toString());
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
