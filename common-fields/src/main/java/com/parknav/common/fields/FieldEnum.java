package com.parknav.common.fields;

/** Interface each enum that describes object's fields should implement */
public interface FieldEnum {

	/**
	 * For fields that describes subobjects with their own fields, this method returns subobject's {@link FieldEnum} class
	 *
	 * @param <F> Field enum describing field type for subobject this field references
	 *
	 * @return this field subobject's {@link FieldEnum} class
	 */
	<F extends Enum<F> & FieldEnum> Class<F> getFieldsClass();
	
}
