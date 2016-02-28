package org.rgadmin.server

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.ContextHandler
import org.eclipse.jetty.server.handler.ContextHandlerCollection
import org.eclipse.jetty.server.handler.ResourceHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.eclipse.jetty.util.resource.Resource
import org.eclipse.jetty.util.resource.ResourceCollection
import org.eclipse.jetty.webapp.WebAppContext
import javax.servlet.MultipartConfigElement
import javax.servlet.Servlet

class RgServer(val port: Int, api: Servlet): Server(port) {

    init {
        val resourceCollection = ResourceCollection()
        resourceCollection.resources = arrayOf(
                webjar("jquery", "2.2.0"),
                webjar("bootstrap", "3.3.6"),
                webjar("font-awesome", "4.5.0")
        )
        val capHandler = ContextHandler();
        capHandler.setInitParameter("useFileMappedBuffer", "false");
        capHandler.setContextPath("/jslibs");
        val resHandler = ResourceHandler();
        resHandler.setBaseResource(resourceCollection);
        capHandler.handler = resHandler;

        val webapp = WebAppContext();
        webapp.getInitParams().put("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
        webapp.setContextPath("/");
        webapp.setWar("target/classes");
        val holder = ServletHolder(api)
        holder.registration.setMultipartConfig(MultipartConfigElement(""))
        webapp.addServlet(holder, "/api/v1/*")
        val contexts = ContextHandlerCollection();
        contexts.setHandlers(arrayOf(webapp,capHandler));

        server.setHandler(contexts);
        server.start();
    }
}

fun webjar(webjar: String, version: String): Resource {
    return Resource.newClassPathResource("META-INF/resources/webjars/$webjar/$version/");
}