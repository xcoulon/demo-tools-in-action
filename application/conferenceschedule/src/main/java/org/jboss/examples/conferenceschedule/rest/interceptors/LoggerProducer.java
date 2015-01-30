package org.jboss.examples.conferenceschedule.rest.interceptors;


import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Slf4J {@link Logger} producer
 * @author Xavier Coulon
 *
 */
@ApplicationScoped
public class LoggerProducer {
	
	@Produces
    public Logger produceLog(final InjectionPoint injectionPoint) {
        return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

}
