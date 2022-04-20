package eu.girc.pis.requests;

import java.util.ArrayList;
import eu.derzauberer.javautils.parser.JsonParser;
import eu.derzauberer.javautils.util.Time;
import eu.girc.pis.components.Line;
import eu.girc.pis.components.Station;
import eu.girc.pis.main.RequestHandler;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class APITemplateRequest implements HttpHandler {

	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		if (exchange.getRequestMethod().toString().equals("GET")) {
			String args[] = RequestHandler.getArgs(exchange.getRequestPath());
			if (args.length == 1) {
				RequestHandler.sendAPI400BadRequet(exchange);
			} else if (args.length == 2 && RequestHandler.isPath(args, 1, "station")) {
				Station station = new Station("Station_Name", "Station Name", 1);
				RequestHandler.sendJson(exchange, station.toJson());
			} else if (args.length == 2 && RequestHandler.isPath(args, 1, "line")) {
				Line line = new Line("Line_Name", "Line Name", new Time(9, 0));
				line.setOperator("GIRC");
				line.setDriver("Der_Zauberer");
				line.setDelay(5);
				ArrayList<JsonParser> stations = new ArrayList<>();
				stations.add(new JsonParser().set("station", "Station_Name").set("plattform", 1).set("travelTimeFromLastStation", 0));
				stations.add(new JsonParser().set("station", "Another_Station").set("plattform", 1).set("travelTimeFromLastStation", 5));
				JsonParser parser = line.toJson();
				parser.set("stations", stations);
				RequestHandler.sendJson(exchange, parser);
			}
		} else {
			RequestHandler.sendAPI400BadRequet(exchange);
		}
		
	}

}
