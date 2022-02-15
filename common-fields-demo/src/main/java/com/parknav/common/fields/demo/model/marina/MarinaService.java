package com.parknav.common.fields.demo.model.marina;

import com.parknav.common.fields.service.crud.CRUDFieldsService;

public interface MarinaService extends CRUDFieldsService<String, Marina, Marina.Field, Void> {

	@Override
	default Marina instance() {
		return new Marina();
	}

}
