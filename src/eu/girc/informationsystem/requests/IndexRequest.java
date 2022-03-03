package eu.girc.informationsystem.requests;

import eu.girc.informationsystem.html.LineListHtml;
import eu.girc.informationsystem.main.RequestHandler;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class IndexRequest implements HttpHandler {

	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		RequestHandler.sendHtml(exchange, new LineListHtml());
	}

}
