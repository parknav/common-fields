package com.steatoda.commons.fields.demo.model.person;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.steatoda.commons.fields.FieldGraph;
import com.steatoda.commons.fields.demo.model.person.Person.Field;
import com.steatoda.commons.fields.service.async.FieldsRequest;
import com.steatoda.commons.fields.service.async.FieldsServiceHandler;

/** Operates on sample persons from memory store */
public class PersonDemoAsyncService implements PersonAsyncService {

	public PersonDemoAsyncService() {
		delegate = new PersonDemoService();
	}

	@Override
	public FieldsRequest get(String id, FieldGraph<Field> graph, FieldsServiceHandler<Person> handler) {
		
		AtomicReference<FieldsRequest> requestRef = new AtomicReference<>();
		
		// simulate remote request with timer
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				try {
					Person person = delegate.get(id, graph);
					handler.onSuccess(person);
				} catch (Exception e) {
					handler.onFail();
				} finally {
					handler.onPostRequest(requestRef.get());
					try {
						handler.onFinish();
					} finally {
						handler.onDestroy();
					}
				}
			}
		};
		
		timer.schedule(task, 100);	// simulate 100 ms pause
		
		FieldsRequest request = () -> {
			try {
				task.cancel();
			} finally {
				handler.onCancel();
				handler.onDestroy();
			}
		};
		
		requestRef.set(request);
		
		handler.onPreRequest(request);
		
		return request;
		
	}

	@SuppressWarnings("unused")
	private static final Logger Log = LoggerFactory.getLogger(PersonDemoAsyncService.class);

	private final PersonService delegate;
	private final Timer timer = new Timer(true);

}
