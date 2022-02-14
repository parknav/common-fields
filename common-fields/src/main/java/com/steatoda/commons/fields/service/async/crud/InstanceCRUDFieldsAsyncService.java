package com.steatoda.commons.fields.service.async.crud;

import com.steatoda.commons.fields.FieldEnum;
import com.steatoda.commons.fields.FieldGraph;
import com.steatoda.commons.fields.HasEntityFields;
import com.steatoda.commons.fields.service.async.FieldsRequest;
import com.steatoda.commons.fields.service.async.FieldsServiceHandler;

/**
 * <p>'Instance' part of 'Collection/Instance' CRUD async interface.</p>
 *
 * @see CollectionCRUDFieldsAsyncService
 * @see CRUDFieldsAsyncService
 */
public interface InstanceCRUDFieldsAsyncService<I, C extends HasEntityFields<I, C, F>, F extends Enum<F> & FieldEnum> {

	FieldsRequest get(FieldGraph<F> graph, FieldsServiceHandler<C> handler);

	/**
	 * <p>Updates fields specified by {@code patch} and pulls {@code graph} when done.</p>
	 * 
	 * <p>E.g. to update only field {@code bar} in entity {@code foo}, write something like:</p>
	 * 
	 * <pre>
	 * 	modify(foo.clone(FieldGraph.of(Foo.Field.bar)), ...)
	 * </pre>
	 *
	 * @param patch patch containing fields to update (only modifiable fields will be updated)
	 * @param graph graph to pull into {@code entity} after modification
	 * @param handler asynchronous handler
	 *
	 * @return {@link FieldsRequest} describing this asynchronous operation
	 */
	FieldsRequest modify(C patch, FieldGraph<F> graph, FieldsServiceHandler<C> handler);

	FieldsRequest delete(FieldsServiceHandler<Void> handler);
	
}
