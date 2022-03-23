package eu.girc.pis.requests;

import eu.girc.pis.html.IndexHtml;
import eu.girc.pis.main.RequestHandler;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class IndexRequest implements HttpHandler {

	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		RequestHandler.sendHtml(exchange, new IndexHtml());
	}

}
