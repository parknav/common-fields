package com.steatoda.commons.fields.model.carrier;

import com.steatoda.commons.fields.FieldGraph;
import com.steatoda.commons.fields.service.FieldsService;

public interface CarrierService extends FieldsService<String, Carrier, Carrier.Field> {

	@Override
	default Carrier instance() {
		return new Carrier();
	}

	@Override
	Carrier get(String id, FieldGraph<Carrier.Field> fields);

}
