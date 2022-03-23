package eu.girc.pis.requests;

import eu.derzauberer.javautils.parser.JsonParser;
import eu.girc.pis.components.Station;
import eu.girc.pis.main.Main;
import eu.girc.pis.main.RequestHandler;
import io.undertow.io.Receiver.FullStringCallback;
import io.undertow.server.HttpServerExchange;

public class APIStationCallback implements FullStringCallback {

	@Override
	public void handle(HttpServerExchange exchange, String message) {
		if (exchange.getRequestMethod().toString().equals("POST")) {
			try {
				Station station = new Station(new JsonParser(message));
				if (station.isValid()) {
					Main.getStations().add(station);
					Main.save();
					RequestHandler.sendAPI200Success(exchange);
				} else {
					RequestHandler.sendAPI400BadRequet(exchange);
				}
			} catch (Exception exception) {
				RequestHandler.sendAPI400BadRequet(exchange);
			}
		} else if (exchange.getRequestMethod().toString().equals("DELETE")) {
			String name = new JsonParser(message).getString("name");
			if (name != null && Main.getStations().contains(name)) {
				Main.getStations().remove(new JsonParser(message).getString("name"));
				RequestHandler.sendAPI200Success(exchange);
				Main.save();
			} else {
				RequestHandler.sendAPI400BadRequet(exchange);
			}
		}
	}

}
