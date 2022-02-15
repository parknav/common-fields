package com.parknav.common.fields.service.async.crud;

import java.util.List;
import java.util.Set;

import com.parknav.common.fields.FieldEnum;
import com.parknav.common.fields.FieldGraph;
import com.parknav.common.fields.HasEntityFields;
import com.parknav.common.fields.service.async.FieldsRequest;
import com.parknav.common.fields.service.async.FieldsServiceHandler;

/**
 * <p>'Collection' part of 'Collection/Instance' CRUD async interface.</p>
 * 
 * @see InstanceCRUDFieldsAsyncService
 * @see CRUDFieldsAsyncService
 */
public interface CollectionCRUDFieldsAsyncService<I, C extends HasEntityFields<I, C, F>, F extends Enum<F> & FieldEnum, S> extends CRUDFieldsAsyncService<I, C, F, S> {

	@Override
	default FieldsRequest get(I id, FieldGraph<F> graph, FieldsServiceHandler<C> handler) {
		return instance(id).get(graph, handler);
	}

	FieldsRequest create(C entity, FieldGraph<F> graph, FieldsServiceHandler<C> handler);

	/**
	 * Updates all modifiable fields in {@code entity} and pulls {@code graph} into it when done.
	 * 
	 * @param entity entity to update and pull changes to
	 * @param graph graph to pull into {@code entity} after modification
	 */
	default FieldsRequest modify(C entity, FieldGraph<F> graph, FieldsServiceHandler<C> handler) {
		return modify(entity, entity.cloneAll(), graph, handler);	// NOTE: full clone is required (not cloneFlat or anything like that), because created patch may contain subentities that have to be stored, too
	}

	/**
	 * <p>Updates fields in {@code entity} specified by {@code fields} and pulls {@code graph} when done.</p>
	 * 
	 * <p>E.g. to update only field {@code bar} in entity {@code foo}, write something like:</p>
	 * 
	 * <pre>
	 * 	modify(foo, EnumSet.of(Foo.Field.bar), ...)
	 * </pre>
	 *
	 * @param entity entity to update and pull changes to
	 * @param fields fields from entity to update (only modifiable fields will be updated)
	 * @param graph graph to pull into {@code entity} after modification
	 */
	default FieldsRequest modify(C entity, Set<F> fields, FieldGraph<F> graph, FieldsServiceHandler<C> handler) {
		C patch = entity.ref();
		patch.pull(entity, fields);
		return modify(entity, patch, graph, handler);
	}

	/**
	 * <p>Updates fields in {@code entity} specified by {@code patch} and pulls {@code graph} when done.</p>
	 * 
	 * <p>E.g. to update only field {@code bar} in entity {@code foo}, write something like:</p>
	 * 
	 * <pre>
	 * 	modify(foo, foo.clone(FieldGraph.of(Foo.Field.bar)), ...)
	 * </pre>
	 *
	 * @param entity entity to update and pull changes to
	 * @param patch patch containing fields to update (only modifiable fields will be updated)
	 * @param graph graph to pull into {@code entity} after modification
	 */
	default FieldsRequest modify(C entity, C patch, FieldGraph<F> graph, FieldsServiceHandler<C> handler) {
		return instance(entity).modify(patch, graph, handler);
	}

	@Override
	default FieldsRequest delete(C entity, FieldsServiceHandler<Void> handler) {
		return instance(entity).delete(handler);
	}
	
	FieldsRequest getAllFieldValues(S selector, Set<F> fields, FieldsServiceHandler<List<C>> handler);
	
	FieldsRequest count(S selector, FieldsServiceHandler<Integer> handler);

	FieldsRequest list(S selector, FieldGraph<F> graph, FieldsServiceHandler<List<C>> handler);

	InstanceCRUDFieldsAsyncService<I, C, F> instance(I id);
	default InstanceCRUDFieldsAsyncService<I, C, F> instance(C entity) { return instance(entity.getId()); }

}
