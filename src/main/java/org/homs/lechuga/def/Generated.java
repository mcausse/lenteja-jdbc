package org.homs.lechuga.def;

import org.homs.lechuga.def.generator.Generator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Generated {

    Class<? extends Generator> value();

    String[] args() default {};
}