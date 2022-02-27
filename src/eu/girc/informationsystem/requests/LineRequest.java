package eu.girc.informationsystem.requests;

import eu.girc.informationsystem.main.Main;
import eu.girc.informationsystem.main.RequestHandler;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class LineRequest implements HttpHandler {

	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		if (exchange.getRequestMethod().toString().equals("GET")) {
			String args[] = RequestHandler.getArgs(exchange.getRequestPath());
			if (args.length == 1) {
				RequestHandler.sendJson(exchange, Main.getLines().toJson());
			} else if (args.length == 2 && Main.getLines().get(args[1]) != null) {
				RequestHandler.sendJson(exchange, Main.getLines().get(args[1]).toJson());
			}
		}
	}

}
