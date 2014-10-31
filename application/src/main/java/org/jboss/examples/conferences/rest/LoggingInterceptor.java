package org.jboss.examples.conferences.rest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import org.slf4j.Logger;

/**
 * A {@link ReaderInterceptor} and {@link WriterInterceptor} that logs the incoming requests and responses.
 * @author xcoulon
 *
 * @see http://stackoverflow.com/questions/25332103/capture-response-payload-in-jax-rs-filter
 */
@LoggingBinding
@Provider
public class LoggingInterceptor implements ReaderInterceptor, WriterInterceptor {

	@Context
	private HttpServletRequest request;
	
	@Context
	private HttpServletResponse response;
	
	@Inject
	private Logger logger;
	
	@Override
	public Object aroundReadFrom(final ReaderInterceptorContext context)
			throws IOException, WebApplicationException {
		// reset before further proceeding.
		if (logger.isInfoEnabled()) {
			final String contentLengthHeader = context.getHeaders().getFirst("Content-Length");
			if(contentLengthHeader != null) {
				final InputStream originalStream = context.getInputStream();
				final byte[] content = new byte[Integer.parseInt(contentLengthHeader)];
				originalStream.read(content);
				if(content.length > 0) {
					logger.info("\nRequest: {} {}\n{}", request.getMethod(), request.getRequestURI(), new String(content, "UTF-8"));
				} else{
					logger.info("\nRequest: {} {}", request.getMethod(), request.getRequestURI());
				}
				final ByteArrayInputStream bais = new ByteArrayInputStream(content);
				context.setInputStream(bais);
			}
		}
		return context.proceed();
	}
	
	@Override
	public void aroundWriteTo(final WriterInterceptorContext context) throws IOException, WebApplicationException {
		if (logger.isInfoEnabled()) {
			final OutputStream originalStream = context.getOutputStream();
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			context.setOutputStream(baos);
			try {
				context.proceed();
			} finally {
				logger.info("Response: {}\n{}", response.getStatus(), baos.toString("UTF-8"));
				baos.writeTo(originalStream);
				baos.close();
				context.setOutputStream(originalStream);
			}
		} else {
			context.proceed();
		}
	}


}