package eu.girc.informationsystem.requests;

import eu.girc.informationsystem.html.SearchHtml;
import eu.girc.informationsystem.main.RequestHandler;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class SeachRequest implements HttpHandler {

	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		String args[] = RequestHandler.getArgs(exchange.getRequestPath());
		if (args.length == 1) {
			RequestHandler.sendHtml(exchange, new SearchHtml());
		} else {
			String seach = args[1].replace('&', ' ');
			RequestHandler.sendHtml(exchange, new SearchHtml(seach));
		}
		
	}

}
