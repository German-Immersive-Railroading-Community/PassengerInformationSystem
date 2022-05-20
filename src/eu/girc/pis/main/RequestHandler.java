package eu.girc.pis.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eu.derzauberer.javautils.parser.JsonParser;
import eu.derzauberer.javautils.util.Sender.MessageType;
import eu.girc.pis.util.Html;
import io.undertow.io.Receiver.FullStringCallback;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class RequestHandler {
	
	private HttpHandler index;
	private String html404;
	private HashMap<String, HttpHandler> requests = new HashMap<>();
	private HashMap<String, FullStringCallback> callbacks = new HashMap<>();
	
	public void setIndex(HttpHandler request) {
		index = request;
	}
	
	public void set404Html(String string) {
		html404 = string;
	}
	
	public void set404Html(Html html) {
		html404 = html.toString();
	}
	
	public void registerRequest(String name, HttpHandler request) {
		requests.put(name, request);
	}
	
	public void registerCallback(String name, FullStringCallback callback) {
		callbacks.put(name, callback);
	}
	
	public void execute(HttpServerExchange exchange) throws Exception {
		
		Main.getConsole().sendMessage(MessageType.INFO, "Request from {} for {} {}", exchange.getConnection().getPeerAddress().toString(), exchange.getRequestMethod().toString(), exchange.getRequestPath());
		String path = exchange.getRequestPath();
		if (path.isEmpty() || path.equals("/")) {
			if (index != null) index.handleRequest(exchange);
		} else {
			for (String name : requests.keySet()) {
				if (path.startsWith(name)) {
					if (!exchange.isResponseStarted()) requests.get(name).handleRequest(exchange);
				}
			}
			for (String name : callbacks.keySet()) {
				if (path.startsWith(name)) {
					if (!exchange.isResponseStarted()) exchange.getRequestReceiver().receiveFullString(callbacks.get(name));
				}
			}
		}
		send404NotFound(exchange);
	}
	
	public static void sendHtml(HttpServerExchange exchange, String string) {
		exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");
		exchange.getResponseSender().send(string);
	}
	
	public static void sendJson(HttpServerExchange exchange, JsonParser parser) {
		exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
		exchange.getResponseSender().send(parser.toString());
	}
	
	public static void sendText(HttpServerExchange exchange, String string, String type) {
		exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/" + type);
		exchange.getResponseSender().send(string);
	}
	
	public void send404NotFound(HttpServerExchange exchange) {
		if (!exchange.isResponseStarted()) {
			exchange.setStatusCode(404);
			exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");
			if (html404 != null) {
				exchange.getResponseSender().send(html404);
			} else {
				exchange.getResponseSender().send("");
			}
		}
	}
	
	public static void sendAPI200Success(HttpServerExchange exchange) {
		if (!exchange.isResponseStarted()) {
			exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
			exchange.getResponseSender().send("{\r\n	\"message\": \"Success!\"\r\n}");
		}
	}
	
	public static void sendAPI404NotFound(HttpServerExchange exchange) {
		if (!exchange.isResponseStarted()) {
			exchange.setStatusCode(404);
			exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
			exchange.getResponseSender().send("{\r\n	\"message\": \"Not Found!\"\r\n}");
		}
	}
	
	public static void sendAPI400BadRequet(HttpServerExchange exchange) {
		if (!exchange.isResponseStarted()) {
			exchange.setStatusCode(400);
			exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
			exchange.getResponseSender().send("{\r\n	\"message\": \"Bad Request!\"\r\n}");
		}
	}
	
	public static boolean isPath(String args[], int pos, String target) {
		return args.length >= pos + 1 && args[pos].equalsIgnoreCase(target);
	}
	
	public static String[] getArgs(String path) {
		return getArgs(path, true);
	}
	
	private static String[] getArgs(String path, boolean apiSplit) {
		List<String> argList = new ArrayList<>();
		for (String string : path.split("/")) {
			if (!string.isEmpty() && !(apiSplit && string.equals("api"))) {
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