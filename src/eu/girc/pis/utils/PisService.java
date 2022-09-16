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

import eu.girc.pis.component.PisComponent;

public class PisService<T extends PisComponent & Comparable<T>> implements Iterable<T> {

	private final String name;
	private final ArrayList<T> components;
	private final File file;

	private static final List<PisService<?>> services = new ArrayList<>();
	private static final ObjectMapper MAPPER = new ObjectMapper();

	public PisService(String name, TypeReference<ArrayList<T>> reference) {
		if (services.stream().filter(service -> service.getServiceName().equals(name)).findAny().isPresent()) {
			throw new IllegalArgumentException("Service " + name + " does already exist!");
		}
		this.name = name;
		components = new ArrayList<>();
		file = new File("data/" + name + ".json");
		try {
			if (file.exists()) components.addAll(MAPPER.readValue(file, reference));
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		services.add(this);
	}

	public String getServiceName() {
		return name;
	}

	public boolean add(T component) {
		if (contains(component)) return false;
		components.add(component);
		Collections.sort(components);
		save();
		return true;
	}

	public boolean remove(T component) {
		if (!components.remove(component)) return false;
		save();
		return true;
	}

	public boolean remove(String id) {
		Optional<T> component = components.stream().filter(self -> self.getId().equals(id)).findAny();
		if (!component.isPresent()) return false;
		components.remove(component.get());
		save();
		return true;
	}

	public Optional<T> get(String id) {
		return components.stream().filter(component -> component.getId().equals(id)).findAny();
	}

	public boolean contains(String id) {
		return components.stream().filter(component -> component.getId().equals(id)).findAny().isPresent();
	}

	public boolean contains(T component) {
		return components.stream().filter(self -> self.getId().equals(component.getId())).findAny().isPresent();
	}
	
	public boolean update(T component, Consumer<T> consumer) {
		if (!contains(component)) return false;
		consumer.accept(component);
		save();
		return true;
	}
	
	public boolean update(String id, Consumer<T> consumer) {
		Optional<T> component = get(id);
		if (!component.isPresent()) return false;
		consumer.accept(component.get());
		save();
		return true;
	}

	public T first() {
		return components.get(0);
	}

	public T last() {
		return components.get(components.size() - 1);
	}

	public int size() {
		return components.size();
	}

	public boolean isEmpty() {
		return components.isEmpty();
	}

	public Stream<T> stream() {
		return components.stream();
	}

	@Override
	public Iterator<T> iterator() {
		return components.iterator();
	}

	@SuppressWarnings("unchecked")
	public List<T> getAsList() {
		return (List<T>) components.clone();
	}

	public void save() {
		try {
			MAPPER.writerWithDefaultPrettyPrinter().writeValue(file, components);
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
