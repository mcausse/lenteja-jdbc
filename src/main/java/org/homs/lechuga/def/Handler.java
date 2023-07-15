package org.homs.lechuga.def;

import org.homs.lechuga.def.handlers.ColumnHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Handler {

    Class<? extends ColumnHandler> value();

    String[] args() default {};
}