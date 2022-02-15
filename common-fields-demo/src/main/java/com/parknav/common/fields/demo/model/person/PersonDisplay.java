package com.parknav.common.fields.demo.model.person;

import com.parknav.common.fields.FieldGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * (Dummy, no-op) presenter that displays Person's names.
 */
public class PersonDisplay {

	public static final FieldGraph<Person.Field> Graph = FieldGraph.Builder.of(Person.Field.class)
		.add(Person.Field.name)
		.build();

	public PersonDisplay() {

		// service that can fetch missing fields
		personService = new PersonDemoService();

	}

	public Person getValue() {

		return person;

	}
	
	public void setValue(Person person) {
	
		this.person = person;

		// for demo purposes, log missing graph
		FieldGraph<Person.Field> missingGraph = person.getMissingGraph(Graph);
		if (!missingGraph.isEmpty())
			Log.info("Got value ({}) with some fields missing: {}", person, missingGraph);

		person.extend(Graph, personService);

		// now we are sure we have all fields we need

		Log.info("Got value with all fields I need: id={}; name={}", person.getId(), person.getName());
		
	}

	private static final Logger Log = LoggerFactory.getLogger(PersonDisplay.class);

	private final PersonService personService;
	
	private Person person;
	
}
