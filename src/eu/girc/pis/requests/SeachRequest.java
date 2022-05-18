package eu.girc.pis.requests;

import eu.girc.pis.html.SearchHtml;
import eu.girc.pis.main.RequestHandler;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class SeachRequest implements HttpHandler {

	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		String args[] = RequestHandler.getArgs(exchange.getRequestPath());
		if (args.length == 1) {
			RequestHandler.sendHtml(exchange, SearchHtml.buildSearch());
		} else {
			String search = args[1].replace('&', ' ');
			RequestHandler.sendHtml(exchange, SearchHtml.buildSeachResults(search));
		}
		
	}

}
