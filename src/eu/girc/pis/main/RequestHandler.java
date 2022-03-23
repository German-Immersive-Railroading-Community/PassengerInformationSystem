package eu.girc.pis.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import eu.derzauberer.javautils.parser.JsonParser;
import eu.derzauberer.javautils.util.Console.MessageType;
import eu.girc.pis.html.Html;
import io.undertow.io.Receiver.FullStringCallback;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class RequestHandler {
	
	private static HttpHandler index;
	private static String html404;
	private static HashMap<String, HttpHandler> requests = new HashMap<>();
	private static HashMap<String, FullStringCallback> callbacks = new HashMap<>();
	private static HttpHandler apiIndex;
	private static HashMap<String, HttpHandler> apiRequests = new HashMap<>();
	private static HashMap<String, FullStringCallback> apiCallbacks = new HashMap<>();
	
	public static void setIndex(HttpHandler request) {
		index = request;
	}
	
	public static void set404Html(String string) {
		html404 = string;
	}
	
	public static void set404Html(Html html) {
		html404 = html.toString();
	}
	
	public static void registerRequest(String name, HttpHandler request) {
		requests.put(name, request);
	}
	
	public static void registerCallback(String name, FullStringCallback callback) {
		callbacks.put(name, callback);
	}
	
	public static void setAPIIndex(HttpHandler request) {
		apiIndex = request;
	}
	
	public static void registerAPIRequest(String name, HttpHandler request) {
		apiRequests.put(name, request);
	}
	
	public static void registerAPICallback(String name, FullStringCallback callback) {
		apiCallbacks.put(name, callback);
	}
	
	public static void execute(HttpServerExchange exchange) throws Exception {
		Main.getConsole().sendMessage("Request from {} for {} {}", MessageType.INFO, exchange.getConnection().getPeerAddress().toString(), exchange.getRequestMethod().toString(), exchange.getRequestPath());
		String args[] = getArgs(exchange.getRequestPath(), false);
		if (args.length > 0 && args[0].equals("api")) {
			execute(exchange, apiIndex, apiRequests, apiCallbacks, getArgs(exchange.getRequestPath()));
			sendAPI404NotFound(exchange);
		} else {
			execute(exchange, index, requests, callbacks, args);
			send404NotFound(exchange);
		}
	}
	
	private static void execute(HttpServerExchange exchange, HttpHandler index, HashMap<String, HttpHandler> requests, HashMap<String, FullStringCallback> callbacks, String args[]) throws Exception {
		if (args.length == 0) {
			if (index != null) {
				index.handleRequest(exchange);
			}
		} else {
			for (String name : requests.keySet()) {
				if (name.equalsIgnoreCase(args[0])) {
					if (!exchange.isResponseStarted()) {
						requests.get(name).handleRequest(exchange);
					} else {
						return;
					}
				}
			}
			for (String name : callbacks.keySet()) {
				if (name.equalsIgnoreCase(args[0])) {
					if (!exchange.isResponseStarted()) {
						exchange.getRequestReceiver().receiveFullString(callbacks.get(name));
					} else {
						return;
					}
				}
			}
		}
	}
	
	public static void sendJson(HttpServerExchange exchange, JsonParser parser) {
		exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
		exchange.getResponseSender().send(parser.toString());
	}
	
	public static void sendHtml(HttpServerExchange exchange, String string) {
		exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");
		exchange.getResponseSender().send(string);
	}
	
	public static void sendHtml(HttpServerExchange exchange, Html html) {
		exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");
		exchange.getResponseSender().send(html.toString());
	}
	
	public static void sendText(HttpServerExchange exchange, String string, String type) {
		exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/" + type);
		exchange.getResponseSender().send(string);
	}
	
	public static void sendImage(HttpServerExchange exchange, String string, String type) {
		exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "image/" + type);
		exchange.getResponseSender().send(string);
	}
	
	public static void send404NotFound(HttpServerExchange exchange) {
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