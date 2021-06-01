package com.steatoda.commons.fields.service.async;

import com.steatoda.commons.fields.FieldEnum;
import com.steatoda.commons.fields.FieldGraph;
import com.steatoda.commons.fields.HasEntityFields;

/**
 * Service that can asynchronously resolve entities with requested ID.
 *
 * @param <I> ID type
 * @param <C> concrete implementation of class implementing {@link HasEntityFields}
 * @param <F> field type
 */
public interface FieldsAsyncService<I, C extends HasEntityFields<I, C, F>, F extends Enum<F> & FieldEnum> {

	/**
	 * Same as {@link #get}, but without backend lookup when {@code graph} is empty.
	 * Use when existence is sure (e.g. resolving foreign key from relational database).
	 *
	 * @param id entity's ID
	 * @param graph field graph to initialize
	 * @param handler asynchronous handler
	 *
	 * @return {@link FieldsRequest} describing this asynchronous operation
	 */
	default FieldsRequest construct(I id, FieldGraph<F> graph, FieldsServiceHandler<C> handler) {
		if (graph.isEmpty()) {
			// short-circuit
			C entity = instance();
			entity.setId(id);
			handler.onSuccess(entity);
			return null;
		}
		// default to full lookup
		return get(id, graph, handler);
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
	 * @param handler asynchronous handler
	 *
	 * @return {@link FieldsRequest} describing this asynchronous operation
	 */
	FieldsRequest get(I id, FieldGraph<F> graph, FieldsServiceHandler<C> handler);
	
}
