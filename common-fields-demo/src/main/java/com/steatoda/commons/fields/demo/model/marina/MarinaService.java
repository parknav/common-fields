package com.steatoda.commons.fields.demo.model.marina;

import com.steatoda.commons.fields.service.crud.CRUDFieldsService;

public interface MarinaService extends CRUDFieldsService<String, Marina, Marina.Field, Void> {

	@Override
	default Marina instance() {
		return new Marina();
	}

}
