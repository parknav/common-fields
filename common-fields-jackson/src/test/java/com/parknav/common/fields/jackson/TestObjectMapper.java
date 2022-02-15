package com.parknav.common.fields.jackson;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.parknav.common.fields.jackson.model.JacksonFieldsEntity;

public class TestObjectMapper extends ObjectMapper {

	public TestObjectMapper() {

		super();

		configure(SerializationFeature.INDENT_OUTPUT, true);

		disable(MapperFeature.AUTO_DETECT_CREATORS);
		disable(MapperFeature.AUTO_DETECT_FIELDS);
		disable(MapperFeature.AUTO_DETECT_GETTERS);
		disable(MapperFeature.AUTO_DETECT_IS_GETTERS);

		registerModule(new SimpleModule()
						.setSerializerModifier(new FieldsSerializerModifier(JacksonFieldsEntity.JsonPropertyId))
		);

		setFilterProvider(new SimpleFilterProvider().addFilter(FieldPropertyFilter.Name, new FieldPropertyFilter().setIgnoreFieldUnavailableException(true)));

	}

}