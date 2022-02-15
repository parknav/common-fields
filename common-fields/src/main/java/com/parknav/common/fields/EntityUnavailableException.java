package com.parknav.common.fields;

import java.util.Objects;

/**
 * Thrown when service can't find entity in backing store.
 */
public class EntityUnavailableException extends RuntimeException {

	public EntityUnavailableException(HasEntityFields<?, ?, ?> object) {
		this(object.getId());
	}
	
	public EntityUnavailableException(Object id) {
		this(id, null);
	}

	public EntityUnavailableException(Object id, Throwable cause) {
		super("Entity '" + Objects.toString(id, "<WITHOUT-ID>") + "' unavailable", cause);
		this.id = id;
	}

	public Object getEntityId() { return id; }

	private static final long serialVersionUID = 1L;

	private final Object id;
	
}
