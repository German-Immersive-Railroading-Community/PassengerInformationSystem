package eu.girc.pis.resources;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import javax.imageio.ImageIO;
public class Resource {

	public static String getResource(String string) {
		return Resource.class.getResource(string).toExternalForm();
	}
	
	public static InputStream getResourceAsStream(String string) {
		return Resource.class.getResourceAsStream(string);
	}
	
	public static String getTextFile(String name) {
		try (Scanner scanner = new Scanner(Resource.class.getResourceAsStream(name)).useDelimiter("\\A")) {
			String string = scanner.hasNext() ? scanner.next() : "";
			return string;
		}
	}
	
	public static BufferedImage getBufferedImage(String name) {
		try {
			return ImageIO.read(Resource.class.getResourceAsStream(name));
		} catch (IOException exception) {
			return null;
		}
	}
	
}
