package com.parknav.common.fields.service.crud;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.parknav.common.fields.service.FieldsService;
import com.parknav.common.fields.FieldEnum;
import com.parknav.common.fields.FieldGraph;
import com.parknav.common.fields.HasEntityFields;

/**
 * Extends {@link FieldsService} with common CRUD (Create, Read, Update, Delete) operations.
 *
 * @param <I> ID type
 * @param <C> concrete implementation of class implementing {@link HasEntityFields}
 * @param <F> field type
 * @param <S> selector used to filter values
 */
public interface CRUDFieldsService<I, C extends HasEntityFields<I, C, F>, F extends Enum<F> & FieldEnum, S> extends FieldsService<I, C, F> {

	/**
	 * Created new entity and populates it with fields specified by {@code graph}.
	 *
	 * @param entity entity to create
	 * @param graph fields graph to resolve
	 */
	void create(C entity, FieldGraph<F> graph);

	/**
	 * Updates all modifiable fields in {@code entity} and pulls {@code graph} into it when done.
	 * 
	 * @param entity entity to update and pull changes to
	 * @param graph graph to pull into {@code entity} after modification
	 */
	default void modify(C entity, FieldGraph<F> graph) {
		modify(entity, entity.cloneAll(), graph);	// NOTE: full clone is required (not cloneFlat or anything like that), because created patch may contain subentities that have to be stored, too
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
	default void modify(C entity, Set<F> fields, FieldGraph<F> graph) {
		C patch = entity.ref();
		patch.pull(entity, fields);
		modify(entity, patch, graph);
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
	void modify(C entity, C patch, FieldGraph<F> graph);

	/**
	 * Deletes entity.
	 *
	 * @param entity entity to delete
	 */
	void delete(C entity);

	/**
	 * <p>Retrieves entities with distinct combination of requested fields.</p>
	 *
	 * <p>NOTE: returned entities are just placeholders e.g. nor do they exist as such in backing store nor they have ID set.
	 * Each of them is used just to represent one combination of requested fields in backing store.
	 * Example usage: pass only one field and get all possible values for it to provide autocomplete functionality.</p>
	 *
	 * @param selector selector to filter entities
	 * @param fields fields on which to determine distinctiveness
	 *
	 * @return stream of all combinations of required fields
	 */
	Stream<C> queryAllFieldValues(S selector, Set<F> fields);

	/**
	 * <p>Invokes {@link #queryAllFieldValues}, collects whole stream into a {@link List} and closes the stream.</p>
	 *
	 * @param selector selector to filter entities
	 * @param fields fields on which to determine distinctiveness
	 *
	 * @return list of all combinations of required fields
	 */
	default List<C> listAllFieldValues(S selector, Set<F> fields) {
		try (Stream<C> stream = queryAllFieldValues(selector, fields)) {
			return stream.collect(Collectors.toList());
		}
	}

	/**
	 * <p>Performs operation in one transactional batch and pulls {@code graph} for each created and modified entity
	 * (deleted entites are left as-is).</p>
	 *
	 * @param operations operations to perform
	 * @param graph graph to pull into each entity after create/modify
	 */
	default void batch(List<? extends CRUDOperation<C>> operations, FieldGraph<F> graph) {
		operations.forEach(operation -> perform(operation, graph));
	}

	/**
	 * <p>Performs operation in one transactional batch.</p>
	 *
	 * <p><b>IMPORTANT:</b> entites are <b>not</b> updated!</p>
	 *
	 * @param operations operations to perform
	 */
	default void batch(Stream<? extends CRUDOperation<C>> operations) {
		FieldGraph<F> graph = FieldGraph.noneOf(instance().getFieldsClass());
		operations.forEach(operation -> perform(operation, graph));
	}

	/**
	 * <p>Performs one operation and pulls {@code graph} for each created and modified entity
	 * (deleted entites are left as-is).</p>
	 *
	 * @param operation operation to perform
	 * @param graph graph to pull into each created or modified entity
	 */
	default void perform(CRUDOperation<C> operation, FieldGraph<F> graph) {
		switch (operation.getType()) {
			case Create: create(operation.getEntity(), graph); return;
			case Modify: modify(operation.getEntity(), graph); return;
			case Delete: delete(operation.getEntity()); return;
		}
		throw new UnsupportedOperationException("Unsupported CRUD operation: " + operation.getType());
	}

	/**
	 * Counts entities matched by provided {@code selector}.
	 *
	 * @param selector selector to filter entities
	 *
	 * @return number of entities selected
	 */
	int count(S selector);

	/**
	 * Retrieves ALL entities matched by provided {@code selector}.
	 *
	 * @param selector selector to filter entities
	 * @param graph fields graph to resolve
	 *
	 * @return stream of entities selected
	 */
	Stream<C> query(S selector, FieldGraph<F> graph);

	/**
	 * <p>Invokes {@link #query}, collects whole stream into a {@link List} and closes the stream.</p>
	 *
	 * @param selector selector to filter entities
	 * @param graph fields graph to resolve
	 *
	 * @return list of entities selected
	 */
	default List<C> list(S selector, FieldGraph<F> graph) {
		try (Stream<C> stream = query(selector, graph)) {
			return stream.collect(Collectors.toList());
		}
	}

}
