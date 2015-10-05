package org.flyboy.vertx.factory;

import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.impl.verticle.CompilingClassLoader;
import io.vertx.core.spi.VerticleFactory;

/**
 * this factory returns Verticles as ApplicationScoped CDI beans.
 * 
 * @author jfraney
 *
 */
public class BeanVerticleFactory implements VerticleFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(BeanVerticleFactory.class);
	@Override
	public String prefix() {
		return "bean";
	}

	@Override
	public Verticle createVerticle(String verticleName, ClassLoader classLoader) throws Exception {
		verticleName = VerticleFactory.removePrefix(verticleName);
		ClassLoader cl = classLoader;
		
		Class<?> clazz;
		if (verticleName.endsWith(".java")) {
			CompilingClassLoader compilingLoader = new CompilingClassLoader(classLoader, verticleName);
			verticleName= compilingLoader.resolveMainClassName();
			cl = compilingLoader;
		}
		clazz = cl.loadClass(verticleName);
		return resolveBean(clazz);
	}

	private Verticle resolveBean(Class<?> clazz) throws InstantiationException, IllegalAccessException {
		Verticle vertical = (Verticle) BeanProvider.getContextualReference(clazz);
		LOGGER.debug("resolved={}, verticle={}", clazz, vertical);
		return vertical;
	}

	@Override
	public void init(Vertx vertx) {
		CdiContainer cdiContainer = CdiContainerLoader.getCdiContainer();
		cdiContainer.boot();
	}
	
	

}
