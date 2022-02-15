package com.parknav.common.fields.jackson.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.parknav.common.fields.FieldEnum;
import com.parknav.common.fields.FieldsEntity;
import com.parknav.common.fields.jackson.FieldPropertyFilter;

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
