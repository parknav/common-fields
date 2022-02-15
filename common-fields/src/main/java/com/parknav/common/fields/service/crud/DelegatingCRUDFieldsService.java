package com.parknav.common.fields.service.crud;

import java.util.List;
import java.util.Set;

import com.parknav.common.fields.FieldEnum;
import com.parknav.common.fields.FieldGraph;
import com.parknav.common.fields.HasEntityFields;

/**
 * {@link CRUDFieldsService} implementation that delegates all calls to another CRUDFieldsService.
 * Override and provide decorated functionality.
 *
 * @param <I> ID type
 * @param <C> concrete implementation of class implementing {@link HasEntityFields}
 * @param <F> field type
 * @param <S> selector used to filter values
 *
 * @see CRUDFieldsService
 */
public class DelegatingCRUDFieldsService<I, C extends HasEntityFields<I, C, F>, F extends Enum<F> & FieldEnum, S> implements CRUDFieldsService<I, C, F, S> {

	/**
	 * Constructs {@code DelegatingCRUDFieldsService} using {@code delegate} as delegate.
	 *
	 * @param delegate delegate service to forward calls to
	 */
	public DelegatingCRUDFieldsService(CRUDFieldsService<I, C, F, S> delegate) {
		this.delegate = delegate;
	}

	@Override
	public C instance() {
		return delegate.instance();
	}

	@Override
	public C get(I id, FieldGraph<F> graph) {
		return delegate.get(id, graph);
	}

	@Override
	public int count(S selector) {
		return delegate.count(selector);
	}

	@Override
	public List<C> list(S selector, FieldGraph<F> graph) {
		return delegate.list(selector, graph);
	}


	@Override
	public void create(C entity, FieldGraph<F> graph) {
		delegate.create(entity, graph);
	}

	@Override
	public void modify(C entity, C patch, FieldGraph<F> graph) {
		delegate.modify(entity, patch, graph);
	}

	@Override
	public void delete(C entity) {
		delegate.delete(entity);
	}

	@Override
	public List<C> getAllFieldValues(S selector, Set<F> fields) {
		return delegate.getAllFieldValues(selector, fields);
	}

	private final CRUDFieldsService<I, C, F, S> delegate;

}
