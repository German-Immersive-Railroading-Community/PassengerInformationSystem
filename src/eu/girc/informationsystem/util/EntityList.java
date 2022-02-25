package eu.girc.informationsystem.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import eu.derzauberer.javautils.parser.JsonParser;

public class EntityList<T extends Entity> implements Iterable<T> {

	private ArrayList<T> entities;
	
	public EntityList() {
		entities = new ArrayList<>();
	}
	
	public void add(T entity) {
		Entity oldEntity = get(entity.getName());
		if (oldEntity != null) {
			oldEntity = entity;
		} else {
			entities.add(entity);
		}
	}
	
	public void remove(T entity) {
		remove(entity.getName());
	}
	
	public void remove(String name) {
		Entity oldEntity = get(name);
		if (oldEntity != null) {
			entities.remove(oldEntity);
		}
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
	
	public List<JsonParser> toJsonList() {
		List<JsonParser> jsonEntities = new ArrayList<>();
		entities.forEach(entity -> jsonEntities.add(entity.toJson()));
		return jsonEntities;
	}
	
	public JsonParser toJson() {
		JsonParser parser = new JsonParser();
		parser.set("", toJsonList());
		return parser;
	}

	public void load(String key, JsonParser parser, Class<T> clazz) {
		List<JsonParser> jsonEntities = parser.getJsonObjectList(key);
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