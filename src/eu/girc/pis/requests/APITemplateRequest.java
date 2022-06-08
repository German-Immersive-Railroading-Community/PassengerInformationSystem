package eu.girc.pis.requests;

import eu.derzauberer.javautils.util.Time;
import eu.girc.pis.components.Line;
import eu.girc.pis.components.LineStation;
import eu.girc.pis.components.Station;
import eu.girc.pis.main.RequestHandler;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class APITemplateRequest implements HttpHandler {

	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		if (exchange.getRequestMethod().toString().equals("GET")) {
			final String args[] = RequestHandler.getArgs(exchange.getRequestPath());
			if (args.length == 1) {
				RequestHandler.sendAPI400BadRequet(exchange);
			} else if (args.length == 2 && RequestHandler.isPath(args, 1, "station")) {
				final Station station = new Station("Station_Name", "Station Name", 1);
				RequestHandler.sendJson(exchange, station.toJson());
			} else if (args.length == 2 && RequestHandler.isPath(args, 1, "line")) {
				final Line line = new Line("Line_Name", "Line Name", new Time(9, 0));
				line.setOperator("GIRC");
				line.setDriver("Der_Zauberer");
				line.setDelay(0);
				line.getStations().add(new LineStation(line, new Station("Station_Name", "Station Name", 1), 1, 0));
				line.getStations().add(new LineStation(line, new Station("Another_Station", "Another Station", 1), 1, 5));
				line.getStations().add(new LineStation(line, new Station("Third_Station", "Third Station", 1), 1, 3));
				line.calculateDepartueTimes();
				RequestHandler.sendJson(exchange, line.toJson());
			}
		} else {
			RequestHandler.sendAPI400BadRequet(exchange);
		}
		
	}

}
