package eu.girc.informationsystem.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import eu.derzauberer.javautils.parser.JsonParser;

public class InformationList<T extends InformationEntity> {

	private ArrayList<T> entities;
	
	public InformationList() {
		entities = new ArrayList<>();
	}
	
	public void add(T entity) {
		InformationEntity oldEntity = get(entity.getName());
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
		InformationEntity oldEntity = get(name);
		if (oldEntity != null) {
			entities.remove(oldEntity);
		}
	}
	
	@SuppressWarnings("unchecked")
	public T get(String name) {
		for (InformationEntity entity : entities) {
			if (entity.getName().equalsIgnoreCase(name)) {
				return (T) entity;
			}
		}
		return null;
	}

	public void load(String name, JsonParser parser, Class<T> clazz) {
		List<JsonParser> jsonEntities = parser.getJsonObjectList(name);
		for (JsonParser jsonEntity : jsonEntities) {
			try {
				T entity = clazz.getDeclaredConstructor(JsonParser.class).newInstance(jsonEntity);
				add(entity);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException exception) {
				exception.printStackTrace();
			}
		}
	}
	
	public void save(String name, JsonParser parser) {
		ArrayList<JsonParser> jsonEntities = new ArrayList<>();
		for (InformationEntity entity : entities) {
			jsonEntities.add(entity.toJson());
		}
		parser.set(name, jsonEntities);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<T> getEntities() {
		return (ArrayList<T>) entities.clone();
	}
	
}