package com.steatoda.commons.fields.demo.model.marina;

import com.google.common.eventbus.Subscribe;
import com.steatoda.commons.fields.FieldsEntityCache;
import com.steatoda.commons.fields.demo.DemoEventBus;

import java.util.EnumSet;
import java.util.Set;

public class MarinaCache extends FieldsEntityCache<String, Marina, Marina.Field> implements AutoCloseable {

	// Marina fields this cache caches
	public static final Set<Marina.Field> Fields = EnumSet.of(
		Marina.Field.name,
		Marina.Field.manager,
		// lets say that for some reason we don't want to cache coordinates for marinas
		//Marina.Field.latitude,
		//Marina.Field.longitude,
		Marina.Field.berths,
		Marina.Field.depths
	);

	public MarinaCache() {
		super(Fields);
		DemoEventBus.get().register(this);
	}

	@Override
	public void close() throws Exception {
		DemoEventBus.get().unregister(this);
	}

	@Subscribe
	void onMarinaModify(MarinaModifyEvent event) {
		clear(event.getMarina());
	}

	@Subscribe
	void onMarinaDelete(MarinaDeleteEvent event) {
		clear(event.getMarina());
	}

}
