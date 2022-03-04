package eu.girc.informationsystem.requests;

import eu.girc.informationsystem.html.StationListHtml;
import eu.girc.informationsystem.main.Main;
import eu.girc.informationsystem.main.RequestHandler;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class StationRequest implements HttpHandler {

	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		String args[] = RequestHandler.getArgs(exchange.getRequestPath());
		if (args.length == 1) {
			RequestHandler.sendHtml(exchange, new StationListHtml());
		} else if (args.length == 2 && Main.getLines().contains(args[1])) {
			//Station
		}
	}

}
