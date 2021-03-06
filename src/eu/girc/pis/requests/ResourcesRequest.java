package eu.girc.pis.requests;

import eu.girc.pis.main.RequestHandler;
import eu.girc.pis.resources.Resource;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class ResourcesRequest implements HttpHandler {
	
	private static final String css = Resource.getTextFile("style.css");
	private static final String js = Resource.getTextFile("script.js");

	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		String args[] = RequestHandler.getArgs(exchange.getRequestPath());
		if (args.length == 2) {
			if (args[1].equals("style.css")) {
				RequestHandler.sendText(exchange, css, "css");
			} else if (args[1].equals("script.js")) {
				RequestHandler.sendText(exchange, js, "javascript");
			}
		}
	}

}
