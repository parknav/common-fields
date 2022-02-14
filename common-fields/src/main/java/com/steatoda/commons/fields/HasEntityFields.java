package com.steatoda.commons.fields;

import com.steatoda.commons.fields.service.FieldsService;
import com.steatoda.commons.fields.service.async.DelegatingFieldsServiceHandler;
import com.steatoda.commons.fields.service.async.FieldsAsyncService;
import com.steatoda.commons.fields.service.async.FieldsRequest;
import com.steatoda.commons.fields.service.async.FieldsServiceHandler;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Interface that gives implementing classes fields entity operations (like extending using service etc.).
 *
 * @param <I> ID type
 * @param <C> concrete implementation of class implementing this {@code HasFields}
 * @param <F> field type
 */
public interface HasEntityFields<I, C extends HasEntityFields<I, C, F>, F extends Enum<F> & FieldEnum> extends HasFields<C, F> {

	/**
	 * @return entity's unique ID
	 */
	I getId();

	/**
	 * Sets entity's unique ID.
	 *
	 * @param id entity's unique ID
	 */
	void setId(I id);

	/**
	 * Ensures entity has all requested fields, fetching missing ones using {@code resolver} if necessary.
	 * Descends to subobject and fetches their fields, too.
	 *
	 * @param graph field graph to set if missing
	 * @param resolver resolver which can provide entity with missing fields
	 *
	 * @return {@code true} if entity was extended, {@code false} if no extension was needed (nor performed)
	 *
	 * @throws EntityUnavailableException if missing (sub)fields can't be fetched from service
	 * 			(includes missing collection members)
	 */
	default boolean extend(FieldGraph<F> graph, Function<FieldGraph<F>, C> resolver) throws EntityUnavailableException {

		FieldGraph<F> missingGraph = getMissingGraph(graph);

		if (missingGraph.isEmpty())
			return false;

		C extension;
		try {
			extension = resolver.apply(missingGraph);
		} catch (Exception e) {
			throw new EntityUnavailableException(getId().toString(), e);
		}

		if (extension == null)
			throw new EntityUnavailableException(getId());

		_extend(extension, missingGraph);

		return true;

	}

	/**
	 * Ensures entity has all requested fields, fetching missing ones using {@code service} if necessary.
	 * Descends to subobject and fetches their fields, too.
	 *
	 * @param graph field graph to set if missing
	 * @param service service which can provide entity with missing fields
	 *
	 * @return {@code true} if entity was extended, {@code false} if no extension was needed (nor performed)
	 *
	 * @throws EntityUnavailableException if missing (sub)fields can't be fetched from service
	 * 			(includes missing collection members)
	 */
	default boolean extend(FieldGraph<F> graph, FieldsService<I, C, F> service) throws EntityUnavailableException {
		return extend(graph, missingGraph -> service.get(getId(), missingGraph));
	}

	/**
	 * Ensures entity has all requested fields, fetching missing ones <u>asynchronously</u> using {@code resolver} if necessary.
	 * Descends to subobject and fetches their fields, too.
	 *
	 * @param graph field graph to set if missing
	 * @param resolver resolver which can provide entity with missing fields
	 * @param handler asynchronous handler
	 *
	 * @return {@link FieldsRequest} describing this asynchronous operation
	 */
	@SuppressWarnings("unchecked")
	default FieldsRequest extend(FieldGraph<F> graph, BiFunction<FieldGraph<F>, FieldsServiceHandler<C>, FieldsRequest> resolver, FieldsServiceHandler<C> handler) {

		FieldGraph<F> missingGraph = getMissingGraph(graph);

		if (missingGraph.isEmpty()) {
			handler.onSuccess((C) this);
			handler.onFinish();
			handler.onDestroy();
			return null;
		}

		return resolver.apply(missingGraph, new DelegatingFieldsServiceHandler<C>(handler) {
			@Override
			public void onSuccess(C extension) {
				_extend(extension, graph);
				super.onSuccess((C) HasEntityFields.this);
			}
		});

	}

	/**
	 * Ensures entity has all requested fields, fetching missing ones <u>asynchronously</u> using {@code resolver} if necessary.
	 * Descends to subobject and fetches their fields, too.
	 *
	 * @param graph field graph to set if missing
	 * @param service asynchronous service which can provide entity with missing fields
	 * @param handler asynchronous handler
	 *
	 * @return {@link FieldsRequest} describing this asynchronous operation
	 */
	default FieldsRequest extend(FieldGraph<F> graph, FieldsAsyncService<I, C, F> service, FieldsServiceHandler<C> handler) {
		return extend(graph, (missingGraph, handler2) -> service.get(getId(), missingGraph, handler2), handler);
	}

}
