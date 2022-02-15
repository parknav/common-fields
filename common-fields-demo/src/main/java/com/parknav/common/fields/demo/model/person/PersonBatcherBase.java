package com.parknav.common.fields.demo.model.person;

import com.parknav.common.fields.service.async.Batcher;

/** Base class for all person batchers */
public class PersonBatcherBase extends Batcher<String, Person, Person.Field> {

	public PersonBatcherBase(PersonAsyncService service) {
		super(service);
	}

}
