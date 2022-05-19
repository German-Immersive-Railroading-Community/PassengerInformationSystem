package eu.girc.pis.requests;

import eu.girc.pis.components.Station;
import eu.girc.pis.main.Main;
import eu.girc.pis.main.RequestHandler;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class APIStationRequest implements HttpHandler {

	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		if (exchange.getRequestMethod().toString().equals("GET")) {
			String args[] = RequestHandler.getArgs(exchange.getRequestPath());
			if (args.length == 1) {
				RequestHandler.sendJson(exchange, Main.getStations().toJson());
			} else if (args.length >= 2 && Main.getStations().get(args[1]) != null) {
				if (args.length == 2) {
					RequestHandler.sendJson(exchange, Main.getStations().get(args[1]).toJson());
				} else if (args.length == 3 && RequestHandler.isPath(args, 2, "lines")) {
					RequestHandler.sendJson(exchange, Main.getStations().get(args[1]).getLines().toJson());
				} else if (args.length == 4 && RequestHandler.isPath(args, 2, "lines")) {
					try {
						Station station = Main.getStations().get(args[1]);
						int plattform = Integer.parseInt(args[3]);
						if (station.getPlatforms() >= plattform && plattform >= 1) {
							RequestHandler.sendJson(exchange, Main.getStations().get(args[1]).getLines(plattform).toJson());
						}
					} catch (NumberFormatException exception) {}
				}
			}
		}
	}

}
