package com.parknav.common.fields.service.crud;

import com.parknav.common.fields.HasEntityFields;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class CRUDBatchBuilder<T extends HasEntityFields<?, ?, ?>> {

	@FunctionalInterface
	public interface OperationFactory<T extends HasEntityFields<?, ?, ?>> {
		CRUDOperation<T> get(CRUDOperation.Type type, T entity);
	}

	protected CRUDBatchBuilder(OperationFactory<T> operationFactory) {
		this.operationFactory = operationFactory;
	}

	/** Adds new {@link CRUDOperation} that should be performed in a batch */
	public CRUDBatchBuilder<T> add(CRUDOperation<T> operation) { this.operations.add(operation); return this; }
	@SafeVarargs
	public final CRUDBatchBuilder<T> add(CRUDOperation<T>... operations) { add(Arrays.asList(operations)); return this; }
	public final CRUDBatchBuilder<T> add(Collection<CRUDOperation<T>> operations) { this.operations.addAll(operations); return this; }

	/** Adds new {@link CRUDOperation} for creating new entity */
	public CRUDBatchBuilder<T> addCreate(T entity) { add(operationFactory.get(CRUDOperation.Type.Create, entity)); return this; }
	@SafeVarargs
	public final CRUDBatchBuilder<T> addCreate(T... entities) { Arrays.asList(entities).forEach(this::addCreate); return this; }
	public final CRUDBatchBuilder<T> addCreate(Collection<T> entities) { entities.forEach(this::addCreate); return this; }

	/** Adds new {@link CRUDOperation} for modifying existing entity */
	public CRUDBatchBuilder<T> addModify(T entity) { add(operationFactory.get(CRUDOperation.Type.Modify, entity)); return this; }
	@SafeVarargs
	public final CRUDBatchBuilder<T> addModify(T... entities) { Arrays.asList(entities).forEach(this::addModify); return this; }
	public final CRUDBatchBuilder<T> addModify(Collection<T> entities) { entities.forEach(this::addModify); return this; }

	/** Adds new {@link CRUDOperation} for deleting existing entity */
	public CRUDBatchBuilder<T> addDelete(T entity) { add(operationFactory.get(CRUDOperation.Type.Delete, entity)); return this; }
	@SafeVarargs
	public final CRUDBatchBuilder<T> addDelete(T... entities) { Arrays.asList(entities).forEach(this::addDelete); return this; }
	public final CRUDBatchBuilder<T> addDelete(Collection<T> entities) { entities.forEach(this::addDelete); return this; }

	public boolean isEmpty() {
		return operations.isEmpty();
	}

	public int size() {
		return operations.size();
	}

	public List<CRUDOperation<T>> build() {
		return Collections.unmodifiableList(operations);
	}

	private final OperationFactory<T> operationFactory;

	private final List<CRUDOperation<T>> operations = new ArrayList<>();

}
