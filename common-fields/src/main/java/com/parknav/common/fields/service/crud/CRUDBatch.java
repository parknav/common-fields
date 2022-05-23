package com.parknav.common.fields.service.crud;

import com.parknav.common.fields.HasEntityFields;

import java.util.List;

/**
 * <p>Descriptor for one "batch" of operations on field-enabled entities.
 * All operations in a batch are executed sequentially and transactional.</p>
 */
public interface CRUDBatch<T extends HasEntityFields<?, ?, ?>> {

	interface Operation<T extends HasEntityFields<?, ?, ?>> {

		enum Type {
			Create,
			Modify,
			Delete
		}

		Type getType();

		T getEntity();

	}

	/** List of operation to perform (sequentially) */
	List<? extends Operation<T>> getOperations();

}
