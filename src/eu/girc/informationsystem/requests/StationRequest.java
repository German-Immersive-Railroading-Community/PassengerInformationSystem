package eu.girc.informationsystem.requests;

import eu.girc.informationsystem.handler.RequestHandler;
import eu.girc.informationsystem.main.Main;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class StationRequest implements HttpHandler {

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
				}
			}
		}
	}

}
