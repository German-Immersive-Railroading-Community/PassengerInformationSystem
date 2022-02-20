package eu.girc.informationsystem.util;

import java.util.ArrayList;

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
	
	@SuppressWarnings("unchecked")
	public ArrayList<T> getEntities() {
		return (ArrayList<T>) entities.clone();
	}
	
}