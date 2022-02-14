package com.steatoda.commons.fields.jackson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation to associate getters and setters with certain fields. See {@link FieldPropertyFilter} for example usage.
 *
 * @see FieldPropertyFilter
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD })
public @interface FieldProperty {

	String value();

}
