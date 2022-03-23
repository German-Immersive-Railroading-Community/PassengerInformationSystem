package eu.girc.pis.requests;

import eu.derzauberer.javautils.parser.JsonParser;
import eu.girc.pis.components.Line;
import eu.girc.pis.main.Main;
import eu.girc.pis.main.RequestHandler;
import io.undertow.io.Receiver.FullStringCallback;
import io.undertow.server.HttpServerExchange;

public class APILineCallback implements FullStringCallback {

	@Override
	public void handle(HttpServerExchange exchange, String message) {
		if (exchange.getRequestMethod().toString().equals("POST")) {
			try {
				Line line = new Line(new JsonParser(message));
				if (line.isValid()) {
					Main.getLines().add(line);
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
			if (name != null && Main.getLines().contains(name)) {
				Main.getLines().remove(name);
				RequestHandler.sendAPI200Success(exchange);
				Main.save();
			} else {
				RequestHandler.sendAPI400BadRequet(exchange);
			}
		}
	}

}
