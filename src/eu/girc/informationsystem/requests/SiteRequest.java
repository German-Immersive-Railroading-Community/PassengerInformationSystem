package eu.girc.informationsystem.requests;

import eu.girc.informationsystem.components.Line;
import eu.girc.informationsystem.components.Station;
import eu.girc.informationsystem.main.Main;
import eu.girc.informationsystem.main.RequestHandler;
import eu.girc.informationsystem.resources.Resource;
import eu.girc.informationsystem.util.EntityList;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class SiteRequest implements HttpHandler {

	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		String string = Resource.getTextFile("index.html");
		String lineElement = Resource.getTextFile("line.html");
		StringBuilder lines = new StringBuilder();
		for (Line line : Main.getLines()) {
			String element = new String(lineElement.toCharArray());
			element = element.replace("{departure}", line.getDeparture().toString());
			element = element.replace("{displayName}", line.getDisplayName());
			element = element.replace("{firstStation}", line.getStations().getFirst().getDisplayName());
			StringBuilder stationString = new StringBuilder();
			EntityList<Station> stations = line.getStations();
			for (int i = 0; i < stations.size() - 1; i++) {
				stationString.append(stations.get(i).getDisplayName() + " - ");
			}
			stationString.append(stations.get(stations.size() - 1).getDisplayName());
			element = element.replace("{stations}", stationString.toString());
			lines.append(element);
		}
		string = string.replace("{content}", lines.toString());
		RequestHandler.sendHtml(exchange, string);
	}

}
