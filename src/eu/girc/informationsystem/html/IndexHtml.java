package eu.girc.informationsystem.html;

import java.util.List;
import eu.derzauberer.javautils.parser.JsonParser;
import eu.girc.informationsystem.components.Line;
import eu.girc.informationsystem.main.Main;
import eu.girc.informationsystem.resources.Resource;
import eu.girc.informationsystem.util.HtmlComponent;

public class IndexHtml implements HtmlComponent {

	private static String indexhtml = Resource.getTextFile("index.html");
	private static String lineComponent = Resource.getTextFile("lineComponent.html");
	
	private String string = "";
	
	public IndexHtml() {
		for (Line line : Main.getLines()) {
			string += buildComponent(lineComponent, line.toJson());
		}
		string = indexhtml.replace("{content}", string);
	}
	
	@Override
	public String buildComponent(String html, JsonParser parser) {
		String result = HtmlComponent.super.buildComponent(html, parser);
		if (parser.getString("stations.[0].station.displayName") != null) {
			result = result.replace("{firstStation}", parser.getString("stations.[0].station.displayName"));
		}
		String stations = "";
		List<JsonParser> stationList = parser.getJsonObjectList("stations");
		for (int i = 0; i < stationList.size() - 1; i++) {
			stations += stationList.get(i).getString("station.displayName") + " - ";
		}
		stations += stationList.get(stationList.size() - 1).getString("station.displayName");
		result = result.replace("{stations}", stations);
		return result;
	}
	
	@Override
	public String toString() {
		return string;
	}
	
}
