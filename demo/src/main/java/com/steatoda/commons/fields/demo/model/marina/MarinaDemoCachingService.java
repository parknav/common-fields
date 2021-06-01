package com.steatoda.commons.fields.demo.model.marina;

import com.steatoda.commons.fields.service.crud.CachingCRUDFieldsService;

public class MarinaDemoCachingService extends CachingCRUDFieldsService<String, Marina, Marina.Field, Void> implements MarinaService, AutoCloseable {

	public MarinaDemoCachingService() {
		this(new MarinaDemoService());
	}

	public MarinaDemoCachingService(MarinaService service) {
		super(Cache, service);
	}

	@Override
	public void close() throws Exception {
		Cache.close();
	}

	private static final MarinaCache Cache = new MarinaCache();

}
