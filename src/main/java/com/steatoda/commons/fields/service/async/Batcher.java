package com.steatoda.commons.fields.service.async;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.steatoda.commons.fields.FieldEnum;
import com.steatoda.commons.fields.FieldGraph;
import com.steatoda.commons.fields.HasEntityFields;

/**
 * Batches operations on one {@link FieldsAsyncService}.
 *
 * @param <I> ID type
 * @param <C> concrete implementation of class implementing {@link HasEntityFields}
 * @param <F> field type
 *
 * @see FieldsAsyncService
 */
public abstract class Batcher<I, C extends HasEntityFields<I, C, F>, F extends Enum<F> & FieldEnum> implements FieldsAsyncService<I, C, F> {

	// TODO borrow instances from extend-jobs for get-jobs?

	/** Represents retrieval of one distinct {@link HasEntityFields} entity. There may be multiple instances of that same entity
	 * in which case union of missing fields will be fetched (once) and all handlers will be notified with that one entity.*/
	private class Job {
		
		public Job(I id) {
			this.id = id;
		}
		
		public void queue(FieldGraph<F> graph, FieldsServiceHandler<C> handler) {
			if (graphBuilder == null)
				graphBuilder = FieldGraph.Builder.of(graph.getDeclaringClass());
			graphBuilder.add(graph);
			handlers.add(handler);
		}
		
		private final I id;
		private final Queue<FieldsServiceHandler<C>> handlers = new LinkedList<>();
		private FieldGraph.Builder<F> graphBuilder = null;
		private FieldsRequest request = null;
		private boolean finished = false;
		
	}

	/**
	 * Constructs new instance using {@code service} as backing service.
	 *
	 * @param service backing service to delegate calls to
	 */
	public Batcher(FieldsAsyncService<I, C, F> service) {
		this.service = service;
	}

	@Override
	public C instance() {
		return service.instance();
	}
	
	@Override
	synchronized public FieldsRequest get(I id, FieldGraph<F> graph, FieldsServiceHandler<C> handler) {
		
		Job job = Jobs.get(id);
		if (job == null)
			Jobs.put(id, job = new Job(id));

		job.queue(graph, handler);
		
		final Job finalJob = job;
		FieldsRequest request = new FieldsRequest() {
			@Override
			public void cancel() {
				// calling cancel() on finished job should not trigger another onCancel
				if (finalJob.finished)
					return;
				// can't cancel (cumulative) request, but we can remove handler from list of to-be-notified handlers
				if (!finalJob.handlers.remove(handler))
					return;	// not found, probably already cancelled
				handler.onCancel();
				handler.onDestroy();
				// if there are no more handlers, cancel (cumulative) request
				if (finalJob.handlers.isEmpty() && finalJob.request != null)
					finalJob.request.cancel();
			}
		};
		
		handler.onPreRequest(request);

		return request;

	}

	/** Executes queued operations. */
	public void run() {

		List<Job> batch = pullJobsBatch();

		for (Job job : batch) {
			
			if (job.handlers.isEmpty())
				continue;	// all handlers are cancelled
			
			FieldGraph<F> graph = job.graphBuilder.build();

			job.request = service.get(job.id, graph, new FieldsServiceHandler<C>() {
				// NOTE: we already called onPreRequest(FieldsRequest) when queuing this instance, so don't call again
				@Override
				public void onPostRequest(FieldsRequest request) {
					for (FieldsServiceHandler<C> handler : job.handlers)
						handler.onPostRequest(request);
				}
				@Override
				public void onSuccess(C entity) {
					job.finished = true;	// from now on, calling cancel() should be no-op
					for (FieldsServiceHandler<C> handler : job.handlers)
						handler.onSuccess(entity);
				}
				@Override
				public void onCancel() {
					job.finished = true;	// from now on, calling cancel() should be no-op
					for (FieldsServiceHandler<C> handler : job.handlers)
						handler.onCancel();
				}
				@Override
				public void onFail() {
					job.finished = true;	// from now on, calling cancel() should be no-op
					for (FieldsServiceHandler<C> handler : job.handlers)
						handler.onFail();
				}
				@Override
				public void onFinish() {
					for (FieldsServiceHandler<C> handler : job.handlers)
						handler.onFinish();
				}
				@Override
				public void onDestroy() {
					for (FieldsServiceHandler<C> handler : job.handlers)
						handler.onDestroy();
				}
			});
				
		}
		
	}

	synchronized private List<Job> pullJobsBatch() {
		
		List<Job> batch = new ArrayList<>(Jobs.values());
		
		Jobs.clear();
		
		return batch;
		
	}

	private final FieldsAsyncService<I, C, F> service;

	private final Map<I, Job> Jobs = new HashMap<>();

}
