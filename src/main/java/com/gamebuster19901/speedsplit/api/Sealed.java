package com.gamebuster19901.speedsplit.api;

import static java.lang.annotation.ElementType.MODULE;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Used to indicate that the annotated element is for internal use only
 */
@Retention(RUNTIME)
@Target({ TYPE, PACKAGE, MODULE })
public @interface Sealed {}
