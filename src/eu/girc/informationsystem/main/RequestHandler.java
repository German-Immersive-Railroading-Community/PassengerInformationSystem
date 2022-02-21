package eu.girc.informationsystem.main;

import java.util.ArrayList;
import java.util.HashMap;

import eu.derzauberer.javautils.parser.JsonParser;
import eu.girc.informationsystem.components.Line;
import eu.girc.informationsystem.components.Station;

public class RequestHandler {
	
	public static String sendResponse(String request) {
		if (request.split(" ").length == 3) {
			if (request.contains("GET")) {
				String path = request.split(" ")[1];
				String parameter = "";
				if (path.contains("?")) {
					parameter = path.split("?")[1];
					path = path.split("?")[0];
				}
				if (path.equals("/favicon.ico")) return ""; 
				ArrayList<String> args = getArgs(path);
				HashMap<String, String> params = getParameter(parameter);
				String string = processGetRequest(path, args, params);
				if (!string.isEmpty()) {
					return generateResponse("200 OK", "application/json", string);
				}
			} else {
				return generateResponse("400 Bad Request", "application/json", "{\r\n	\"message\": \"Bad Request!\"\r\n}");
			}
		}
		return generateResponse("404 Not Found", "application/json", "{\r\n	\"message\": \"Not Found!\"\r\n}");
	}
	
	public static String processGetRequest(String path, ArrayList<String> args, HashMap<String, String> params) {
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
	
	private static ArrayList<String> getArgs(String path) {
		ArrayList<String> args = new ArrayList<>();
		for (String string : path.split("/")) {
			if (!string.isEmpty()) {
				args.add(string);
			}
		}
		return args;
	}
	
	private static HashMap<String, String> getParameter(String parameter) {
		HashMap<String, String> parameterList = new HashMap<>();
		int lastSperator = 0;
		int lastValue = 0;
		int index = 0;
		String name = "";
		char chars[] = parameter.toCharArray();
		if (!parameter.endsWith(";") && !parameter.endsWith(";")) chars = (parameter + ";").toCharArray();
		for (char character : chars) {
			if (character == ';' || character == '&') {
				String value = parameter.substring(lastValue, index);
				parameterList.put(name, value);
				lastSperator = index + 1;
			} else if (character == '=') {
				name = parameter.substring(lastSperator, index);
				lastValue = index + 1;
			}
			index++;
		}
		return parameterList;
	}
	
	private static String generateResponse(String code, String contentType, String content) {
		String string = "HTTP/1.1 " + code + "\r\n"
		+ "Content-Type: " + contentType + "\r\n\n"
		+ content;
		return string;
	}

}