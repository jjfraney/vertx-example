package org.flyboy.vertx.example;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.apache.deltaspike.cdise.api.ContextControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;

/**
 * This verticle asynchronously sends a message with the text
 * from an instance injected by cdi container.
 * <p>
 * Derived from example of worker verticle from vertx-examples core, v3.0.0
 * <\p>
 */

public class WorkerVerticle extends AbstractVerticle {
	private static final Logger LOGGER = LoggerFactory.getLogger(WorkerVerticle.class);
	
	@Inject
	private Counter counter;
	
	@Inject
	ContextControl ctxCtrl;
	
	@Override
	public void start() throws Exception {
		LOGGER.info("Starting.");

		vertx.eventBus().consumer("sample.data", message -> {
			
			ctxCtrl.startContext(RequestScoped.class);
		    
			String text = counter.text();
			
			LOGGER.info("sending reply: {}", text);
			String body = (String) message.body();
			message.reply(body.toString() + ": " + text);
			
			ctxCtrl.stopContext(RequestScoped.class);
		});
		LOGGER.info("ready for events");
	}
	
	@PostConstruct
	public void constructed() {
		LOGGER.info("After construction");
	}

}