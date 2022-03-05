package eu.girc.informationsystem.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import eu.derzauberer.javautils.parser.JsonParser;
import eu.girc.informationsystem.components.Line;

public class EntityList<T extends Entity> implements Iterable<T> {

	private ArrayList<T> entities;
	
	public EntityList() {
		entities = new ArrayList<>();
	}
	
	public void add(T entity) {
		Entity oldEntity = get(entity.getName());
		if (oldEntity != null) {
			entities.remove(oldEntity);
		}
		entities.add(entity);
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
	
	public EntityList<T> alphabeticalSort() {
		Collections.sort(entities, new Comparator<Entity>() {
		    @Override
		    public int compare(Entity compare1, Entity compare2) {
		        return compare1.getDisplayName().compareToIgnoreCase(compare2.getDisplayName());
		    }
		});
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public List<JsonParser> toJsonList() {
		if (!entities.isEmpty() && entities.get(0) instanceof Line) {
			Time.timeSort((EntityList<Line>) this);
		}
		List<JsonParser> jsonEntities = new ArrayList<>();
		forEach(entity -> jsonEntities.add(entity.toJson()));
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