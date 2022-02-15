package com.parknav.common.fields.service.async.crud;

import java.util.List;
import java.util.Set;

import com.parknav.common.fields.service.crud.CRUDFieldsService;
import com.parknav.common.fields.FieldEnum;
import com.parknav.common.fields.FieldGraph;
import com.parknav.common.fields.HasEntityFields;
import com.parknav.common.fields.service.async.FieldsAsyncService;
import com.parknav.common.fields.service.async.FieldsRequest;
import com.parknav.common.fields.service.async.FieldsServiceHandler;

/**
 * <p>'Flat' CRUD async interface.</p>
 *
 * @param <I> ID type
 * @param <C> concrete implementation of class implementing {@link HasEntityFields}
 * @param <F> field type
 * @param <S> selector used to filter values
 *
 * @see CollectionCRUDFieldsAsyncService
 * @see InstanceCRUDFieldsAsyncService
 * @see CRUDFieldsService
 */
public interface CRUDFieldsAsyncService<I, C extends HasEntityFields<I, C, F>, F extends Enum<F> & FieldEnum, S> extends FieldsAsyncService<I, C, F> {

	/**
	 * Created new entity and populates it with fields specified by {@code graph}.
	 *
	 * @param entity entity to create
	 * @param graph fields graph to resolve
	 * @param handler asynchronous handler
	 *
	 * @return {@link FieldsRequest} describing this asynchronous operation
	 */
	FieldsRequest create(C entity, FieldGraph<F> graph, FieldsServiceHandler<C> handler);

	/**
	 * Updates all modifiable fields in {@code entity} and pulls {@code graph} into it when done.
	 * 
	 * @param entity entity to update and pull changes to
	 * @param graph graph to pull into {@code entity} after modification
	 * @param handler asynchronous handler
	 *
	 * @return {@link FieldsRequest} describing this asynchronous operation
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
	 * @param handler asynchronous handler
	 *
	 * @return {@link FieldsRequest} describing this asynchronous operation
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
	 * @param handler asynchronous handler
	 *
	 * @return {@link FieldsRequest} describing this asynchronous operation
	 */
	FieldsRequest modify(C entity, C patch, FieldGraph<F> graph, FieldsServiceHandler<C> handler);

	/**
	 * Deletes entity.
	 *
	 * @param entity entity to delete
	 * @param handler asynchronous handler
	 *
	 * @return {@link FieldsRequest} describing this asynchronous operation
	 */
	FieldsRequest delete(C entity, FieldsServiceHandler<Void> handler);

	/**
	 * <p>Retrieves list of entities each set to distinct combination of requested fields.</p>
	 *
	 * <p>NOTE: returned entities are just placeholders e.g. nor do they exist as such in backing store nor they have ID set.
	 * Each of them is used just to represent one combination of requested fields in backing store.
	 * Example usage: pass only one field and get all possible values for it to provide autocomplete functionality.</p>
	 *
	 * @param selector selector to filter entities
	 * @param fields fields on which to determine distinctiveness
	 * @param handler asynchronous handler
	 *
	 * @return {@link FieldsRequest} describing this asynchronous operation
	 */
	FieldsRequest getAllFieldValues(S selector, Set<F> fields, FieldsServiceHandler<List<C>> handler);

	/**
	 * Counts entities matched by provided {@code selector}.
	 *
	 * @param selector selector to filter entities
	 * @param handler asynchronous handler
	 *
	 * @return {@link FieldsRequest} describing this asynchronous operation
	 */
	FieldsRequest count(S selector, FieldsServiceHandler<Integer> handler);

	/**
	 * List ALL entities matched by provided {@code selector}. Be careful when there could be large number of such entities.
	 * In that case, provide domain-specialized list method that supports pagination.
	 *
	 * @param selector selector to filter entities
	 * @param graph fields graph to resolve
	 * @param handler asynchronous handler
	 *
	 * @return {@link FieldsRequest} describing this asynchronous operation
	 */
	FieldsRequest list(S selector, FieldGraph<F> graph, FieldsServiceHandler<List<C>> handler);

}
