package com.parknav.common.fields.demo.model.person;

import com.parknav.common.fields.service.async.FieldsAsyncService;

public interface PersonAsyncService extends FieldsAsyncService<String, Person, Person.Field> {

	@Override
	default Person instance() {
		return new Person();
	}

}
