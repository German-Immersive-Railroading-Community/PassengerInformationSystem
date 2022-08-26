package eu.girc.pis.entities;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import eu.girc.pis.main.PIS;
import eu.girc.pis.utils.SecurityConfig;
import eu.girc.pis.utils.TrainType;

public enum DataType {
	
	STATIONS(PIS.getStations(), () -> PIS.saveStations(), () -> new Station("", "", 1)),
	LINES(PIS.getLines(), () -> PIS.saveLines(), () -> new Line(null, TrainType.INTERCITY_EXPRESS, 1, "", "", LocalTime.of(0, 0), false, 0, null)),
	USERS(PIS.getUsers(), () -> PIS.saveUsers(), () -> new User("", "", "", SecurityConfig.getPasswordEncoder().encode("1234"), false, ""));
	
	private final Set<? extends Entity> set;
	private final Runnable save;
	private final Supplier<? extends Entity> standard;
	
	private <T extends Entity> DataType(Set<T> set, Runnable save, Supplier<T> standard) {
		this.set = set;
		this.save = save;
		this.standard = standard;
	}
	
	public Set<? extends Entity> getSet() {
		return set;
	}
	
	public Entity getStandard() {
		return standard.get();
	}
	
	public void save() {
		save.run();
	}
	
	public static Optional<DataType> fromString(String string) {
		return Arrays.asList(DataType.values())
			.stream().filter(type -> type.name().toLowerCase().equals(string))
			.findAny();
	}

}
