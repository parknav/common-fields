package com.parknav.common.fields.demo.model.marina;

import org.apache.commons.lang3.StringUtils;

import com.parknav.common.fields.FieldGraph;

public class MarinaValidator {

	public static final FieldGraph<Marina.Field> Graph = FieldGraph.Builder.of(Marina.Field.class)
		.add(Marina.Field.name)
		.build();

	public void validate(Marina marina) {

		if (StringUtils.isBlank(marina.getIfPresent(marina::getName, Marina.Field.name)))
			throw new RuntimeException("Name is mandatory");
	
	}

}
