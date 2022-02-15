package com.parknav.common.fields.demo.model.marina;

import com.parknav.common.fields.service.crud.CachingCRUDFieldsService;

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
