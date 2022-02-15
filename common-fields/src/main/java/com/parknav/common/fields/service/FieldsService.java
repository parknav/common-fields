package com.parknav.common.fields.service;

import com.parknav.common.fields.FieldEnum;
import com.parknav.common.fields.FieldGraph;
import com.parknav.common.fields.HasEntityFields;

/**
 * Service that can resolve entities with requested ID.
 *
 * @param <I> ID type
 * @param <C> concrete implementation of class implementing {@link HasEntityFields}
 * @param <F> field type
 */
public interface FieldsService<I, C extends HasEntityFields<I, C, F>, F extends Enum<F> & FieldEnum> {

	/**
	 * Same as {@link #get}, but without actual store lookup when {@code graph} is empty.
	 * Use when existence is sure (e.g. resolving foreign key from relational database).
	 *
	 * @param id entity's ID
	 * @param graph field graph to initialize
	 *
	 * @return entity with given {@code id} with fields initialized as specified by {@code graph}
	 */
	default C construct(I id, FieldGraph<F> graph) {
		if (graph.isEmpty()) {
			// short-circuit
			C entity = instance();
			entity.setId(id);
			return entity;
		}
		// default to full lookup
		return get(id, graph);
	}
	
	/**
	 * Checks for entity existence in backing store
	 * 
	 * @param entity Entity to check (reference only)
	 * 
	 * @return {@code true} if entity exists, {@code false} otherwise.
	 */
	default boolean exists(C entity) {
		return get(entity.getId(), FieldGraph.noneOf(entity.getFieldsClass())) != null;
	}

	/**
	 * @return new, uninitialized instance
	 */
	C instance();

	/**
	 * Resolves entity with given {@code id} with fields initialized as specified by {@code graph}.
	 *
	 * @param id entity's ID
	 * @param graph field graph to initialize
	 *
	 * @return resolved entity or {@code null} if entity could not be found
	 */
	C get(I id, FieldGraph<F> graph);
	
}
