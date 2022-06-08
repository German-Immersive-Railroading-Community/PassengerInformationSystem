package eu.girc.pis.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import eu.derzauberer.javautils.parser.JsonParser;
import eu.girc.pis.components.Line;
import eu.girc.pis.components.Station;

public class EntityList<T extends Entity> implements Iterable<T> {

	private ArrayList<T> entities;
	
	public EntityList() {
		entities = new ArrayList<>();
	}
	
	public void add(T entity) {
		final Entity oldEntity = get(entity.getName());
		if (oldEntity != null) {
			entities.remove(oldEntity);
		}
		entities.add(entity);
	}
	
	public void remove(T entity) {
		remove(entity.getName());
	}
	
	public void remove(String name) {
		final Entity oldEntity = get(name);
		if (oldEntity != null) {
			entities.remove(oldEntity);
		}
	}
	
	public void clear() {
		entities.clear();
	}
	
	public boolean contains(T entity) {
		return contains(entity.getName());
	}
	
	public boolean contains(String name) {
		for (T entity : entities) {
			if (entity.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public T get(String name) {
		for (Entity entity : entities) {
			if (entity.getName().equalsIgnoreCase(name)) {
				return (T) entity;
			}
		}
		return null;
	}
	
	public T get(int index) {
		if (entities.size() >= index) {
			return entities.get(index);
		}
		return null;
	}
	
	public T getFirst() {
		if (entities.size() > 0) {
			return entities.get(0);
		}
		return null;
	}
	
	public T getLast() {
		if (entities.size() > 0) {
			return entities.get(entities.size() - 1);
		}
		return null;
	}
	
	public int size() {
		return entities.size();
	}
	
	public EntityList<T> sort() {
		entities.sort((entity1, entity2) -> {
			if (entity1 instanceof Line && entity2 instanceof Line) {
				return ((Line) entity1).getDeparture().compareTo(((Line) entity2).getDeparture());
			} else {
				return entity1.getDisplayName().compareTo(entity2.getDisplayName());
			}
		});
		return this;
	}
	
	public EntityList<T> sort(Station station) {
		entities.sort((entity1, entity2) -> {
			if (entity1 instanceof Line && entity2 instanceof Line) {
				final Line line1 = (Line) entity1;
				final Line line2 = (Line) entity2;
				if (line1.getLineStation(station) != null && line2.getLineStation(station) != null) {
					return line1.getLineStation(station).getDeparture().compareTo(line1.getLineStation(station).getDeparture());
				}
				return ((Line) entity1).getDeparture().compareTo(((Line) entity2).getDeparture());
			} else {
				return entity1.getDisplayName().compareTo(entity2.getDisplayName());
			}
		});
		return this;
	}
	
	public EntityList<T> searchForDisplayName(String name) {
		final EntityList<T> entities = new EntityList<>();
		for (T entity : this.sort()) {
			if (entity.getDisplayName().contains(name)) {
				entities.add(entity);
			}
		}
		return entities;
	}
	
	public List<JsonParser> toJsonList() {
		final List<JsonParser> jsonEntities = new ArrayList<>();
		forEach(entity -> jsonEntities.add(entity.toJson()));
		return jsonEntities;
	}
	
	public JsonParser toJson() {
		final JsonParser parser = new JsonParser();
		parser.set("", toJsonList());
		return parser;
	}

	public void load(String key, JsonParser parser, Class<T> clazz) {
		final List<JsonParser> jsonEntities = parser.getJsonObjectList(key);
		for (JsonParser jsonEntity : jsonEntities) {
			try {
				T entity = clazz.getDeclaredConstructor(JsonParser.class).newInstance(jsonEntity);
				add(entity);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException exception) {
				exception.printStackTrace();
			}
		}
	}
	
	public void save(String key, JsonParser parser) {
		parser.set(key, toJsonList());
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<T> getEntities() {
		return (ArrayList<T>) entities.clone();
	}

	@Override
	public Iterator<T> iterator() {
		return entities.iterator();
	}
	
}