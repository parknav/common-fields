package com.parknav.common.fields.demo.model.person;

import com.parknav.common.fields.service.crud.CRUDFieldsService;

public interface PersonService extends CRUDFieldsService<String, Person, Person.Field, Void> {

	@Override
	default Person instance() {
		return new Person();
	}

}
