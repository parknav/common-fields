package com.steatoda.commons.fields.demo.model.marina;

import com.google.common.eventbus.Subscribe;
import com.steatoda.commons.fields.FieldGraph;
import com.steatoda.commons.fields.demo.DemoEventBus;
import com.steatoda.commons.fields.demo.model.person.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Virtual presenter that displays marina names and their managers.
 * Updates itself on detected change.
 * */
public class MarinaDetailsDisplay implements AutoCloseable {

	public static final FieldGraph<Marina.Field> Graph = FieldGraph.Builder.of(Marina.Field.class)
		.add(Marina.Field.name)
		.add(Marina.Field.manager, FieldGraph.Builder.of(Person.Field.class)
			.add(Person.Field.name)
			.build()
		)
		.build();

	public MarinaDetailsDisplay() {

		// service that can fetch missing fields
		marinaService = new MarinaDemoService();

		// register for MarinaModifyEvent<s>
		DemoEventBus.get().register(this);
		
	}

	public Marina getValue() {

		return marina;

	}
	
	public void setValue(Marina marina) {
	
		this.marina = marina;

		// for demo purposes, log missing graph
		FieldGraph<Marina.Field> missingGraph = marina.getMissingGraph(Graph);
		if (!missingGraph.isEmpty())
			Log.info("Got value ({}) with some fields missing: {}", marina, missingGraph);

		marina.extend(Graph, marinaService);

		// now we are sure we have all fields we need

		Log.info("Got value with all fields I need: id={}; name={}; manager={}", marina.getId(), marina.getName(), marina.getManager().getName());
		
	}

	@Override
	public void close() {
		DemoEventBus.get().unregister(this);
	}
	
	@Subscribe
	void onMarinaModify(MarinaModifyEvent event) {

		if (marina == null)
			return;	// we are not initialized yet
		
		if (!marina.equals(event.getMarina()))
			return;	// we are displaying different object

		setValue(event.getMarina());

	}

	private static final Logger Log = LoggerFactory.getLogger(MarinaDetailsDisplay.class);

	private final MarinaService marinaService;
	
	private Marina marina;
	
}
