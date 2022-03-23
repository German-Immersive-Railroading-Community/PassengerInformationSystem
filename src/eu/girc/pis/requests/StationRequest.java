package eu.girc.pis.requests;

import eu.girc.pis.html.StationHtml;
import eu.girc.pis.html.StationListHtml;
import eu.girc.pis.main.Main;
import eu.girc.pis.main.RequestHandler;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class StationRequest implements HttpHandler {

	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		String args[] = RequestHandler.getArgs(exchange.getRequestPath());
		if (args.length == 1) {
			RequestHandler.sendHtml(exchange, new StationListHtml());
		} else if (args.length == 2 && Main.getStations().contains(args[1])) {
			RequestHandler.sendHtml(exchange, new StationHtml(Main.getStations().get(args[1])));
		}
	}

}
