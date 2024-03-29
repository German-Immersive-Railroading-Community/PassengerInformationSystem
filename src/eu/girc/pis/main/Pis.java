package eu.girc.pis.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.girc.pis.model.InternalData;
import eu.girc.pis.model.Line;
import eu.girc.pis.model.Station;
import eu.girc.pis.model.User;
import eu.girc.pis.utils.PisService;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages = {"eu.girc.pis.controller", "eu.girc.pis.utils"})
public class Pis {

	private static InternalData data = new InternalData(new HashSet<>());
	private static final PisService<Station> stationService = new PisService<>("stations", new TypeReference<ArrayList<Station>>() {});
	private static final PisService<Line> lineService = new PisService<>("lines", new TypeReference<ArrayList<Line>>() {});
	private static final PisService<Line> templateService = new PisService<>("templates", new TypeReference<ArrayList<Line>>() {});
	private static final PisService<Line> archiveService = new PisService<>("archived", new TypeReference<ArrayList<Line>>() {});
	private static final PisService<User> userService = new PisService<>("users", new TypeReference<ArrayList<User>>() {});
	
	private static final HashMap<User, String> resetTokens = new HashMap<>();
	
	private static final Random RANDOM = new Random();
	private static final ObjectMapper MAPPER = new ObjectMapper();

	private static final File INTERAL_DATA = new File("data/internal_data.json");

	public static void main(String[] args) {
		new File("data").mkdir();
		try {
			if (INTERAL_DATA.exists()) data = MAPPER.readValue(INTERAL_DATA, InternalData.class);
			MAPPER.writerWithDefaultPrettyPrinter().writeValue(INTERAL_DATA, data);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		SpringApplication.run(Pis.class, args);
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
	
	public static String generateResetToken() {
		final StringBuilder string = new StringBuilder();
		for (int i = 0; i < 20; i++) {
			switch (RANDOM.nextInt(2)) {
			case 0: string.append((char) (RANDOM.nextInt(9) + 48)); break;
			case 1: string.append((char) (RANDOM.nextInt(26) + 65)); break;
			case 2: string.append((char) (RANDOM.nextInt(26) + 97)); break;
			default: break;
			};
		}
		return string.toString();
	}
	
	public static void saveInteralData() {
		try {
			MAPPER.writerWithDefaultPrettyPrinter().writeValue(INTERAL_DATA, data);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
		
	public static PisService<Station> getStationService() {
		return stationService;
	}
	
	public static PisService<Line> getLineService() {
		return lineService;
	}
	
	public static PisService<Line> getTemplateService() {
		return templateService;
	}
	
	public static PisService<Line> getArchiveService() {
		return archiveService;
	}
	
	public static PisService<User> getUserService() {
		return userService;
	}
	
	public static HashMap<User, String> getResetTokens() {
		return resetTokens;
	}

}
