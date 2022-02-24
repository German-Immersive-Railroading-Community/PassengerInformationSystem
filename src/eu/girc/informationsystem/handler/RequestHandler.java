package eu.girc.informationsystem.handler;

import java.util.ArrayList;
import java.util.List;
import eu.derzauberer.javautils.parser.JsonParser;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class RequestHandler {
	
	public static void send404NotFound(HttpServerExchange exchange) {
		if (!exchange.isResponseStarted()) {
			exchange.setStatusCode(404);
			exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
			exchange.getResponseSender().send("{\r\n	\"message\": \"Not Found!\"\r\n}");
		}
	}
	
	public static void send400BadRequet(HttpServerExchange exchange) {
		if (!exchange.isResponseStarted()) {
			exchange.setStatusCode(400);
			exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
			exchange.getResponseSender().send("{\r\n	\"message\": \"Bad Request!\"\r\n}");
		}
	}
	
	public static void sendJson(HttpServerExchange exchange, JsonParser parser) {
		exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
		exchange.getResponseSender().send(parser.toString());
	}
	
	public static boolean isPath(String args[], int pos, String target) {
		return args.length >= pos + 1 && args[pos].equalsIgnoreCase(target);
	}
	
	public static String[] getArgs(String path) {
		List<String> argList = new ArrayList<>();
		for (String string : path.split("/")) {
			if (!string.isEmpty()) {
				argList.add(string);
			}
		}
		String[] args = new String[argList.size()];
		for (int i = 0; i < argList.size(); i++) {
			args[i] = argList.get(i);
		}
		return args;
	}

}