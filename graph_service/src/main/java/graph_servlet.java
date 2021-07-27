import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;

import java.net.URI;
import java.net.URL;


public final class graph_servlet extends DefaultServlet {

    public static void display() throws Exception {
        final Server server = new default_server().build(8080);
        ServerConnector connector = new ServerConnector (server);
        server.addConnector(connector);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        ClassLoader cl = graph_servlet.class.getClassLoader();
        URL f = cl.getResource("static/index.html");

        if(f==null){
            throw new RuntimeException("resource not found");
        }

            URI webRootUri = f.toURI().resolve("./").normalize();
            context.setContextPath("/");
            context.setBaseResource(Resource.newResource(webRootUri));


        server.setHandler(context);

        ServletHolder holderPwd = new ServletHolder("default", graph_servlet.class);
        holderPwd.setInitParameter("dirAllowed","true");
        context.addServlet(holderPwd,"/");

        server.start();
        server.join();
    }
}
