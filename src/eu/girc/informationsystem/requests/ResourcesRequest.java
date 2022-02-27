package eu.girc.informationsystem.requests;

import eu.girc.informationsystem.main.RequestHandler;
import eu.girc.informationsystem.resources.Resource;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class ResourcesRequest implements HttpHandler {

	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		String args[] = RequestHandler.getArgs(exchange.getRequestPath());
		if (args.length == 2) {
			String filetype = "html";
			if (args[1].contains(".")) filetype = args[1].split("\\.")[1];
			RequestHandler.sendText(exchange, Resource.getTextFile(args[1]), filetype);
		}
	}

}
