package com.steatoda.commons.fields;

import java.util.EnumSet;
import java.util.Set;

/**
 * Vanilla implementation of {@link HasFields} interface.
 * @param <C> concrete implementation of FieldsObject
 * @param <F> field type
 */
public abstract class FieldsObject<C extends HasFields<C, F>, F extends Enum<F> & FieldEnum> implements HasFields<C, F>, Cloneable {

	protected FieldsObject(Class<F> fieldsClass) {
		this.fieldsClass = fieldsClass;
		fields = EnumSet.noneOf(fieldsClass);
	}
	
	@Override
	public Class<F> getFieldsClass() { return fieldsClass; }

	@Override
	public Set<F> getFields() { return fields; }
	
	@Override
	public void setFields(Set<F> fields) { this.fields = fields; }

	//@Override	// GWT complains
	@SuppressWarnings("MethodDoesntCallSuperMethod")
	public C clone() {
		return cloneAll();
	}
	
	private final Class<F> fieldsClass;
	
	private Set<F> fields;
	
}
