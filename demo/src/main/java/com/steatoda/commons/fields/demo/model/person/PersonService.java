package com.steatoda.commons.fields.demo.model.person;

import com.steatoda.commons.fields.service.crud.CRUDFieldsService;

public interface PersonService extends CRUDFieldsService<String, Person, Person.Field, Void> {

	@Override
	default Person instance() {
		return new Person();
	}

}
