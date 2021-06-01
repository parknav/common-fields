package com.steatoda.commons.fields.service.crud;

import com.steatoda.commons.fields.FieldEnum;
import com.steatoda.commons.fields.FieldGraph;
import com.steatoda.commons.fields.FieldsEntityCache;
import com.steatoda.commons.fields.HasEntityFields;

import java.util.List;

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
	public List<C> list(S selector, FieldGraph<F> graph) {
		List<C> entities = service.list(selector, graph);
		entities.forEach(cache::merge);
		return entities;
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
