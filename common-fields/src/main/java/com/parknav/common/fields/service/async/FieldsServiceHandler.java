package com.parknav.common.fields.service.async;

/**
 * Handler of asynchronous service invocations.
 *
 * @param <T> result type
 */
@FunctionalInterface
public interface FieldsServiceHandler<T> {

	/**
	 * Called <b>before</b> remote async request, iff one is about to be made.
	 *
 	 * @param request {@link FieldsRequest} describing this asynchronous operation
	 */
	default void onPreRequest(FieldsRequest request) { /* no-op */ }

	/**
	 * Called <b>after</b> remote async request, iff one was made.
	 *
	 * @param request {@link FieldsRequest} describing this asynchronous operation
	 */
	default void onPostRequest(FieldsRequest request) { /* no-op */ }

	/**
	 * <p>Called after request is cancelled.</p>
	 * 
	 * <p>NOTE: this is final handler - no subsequent {@link #onFinish()} will be called.</p>
	 */
	default void onCancel() { /* no-op */ }

	/**
	 * Called after invocation results is retrieved.
	 *
	 * @param value result from performed operation
	 */
	void onSuccess(T value);

	/**
	 * <p>Called when request fails, but NOT when it is cancelled (for cancellation, see {@link #onCancel()}).</p>
	 */
	default void onFail() { /* no-op */ }
	
	/**
	 * <p>Called after request succeeds or fails, but NOT after it is cancelled (for cancellation, see {@link #onCancel()}).</p>
	 */ 
	default void onFinish() { /* no-op */ }

	/**
	 * <p>Called after request should be disposed (it was either cancelled or finished via succeeding or failure).</p>
	 */ 
	default void onDestroy() { /* no-op */ }

}
