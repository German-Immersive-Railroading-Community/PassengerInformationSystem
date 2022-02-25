package eu.girc.informationsystem.requests;

import eu.derzauberer.javautils.parser.JsonParser;
import eu.girc.informationsystem.handler.RequestHandler;
import eu.girc.informationsystem.main.Main;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class IndexRequest implements HttpHandler {

	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		JsonParser parser = new JsonParser();
		parser.set("stations", Main.getStations().toJsonList());
		parser.set("lines", Main.getLines().toJsonList());
		RequestHandler.sendJson(exchange, parser);
	}

}
