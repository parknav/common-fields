package com.steatoda.commons.fields;

/**
 * Editors that work with objects which support fields can implement this interface to provide caller with information
 * exactly which fields they operate on.
 *
 * @param <F> Field enum
 */
public interface FieldAwareEditor<F extends Enum<F> & FieldEnum> {

	/**
	 * @return complete fields graph that this editor needs to operate properly
	 */
	FieldGraph<F> getGraph();

	/**
	 * @return fields graph that editor modifies
	 */
	FieldGraph<F> getEditableGraph();
	
}
