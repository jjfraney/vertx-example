# Example: Vert.x wraps CDI container

This is sample of extending vert.x with a VerticleFactory that embeds a CDI container.  The BeansVerticleFactory bootstraps the CDI container, and, then resolves Verticle instances with it.

This enables CDI managed bean injection into Verticles.

This example has two Verticles, a Main and a Worker.  The Worker injects a managed bean.  When a message from the event bus is delivered to the Worker, it accesses the bean.
