package com.steatoda.commons.fields.demo.model.person;

import com.steatoda.commons.fields.service.async.FieldsAsyncService;

public interface PersonAsyncService extends FieldsAsyncService<String, Person, Person.Field> {

	@Override
	default Person instance() {
		return new Person();
	}

}
