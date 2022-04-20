package eu.girc.pis.requests;

import eu.derzauberer.javautils.parser.JsonParser;
import eu.girc.pis.main.RequestHandler;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class APIIndexRequest implements HttpHandler {

	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception { 
		if (exchange.getRequestPath().equals("/api") || exchange.getRequestPath().equals("/api/")) {
			if (exchange.getRequestMethod().toString().equals("GET")) {
				JsonParser parser = new JsonParser();
				parser.set("stations", "/station/");
				parser.set("specific_station", "/station/{station_name}/");
				parser.set("lines_of_station", "/station/{station_name}/lines/");
				parser.set("lines_of_plattform", "/station/{station_name}/lines/{plattform}/");
				parser.set("lines", "/line/");
				parser.set("specific_line", "/line/{line_name}/");
				parser.set("station_template", "/template/station/");
				parser.set("line_template", "/template/line/");
				RequestHandler.sendJson(exchange, parser);
			} else {
				RequestHandler.sendAPI400BadRequet(exchange);
			}
		}
		
	}

}
