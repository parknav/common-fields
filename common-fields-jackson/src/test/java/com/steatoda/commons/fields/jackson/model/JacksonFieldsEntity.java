package com.steatoda.commons.fields.jackson.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.steatoda.commons.fields.FieldEnum;
import com.steatoda.commons.fields.FieldsEntity;
import com.steatoda.commons.fields.jackson.FieldPropertyFilter;

@JsonFilter(FieldPropertyFilter.Name)
public abstract class JacksonFieldsEntity<I, C extends JacksonFieldsEntity<I, C, F>, F extends Enum<F> & FieldEnum> extends FieldsEntity<I, C, F> {

	public static final String JsonPropertyId = "id";

	protected JacksonFieldsEntity(Class<F> fieldsClass) {
		super(fieldsClass);
	}

	@Override
	@JsonProperty(JsonPropertyId)
	public I getId() {
		return super.getId();
	}

}
