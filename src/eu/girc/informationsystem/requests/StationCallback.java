package eu.girc.informationsystem.requests;

import eu.derzauberer.javautils.parser.JsonParser;
import eu.girc.informationsystem.components.Station;
import eu.girc.informationsystem.main.Main;
import eu.girc.informationsystem.main.RequestHandler;
import io.undertow.io.Receiver.FullStringCallback;
import io.undertow.server.HttpServerExchange;

public class StationCallback implements FullStringCallback {

	@Override
	public void handle(HttpServerExchange exchange, String message) {
		if (exchange.getRequestMethod().toString().equals("POST")) {
			try {
				Station station = new Station(new JsonParser(message));
				if (station.isValid()) {
					Main.getStations().add(station);
					Main.save();
					RequestHandler.send200Success(exchange);
				} else {
					RequestHandler.send400BadRequet(exchange);
				}
			} catch (Exception exception) {
				RequestHandler.send400BadRequet(exchange);
			}
		} else if (exchange.getRequestMethod().toString().equals("DELETE")) {
			String name = new JsonParser(message).getString("name");
			if (name != null && Main.getStations().contains(name)) {
				Main.getStations().remove(new JsonParser(message).getString("name"));
				RequestHandler.send200Success(exchange);
				Main.save();
			} else {
				RequestHandler.send400BadRequet(exchange);
			}
		}
	}

}
