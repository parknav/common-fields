package com.parknav.common.fields.demo.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.parknav.common.fields.FieldEnum;
import com.parknav.common.fields.FieldsEntity;
import com.parknav.common.fields.jackson.FieldPropertyFilter;

@JsonFilter(FieldPropertyFilter.Name)
public abstract class DemoFieldsEntity<I, C extends DemoFieldsEntity<I, C, F>, F extends Enum<F> & FieldEnum> extends FieldsEntity<I, C, F> {

	public static final String JsonPropertyId = "id";

	protected DemoFieldsEntity(Class<F> fieldsClass) {
		super(fieldsClass);
	}

	@Override
	@JsonProperty(JsonPropertyId)
	public I getId() {
		return super.getId();
	}

}
