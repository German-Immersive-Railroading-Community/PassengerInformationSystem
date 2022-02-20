package eu.girc.informationsystem.main;

import java.util.ArrayList;

import eu.derzauberer.javautils.parser.JsonParser;
import eu.girc.informationsystem.components.Line;
import eu.girc.informationsystem.components.Station;

public class RequestHandler {
	
	public static String sendResponse(String request) {
		if (request.contains("GET") && request.split(" ").length == 3) {
			String path = request.split(" ")[1];
			if (path.equals("/favicon.ico")) return "";
			String string = RequestHandler.processRequest(path);
			if (!string.isEmpty()) {
				return generateResponse("200 OK", "application/json", RequestHandler.processRequest(path));
			}
		}
		return generateResponse("404 Not Found", "application/json", "{\r\n	\"message\": \"Not Found!\"\r\n}");
	}
	
	public static String processRequest(String path) {
		if (path.equalsIgnoreCase("/favicon.ico")) {
			return "";
		}
		ArrayList<String> args = getArgs(path);
		if (args.size() == 0) {
			return InformationHandler.getParser().toString();
		} else if (isPath(args, 0, "station")) {
			if (args.size() == 1) {
				ArrayList<JsonParser> stationList = new ArrayList<>();
				for (Station station : InformationHandler.getStations().getEntities()) {
					stationList.add(station.toJson());
				}
				JsonParser parser = new JsonParser();
				parser.set("", stationList);
				return parser.toString();
			} else if (args.size() == 2 && InformationHandler.getStations().get(args.get(1)) != null) {
				return InformationHandler.getStations().get(args.get(1)).toString();
			}
		} else if (isPath(args, 0, "line")) {
			if (args.size() == 1) {
				ArrayList<JsonParser> lineList = new ArrayList<>();
				for (Line line : InformationHandler.getLines().getEntities()) {
					lineList.add(line.toJson());
				}
				JsonParser parser = new JsonParser();
				parser.set("", lineList);
				return parser.toString();
			} else if (args.size() == 2 && InformationHandler.getLines().get(args.get(1)) != null) {
				return InformationHandler.getLines().get(args.get(1)).toString();
			}
		}
		return "";
	}
	
	private static boolean isPath(ArrayList<String> args, int pos, String target) {
		if (args.size() >= pos + 1 && (args.get(pos).equalsIgnoreCase(target) || args.get(pos).equalsIgnoreCase(target + "s"))) return true;
		return false;
	}
	
	public static ArrayList<String> getArgs(String path) {
		ArrayList<String> args = new ArrayList<>();
		for (String string : path.split("/")) {
			if (!string.isEmpty()) {
				args.add(string);
			}
		}
		return args;
	}
	
	private static String generateResponse(String code, String contentType, String content) {
		String string = "HTTP/1.1 " + code + "\r\n"
		+ "Content-Type: " + contentType + "\r\n\n"
		+ content;
		return string;
	}

}