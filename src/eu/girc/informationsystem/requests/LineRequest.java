package eu.girc.informationsystem.requests;

import java.util.ArrayList;
import eu.girc.informationsystem.components.Line;
import eu.girc.informationsystem.components.LineStation;
import eu.girc.informationsystem.main.Main;
import eu.girc.informationsystem.main.RequestHandler;
import eu.girc.informationsystem.resources.Resource;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class LineRequest implements HttpHandler{
	
	private static String linehtml = Resource.getTextFile("line.html");
	private static String stationComponent = Resource.getTextFile("stationComponent.html");

	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		String args[] = RequestHandler.getArgs(exchange.getRequestPath());
		if (args.length == 2 && Main.getLines().contains(args[1])) {
			RequestHandler.sendHtml(exchange, getLineHtml(Main.getLines().get(args[1])));
		}
	}
	
	private static String getLineHtml(Line line) {
		String string = linehtml;
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
	
	private static String buildStationListHtml(ArrayList<LineStation> stations) {
		String string = "";
		for (LineStation station : stations) {
			String stationComponent = LineRequest.stationComponent;
			stationComponent = stationComponent.replace("{departure}", station.getDeparture().toString());
			stationComponent = stationComponent.replace("{displayName}", station.getStation().getDisplayName());
			string += stationComponent;
		}
		return string;
	}
	
}
