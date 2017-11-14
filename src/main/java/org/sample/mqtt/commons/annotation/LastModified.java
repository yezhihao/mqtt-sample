package org.sample.mqtt.commons.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Alan on 2017/2/22.
 */
@Documented
@Target({METHOD})
@Retention(RUNTIME)
public @interface LastModified {

}