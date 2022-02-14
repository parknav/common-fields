package com.steatoda.commons.fields.demo.model.boat;

import java.util.Set;

import com.steatoda.commons.fields.FieldGraph;
import com.steatoda.commons.fields.service.FieldsService;

public interface BoatService extends FieldsService<String, Boat, Boat.Field> {

	@Override
	default Boat instance() {
		return new Boat();
	}

	/**
	 * Gets boat with given id.
	 * 
	 * @param id Boat's id.
	 * @param fields Fields to initialize.
	 * 
	 * @return {@link Boat} for given id with requested fields initialized
	 * 			or <code>null</code> if boat with given id doesn't exist
	 */
	@Override
	Boat get(String id, FieldGraph<Boat.Field> fields);

	/**
	 * Stores new boat in store and populates generated fields.
	 * 
	 * @param boat {@link Boat} with <u>all</u> fields set.
	 * 
	 * @throws com.steatoda.commons.fields.FieldUnavailableException if not all fields are initialized
	 */
	void create(Boat boat);
	
	/**
	 * Modifies boat in store. Fields to update are taken from {@link Boat#getFields()}.
	 * 
	 * @param boat Boat to modify.
	 */
	default void modify(Boat boat) {
		modify(boat, boat.getFields());
	}
	
	/**
	 * Modifies requested fields for boat in store.
	 * 
	 * @param boat Boat to modify.
	 * @param fields Which fields in boat to modify.
	 */
	void modify(Boat boat, Set<Boat.Field> fields);

	/**
	 * Deletes boat from store.
	 * 
	 * @param boat {@link Boat} with only id required.
	 */
	void delete(Boat boat);

}
