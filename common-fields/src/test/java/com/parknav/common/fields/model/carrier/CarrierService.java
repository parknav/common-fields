package com.parknav.common.fields.model.carrier;

import com.parknav.common.fields.FieldGraph;
import com.parknav.common.fields.service.FieldsService;

public interface CarrierService extends FieldsService<String, Carrier, Carrier.Field> {

	@Override
	default Carrier instance() {
		return new Carrier();
	}

	@Override
	Carrier get(String id, FieldGraph<Carrier.Field> fields);

}
