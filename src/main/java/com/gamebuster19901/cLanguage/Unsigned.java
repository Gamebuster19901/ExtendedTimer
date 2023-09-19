package com.gamebuster19901.cLanguage;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.RECORD_COMPONENT;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.TYPE_PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Indicates that the value annotated is expected to be unsigned
 * 
 * </p>If both @Signed and @Unsigned are both annotated to the value, 
 * then the signage is dependent on the underlying implementation.
 * 
 * @author gamebuster
 *
 */

@Retention(RUNTIME)
@Target({ TYPE, FIELD, METHOD, PARAMETER, LOCAL_VARIABLE, TYPE_PARAMETER, RECORD_COMPONENT })
public @interface Unsigned {}
