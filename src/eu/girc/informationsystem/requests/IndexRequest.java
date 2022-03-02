package eu.girc.informationsystem.requests;

import eu.girc.informationsystem.components.Line;
import eu.girc.informationsystem.components.Station;
import eu.girc.informationsystem.main.Main;
import eu.girc.informationsystem.main.RequestHandler;
import eu.girc.informationsystem.resources.Resource;
import eu.girc.informationsystem.util.EntityList;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class IndexRequest implements HttpHandler {
	
	private static String indexhtml = Resource.getTextFile("index.html");
	private static String lineComponent = Resource.getTextFile("lineComponent.html");

	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		RequestHandler.sendHtml(exchange, buildLineHtml());
	}
	
	public static String buildLineHtml() {
		String string = "";
		for (Line line : Main.getLines()) {
			string += buildLineComponentHtml(line);
		}
		string = indexhtml.replace("{content}", string);
		return string;
	}
	
	public static String buildLineComponentHtml(Line line) {
		String string = lineComponent;
		string = string.replace("{name}", line.getName());
		string = string.replace("{displayName}", line.getDisplayName());
		string = string.replace("{departure}", line.getDeparture().toString());
		if (line.getStations().getFirst() != null) string = string.replace("{fistStation}", line.getStations().getFirst().getDisplayName());
		string = string.replace("{stations}", buildStationListHtml(line.getStations()));
		return string;
	}
	
	public static String buildStationListHtml(EntityList<Station> stations) {
		String string = "";
		for (int i = 0; i < stations.size() - 1; i++) {
			string += stations.get(i).getDisplayName() + " - ";
		}
		string += stations.getLast().getDisplayName();
		return string;
	}

}
