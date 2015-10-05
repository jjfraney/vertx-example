package org.flyboy.vertx.example;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

/**
 * derived from vert.x core-examples, version 3.0.0.
 * 
 * @author jfraney
 *
 */
public class MainVerticle extends AbstractVerticle {
	private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

	// Convenience method so you can run it in your IDE
	public static void main(String[] args) {
		LOGGER.info("defining verticles.");
		Consumer<Vertx> runner = vertx -> {
			try {
				vertx.deployVerticle("beans:" + MainVerticle.class.getName());
			} catch (Throwable t) {
				t.printStackTrace();
			}
		};
		LOGGER.info("starting vertx");
		Vertx vertx = Vertx.vertx();
		runner.accept(vertx);
	}

	@Override
	public void start() throws Exception {
		LOGGER.info("Running");
		
		vertx.deployVerticle("bean:org.flyboy.vertx.example.WorkerVerticle", new DeploymentOptions().setWorker(true), result -> {
			if(result.succeeded()) {
				LOGGER.info("Successful worker deploy, sending a message");
			    vertx.eventBus().send(
			            "sample.data",
			            "hello vert.x",
			            r -> {
			              LOGGER.info("Receiving reply: {}", r.result().body());
			            }
			        );
			} else {
				LOGGER.error("Unable to deploy worker: {}", result.cause().getMessage());
			}
		});
		
		vertx.setTimer(500, h -> {
			LOGGER.info("completed, and exiting");
			System.exit(0);
		});
	}
	
}