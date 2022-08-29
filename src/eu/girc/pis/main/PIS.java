package eu.girc.pis.main;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.girc.pis.entities.Entity;
import eu.girc.pis.entities.InternalData;
import eu.girc.pis.entities.Line;
import eu.girc.pis.entities.Station;
import eu.girc.pis.entities.User;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages = {"eu.girc.pis.controller", "eu.girc.pis.utils"})
public class PIS {

	private static InternalData data = new InternalData(new HashSet<>());
	private static final TreeSet<User> users = new TreeSet<>();
	private static final TreeSet<Station> stations = new TreeSet<>();
	private static final TreeSet<Line> lines = new TreeSet<>();
	private static final TreeSet<Line> templates = new TreeSet<>();
	private static final TreeSet<Line> archived = new TreeSet<>();
	
	private static final Random RANDOM = new Random();
	private static final ObjectMapper MAPPER = new ObjectMapper();

	private static final File INTERAL_DATA = new File("data/internal_data.json");
	private static final File USERS_FILE = new File("data/user.json");
	private static final File STATIONS_FILE = new File("data/stations.json");
	private static final File LINES_FILE = new File("data/lines.json");
	private static final File TEMPLATES_FILE = new File("data/templates.json");
	private static final File ARCHIVE_FILE = new File("data/archived.json");

	public static void main(String[] args) {
		new File("data").mkdir();
		try {
			if (INTERAL_DATA.exists()) data = MAPPER.readValue(INTERAL_DATA, InternalData.class);
			if (USERS_FILE.exists()) users.addAll(MAPPER.readValue(USERS_FILE, new TypeReference<TreeSet<User>>(){}));
			if (STATIONS_FILE.exists()) stations.addAll(MAPPER.readValue(STATIONS_FILE, new TypeReference<TreeSet<Station>>(){}));
			if (LINES_FILE.exists()) lines.addAll(MAPPER.readValue(LINES_FILE, new TypeReference<TreeSet<Line>>(){}));
			if (TEMPLATES_FILE.exists()) templates.addAll(MAPPER.readValue(TEMPLATES_FILE, new TypeReference<TreeSet<Line>>(){}));
			if (ARCHIVE_FILE.exists()) archived.addAll(MAPPER.readValue(ARCHIVE_FILE, new TypeReference<TreeSet<Line>>(){}));
			MAPPER.writerWithDefaultPrettyPrinter().writeValue(INTERAL_DATA, data);
			MAPPER.writerWithDefaultPrettyPrinter().writeValue(USERS_FILE, users);
			MAPPER.writerWithDefaultPrettyPrinter().writeValue(STATIONS_FILE, stations);
			MAPPER.writerWithDefaultPrettyPrinter().writeValue(LINES_FILE, lines);
			MAPPER.writerWithDefaultPrettyPrinter().writeValue(TEMPLATES_FILE, templates);
			MAPPER.writerWithDefaultPrettyPrinter().writeValue(ARCHIVE_FILE, archived);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		SpringApplication.run(PIS.class, args);
	}

	public static String generate8BitId() {
		int generated;
		do {
			generated = RANDOM.nextInt(65535);
		} while (data.getGeneratedIds().contains(generated));
		data.getGeneratedIds().add(generated);
		saveInteralData();
		return String.format("%04x", generated);
	}

	public static <T extends Entity> Optional<T> find(Set<T> list, String id) {
		return list.stream().filter(element -> element.getId().equals(id)).findAny();
	}
	
	public static void saveInteralData() {
		try {
			MAPPER.writerWithDefaultPrettyPrinter().writeValue(INTERAL_DATA, data);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	public static void saveStations() {
		try {
			MAPPER.writerWithDefaultPrettyPrinter().writeValue(STATIONS_FILE, stations);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public static TreeSet<Station> getStations() {
		return stations;
	}
	
	public static void saveLines() {
		try {
			MAPPER.writerWithDefaultPrettyPrinter().writeValue(LINES_FILE, lines);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public static TreeSet<Line> getLines() {
		return lines;
	}
	
	public static void saveTemplates() {
		try {
			MAPPER.writerWithDefaultPrettyPrinter().writeValue(TEMPLATES_FILE, templates);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	public static TreeSet<Line> getTemplates() {
		return templates;
	}
	
	public static void saveArchived() {
		try {
			MAPPER.writerWithDefaultPrettyPrinter().writeValue(ARCHIVE_FILE, archived);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	public static TreeSet<Line> getArchived() {
		return archived;
	}
	
	public static void saveUsers() {
		try {
			MAPPER.writerWithDefaultPrettyPrinter().writeValue(USERS_FILE, users);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	public static TreeSet<User> getUsers() {
		return users;
	}

}
