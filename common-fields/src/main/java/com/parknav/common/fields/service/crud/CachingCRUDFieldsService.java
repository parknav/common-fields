package com.parknav.common.fields.service.crud;

import com.parknav.common.fields.FieldEnum;
import com.parknav.common.fields.FieldGraph;
import com.parknav.common.fields.FieldsEntityCache;
import com.parknav.common.fields.HasEntityFields;

import java.util.stream.Stream;

/**
 * <p>{@link CRUDFieldsService} implementation that caches objects incrementally.</p>
 *
 * <p>Populates cache in every method, but uses it only in {@link #construct(Object, FieldGraph)} and {@link #get(Object, FieldGraph)}.
 * Causes very little overhead over {@code service} itself.</p>

 * @param <I> ID type
 * @param <C> concrete implementation of class implementing {@link HasEntityFields}
 * @param <F> field type
 * @param <S> selector used to filter values
 *
 * @see CRUDFieldsService
 * @see FieldsEntityCache
 */
public class CachingCRUDFieldsService<I, C extends HasEntityFields<I, C, F>, F extends Enum<F> & FieldEnum, S> extends DelegatingCRUDFieldsService<I, C, F, S> {

	/**
	 * @param cache entity cache
	 * @param service service that can resolve entities
	 */
	public CachingCRUDFieldsService(FieldsEntityCache<I, C, F> cache, CRUDFieldsService<I, C, F, S> service) {
		super(service);
		this.cache = cache;
		this.service = service;
	}

	@Override
	public C construct(I id, FieldGraph<F> graph) {
		if (graph.isEmpty())
			return service.construct(id, graph);
		return get(id, graph);
	}

	@Override
	public C get(I id, FieldGraph<F> graph) {
		return cache.get(id, graph, service);
	}

	@Override
	public Stream<C> query(S selector, FieldGraph<F> graph) {
		return service.query(selector, graph).peek(cache::merge);
	}

	@Override
	public void create(C entity, FieldGraph<F> graph) {
		service.create(entity, graph);
		cache.put(entity);
	}

	@Override
	public void modify(C entity, C patch, FieldGraph<F> graph) {
		cache.clear(entity);
		service.modify(entity, patch, graph);
		cache.put(entity);
	}

	@Override
	public void delete(C entity) {
		cache.clear(entity);
		service.delete(entity);
	}

	private final FieldsEntityCache<I, C, F> cache;
	private final CRUDFieldsService<I, C, F, S> service;

}
