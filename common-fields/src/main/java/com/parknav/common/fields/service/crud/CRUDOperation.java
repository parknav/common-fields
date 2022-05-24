package com.parknav.common.fields.service.crud;

import com.parknav.common.fields.HasEntityFields;

/**
 * <p>Descriptor for one CRUD operation on field-enabled entities.</p>
 */
public interface CRUDOperation<T extends HasEntityFields<?, ?, ?>> {

	enum Type {
		Create,
		Modify,
		Delete
	}

	Type getType();

	T getEntity();

}
