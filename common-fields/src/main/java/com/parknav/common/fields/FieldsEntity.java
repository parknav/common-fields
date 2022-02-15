package com.parknav.common.fields;

import java.util.Objects;

/**
 * Vanilla implementation of {@link HasEntityFields} interface.
 *
 * @param <I> ID type
 * @param <C> concrete implementation of FieldsEntity
 * @param <F> field type
 */
public abstract class FieldsEntity<I, C extends FieldsEntity<I, C, F>, F extends Enum<F> & FieldEnum> extends FieldsObject<C, F> implements HasEntityFields<I, C, F> {

	protected FieldsEntity(Class<F> fieldsClass) {
		super(fieldsClass);
	}

	/**
	 * Retrieves entity's ID.
	 * @return entity's ID
	 */
	@Override
	public I getId() {
		return id;
	}

	/**
	 * Sets entity's ID.
	 * @param id new entity's ID
	 */
	@SuppressWarnings("unchecked")
	@Override
	public C setId(I id) {
		this.id = id;
		return (C) this;
	}

	@Override
	public String toString() {
		return Objects.toString(id);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FieldsEntity<?, ?, ?> other = (FieldsEntity<?, ?, ?>) obj;
		if (id == null)
			return other.id == null;
		else
			return id.equals(other.id);
	}

	private I id;
	
}
