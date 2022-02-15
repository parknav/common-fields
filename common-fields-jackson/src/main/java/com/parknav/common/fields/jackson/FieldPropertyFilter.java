package com.parknav.common.fields.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

import com.parknav.common.fields.FieldUnavailableException;
import com.parknav.common.fields.HasFields;

/**
 * <p>During serialization, filter only initialized fields. Useful when class uses
 * {@link HasFields#fieldGet} in getters to prevent getter invocation for missing fields.</p>
 * <p><b>Note:</b> for this filter to work, three things should be set:</p>
 * <ul>
 * 	<li>Filter must be registered on {@link com.fasterxml.jackson.databind.ObjectMapper} via <code>setFilterProvider(new SimpleFilterProvider().addFilter(FieldPropertyFilter.Name, new FieldPropertyFilter()))</code></li>
 *	<li>Each class should be annotated with <code>@JsonFilter(FieldPropertyFilter.Name)</code></li>
 *	<li>Optional: each getter should be annotated with <code>@FieldProperty(<i>&lt;field name&gt;</i>)</code>
 *		(if not, FieldPropertyFilter can be configured (via {@link #setIgnoreFieldUnavailableException}) to ignore
 *		{@link FieldUnavailableException}s during serialization
 *	</li>
 * </ul>
 */ 
public class FieldPropertyFilter extends SimpleBeanPropertyFilter {

	public static final String Name = "FieldPropertyFilter";

	/**
	 * @return should this filter ignore {@link FieldUnavailableException} or not
	 */
	public boolean getIgnoreFieldUnavailableException() {
		return ignoreFieldUnavailableException;
	}

	/**
	 * Configures wether this filter should ignore {@link FieldUnavailableException} or not
	 *
	 * @param ignoreFieldUnavailableException if set to {@code true} {@link FieldUnavailableException} will be ignored, otherwise filter will fail on it
	 *
	 * @return this
	 */
	public FieldPropertyFilter setIgnoreFieldUnavailableException(boolean ignoreFieldUnavailableException) {
		this.ignoreFieldUnavailableException = ignoreFieldUnavailableException;
		return this;
	}

	@Override
	public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer) throws Exception {

		try {
			if (include(pojo, writer))
				writer.serializeAsField(pojo, jgen, provider);
			else if (!jgen.canOmitFields())
				writer.serializeAsOmittedField(pojo, jgen, provider);
		} catch (Exception e) {
			if (ignoreFieldUnavailableException) {
				Throwable t = e;
				do {
					if (t instanceof FieldUnavailableException)
						return;	// skip this property
					t = t.getCause();
				} while (t != null);
			}
			throw e;
		}

	}
	
	protected boolean include(Object pojo, PropertyWriter writer) {

		if (!(pojo instanceof HasFields))
			return true;
		
		FieldProperty fieldProperty = writer.getAnnotation(FieldProperty.class);
		
		if (fieldProperty == null)
			return true;

		return ((HasFields<?, ?>) pojo).hasFieldsAsString(fieldProperty.value());
		
	}

	private boolean ignoreFieldUnavailableException = false;

}
