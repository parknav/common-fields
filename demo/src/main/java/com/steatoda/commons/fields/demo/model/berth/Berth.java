package com.steatoda.commons.fields.demo.model.berth;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.steatoda.commons.fields.FieldGraph;
import com.steatoda.commons.fields.FieldUnavailableException;
import com.steatoda.commons.fields.demo.model.DemoFieldsEntity;
import com.steatoda.commons.fields.demo.model.boat.Boat;
import com.steatoda.commons.fields.FieldEnum;

/** Docking berth at marina */
public class Berth extends DemoFieldsEntity<String, Berth, Berth.Field> {

	public enum Field implements FieldEnum {
		
		boat	(Boat.Field.class);

		@SuppressWarnings("unused")
		Field() { this(null); }
		<F extends Enum<F> & FieldEnum> Field(Class<F> clazz) { this.clazz = clazz; }
		@Override
		@SuppressWarnings("unchecked")
		public <F extends Enum<F> & FieldEnum> Class<F> getFieldsClass() { return (Class<F>) clazz; }
		private final Class<?> clazz;
		
	}
	
	public static Berth ref(String id) {
		Berth berth = new Berth();
		berth.setId(id);
		return berth;
	}

	public Berth() {
		super(Field.class);
	}
	
	/** Docked boat */
	@JsonProperty
	public Boat getBoat() { return fieldGet(Field.boat, boat); }
	public Berth setBoat(Boat boat) { this.boat = fieldSet(Field.boat, boat); return this; }

	@Override
	public Object pull(Field field, Berth other, FieldGraph<Field> graph) {
		switch (field) {
			case boat:	return pull(other, other::getBoat,	this::setBoat,	value -> value.clone(field, graph));
		}
		throw new FieldUnavailableException(field);
	}

	@Override
	public Berth ref() {
		return ref(getId());
	}

	private Boat boat;

}
