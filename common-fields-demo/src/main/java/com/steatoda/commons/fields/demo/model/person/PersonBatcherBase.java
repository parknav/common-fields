package com.steatoda.commons.fields.demo.model.person;

import com.steatoda.commons.fields.service.async.Batcher;

/** Base class for all person batchers */
public class PersonBatcherBase extends Batcher<String, Person, Person.Field> {

	public PersonBatcherBase(PersonAsyncService service) {
		super(service);
	}

}
