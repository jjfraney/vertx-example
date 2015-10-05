package org.flyboy.vertx.example;

import javax.enterprise.context.RequestScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestScoped
public class Counter {
	private static final Logger LOGGER = LoggerFactory.getLogger(Counter.class);
	public Counter() {
		LOGGER.debug("counter ctor.");
	}
	public String text() {
		return "hello from counter";
	}
}
