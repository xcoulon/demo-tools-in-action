/**
 * 
 */
package org.jboss.examples.conferences.rest;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.ws.rs.NameBinding;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

/**
 * @author xcoulon
 *
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface LoggingBinding {

}
