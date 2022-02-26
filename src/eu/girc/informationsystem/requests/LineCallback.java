package eu.girc.informationsystem.requests;

import eu.derzauberer.javautils.parser.JsonParser;
import eu.girc.informationsystem.components.Line;
import eu.girc.informationsystem.handler.RequestHandler;
import eu.girc.informationsystem.main.Main;
import io.undertow.io.Receiver.FullStringCallback;
import io.undertow.server.HttpServerExchange;

public class LineCallback implements FullStringCallback {

	@Override
	public void handle(HttpServerExchange exchange, String message) {
		if (exchange.getRequestMethod().toString().equals("POST")) {
			try {
				Line line = new Line(new JsonParser(message));
				if (line.isValid()) {
					Main.getLines().add(line);
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
			if (name != null && Main.getLines().contains(name)) {
				Main.getLines().remove(name);
				RequestHandler.send200Success(exchange);
				Main.save();
			} else {
				RequestHandler.send400BadRequet(exchange);
			}
		}
	}

}
