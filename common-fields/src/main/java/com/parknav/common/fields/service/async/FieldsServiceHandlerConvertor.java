package com.parknav.common.fields.service.async;

/**
 * Converts {@link FieldsServiceHandler} from one type to another. Useful when delegating to handler of another type.
 *
 * @param <T> type returned by performed operation
 * @param <R> converted type passed to delegate handler
 */
public abstract class FieldsServiceHandlerConvertor<T, R> implements FieldsServiceHandler<T> {

	/**
	 * Constructs {@code FieldsServiceHandlerConvertor} using {@code delegate} as handler to forward method invocations to.
	 *
	 * @param delegate handler to forward method invocations to
	 */
	public FieldsServiceHandlerConvertor(FieldsServiceHandler<R> delegate) {
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
		delegate.onSuccess(convert(value));
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

	/**
	 * Converts value from {@code T} (result from invoked operation) to {@code R} (handled by delegating handler)
	 *
	 * @param value result from invoked operation
	 *
	 * @return converted value
	 */
	protected abstract R convert(T value);

	private final FieldsServiceHandler<R> delegate;
	
}
