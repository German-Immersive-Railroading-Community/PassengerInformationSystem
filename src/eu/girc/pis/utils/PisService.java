package eu.girc.pis.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.girc.pis.entities.PisEntity;

public class PisService<T extends PisEntity & Comparable<T>> implements Iterable<T> {

	private final String name;
	private final ArrayList<T> entities;
	private final File file;

	private static final List<PisService<?>> services = new ArrayList<>();
	private static final ObjectMapper MAPPER = new ObjectMapper();

	public PisService(String name, TypeReference<ArrayList<T>> reference) {
		if (services.stream().filter(service -> service.getServiceName().equals(name)).findAny().isPresent()) {
			throw new IllegalArgumentException("Service " + name + " does already exist!");
		}
		this.name = name;
		entities = new ArrayList<>();
		file = new File("data/" + name + ".json");
		try {
			if (file.exists()) entities.addAll(MAPPER.readValue(file, reference));
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		services.add(this);
	}

	public String getServiceName() {
		return name;
	}

	public boolean add(T entity) {
		if (contains(entity)) return false;
		entities.add(entity);
		Collections.sort(entities);
		save();
		return true;
	}

	public boolean remove(T entity) {
		if (!entities.remove(entity)) return false;
		save();
		return true;
	}

	public boolean remove(String id) {
		Optional<T> entity = entities.stream().filter(self -> self.getId().equals(id)).findAny();
		if (!entity.isPresent()) return false;
		entities.remove(entity.get());
		save();
		return true;
	}

	public Optional<T> get(String id) {
		return entities.stream().filter(entity -> entity.getId().equals(id)).findAny();
	}

	public boolean contains(String id) {
		return entities.stream().filter(entity -> entity.getId().equals(id)).findAny().isPresent();
	}

	public boolean contains(T entity) {
		return entities.stream().filter(self -> self.getId().equals(entity.getId())).findAny().isPresent();
	}
	
	public boolean update(T entity, Consumer<T> consumer) {
		if (!contains(entity)) return false;
		consumer.accept(entity);
		save();
		return true;
	}
	
	public boolean update(String id, Consumer<T> consumer) {
		Optional<T> entity = get(id);
		if (!entity.isPresent()) return false;
		consumer.accept(entity.get());
		save();
		return true;
	}

	public T first() {
		return entities.get(0);
	}

	public T last() {
		return entities.get(entities.size() - 1);
	}

	public int size() {
		return entities.size();
	}

	public boolean isEmpty() {
		return entities.isEmpty();
	}

	public Stream<T> stream() {
		return entities.stream();
	}

	@Override
	public Iterator<T> iterator() {
		return entities.iterator();
	}

	@SuppressWarnings("unchecked")
	public List<T> getAsList() {
		return (List<T>) entities.clone();
	}

	public void save() {
		try {
			MAPPER.writerWithDefaultPrettyPrinter().writeValue(file, entities);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public static Optional<PisService<?>> getService(String name) {
		return services.stream()
				.filter(service -> service.getServiceName().equals(name))
				.findAny();
	}

}
