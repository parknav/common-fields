package com.parknav.common.fields.demo.model.person;

import com.parknav.common.fields.FieldGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** (Dummy, no-op) notifier that sends email to users */
public class Notifier {

	public static final FieldGraph<Person.Field> Graph = FieldGraph.Builder.of(Person.Field.class)
		.add(Person.Field.email)
		.build();

	public Notifier() {
		
		// service that can fetch missing fields
		personService = new PersonDemoService();

	}

	public void sendEmail(Person person) {

		// for demo purposes, log missing graph
		FieldGraph<Person.Field> missingGraph = person.getMissingGraph(Graph);
		if (!missingGraph.isEmpty())
			Log.info("Got param ({}) without email, fetching...", person);

		person.extend(Graph, personService);

		// NOTE if this was real notifier, here we would open SMTP connection and do Real Stuff
		Log.info("Sending email to {}", person.getEmail());
		
	}

	private static final Logger Log = LoggerFactory.getLogger(Notifier.class);

	private final PersonService personService;
	
}
