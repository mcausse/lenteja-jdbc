package org.homs.lechuga.entity.anno;

import org.homs.lechuga.entity.generator.Generator;

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