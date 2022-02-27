package eu.girc.informationsystem.requests;

import eu.girc.informationsystem.components.Line;
import eu.girc.informationsystem.components.LineStation;
import eu.girc.informationsystem.components.Station;
import eu.girc.informationsystem.main.RequestHandler;
import eu.girc.informationsystem.util.Time;
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
				line.getLineStations().add(new LineStation(new Station("Station_Name", "Station Name", 1), 1, 0));
				line.getLineStations().add(new LineStation(new Station("Another_Station", "Another Station", 1), 1, 5));
				RequestHandler.sendJson(exchange, line.toJson());
			}
		} else {
			RequestHandler.sendAPI400BadRequet(exchange);
		}
		
	}

}
