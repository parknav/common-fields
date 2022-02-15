package com.parknav.common.fields.service.async;

/**
 * Wraps {@link FieldsServiceHandler} and delegates every call to it. Override and provide decorated functionality.
 *
 * @param <T> result type
 *
 * @see FieldsServiceHandler
 */
public class DelegatingFieldsServiceHandler<T> implements FieldsServiceHandler<T> {

	/**
	 * Constructs {@code DelegatingFieldsServiceHandler} using {@code delegate} as delegate.
	 *
	 * @param delegate delegate handler to forward calls to
	 */
	public DelegatingFieldsServiceHandler(FieldsServiceHandler<T> delegate) {
		this.delegate = delegate;
	}

	@Override
	public void onPreRequest(FieldsRequest request) {
		delegate.onPreRequest(request);
	}
	
	@Override
	public void onPostRequest(FieldsRequest request) {
		delegate.onPostRequest(request);
	}

	@Override
	public void onCancel() {
		delegate.onCancel();
	}

	@Override
	public void onSuccess(T value) {
		delegate.onSuccess(value);
	}

	@Override
	public void onFail() {
		delegate.onFail();
	}
	
	@Override
	public void onFinish() {
		delegate.onFinish();
	}

	@Override
	public void onDestroy() {
		delegate.onDestroy();
	}

	private final FieldsServiceHandler<T> delegate;
	
}
