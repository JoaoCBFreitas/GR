
package com.test.jetty;

//import java.io.File;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletHandler;

/**
 * JettyTestServer
 */
public class JettyTestServer {
    private Server server;

    public void start() throws Exception {
        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        //porta a onde irá estar à escuta o servidor
        connector.setPort(8080);
        server.setConnectors(new Connector[]{connector});

        ResourceHandler resourceHandler = new ResourceHandler();
        ServletHandler handler = new ServletHandler();

        //definir que ficheiro será mostrado quando visitar o endereço http://localhost:porta/
        resourceHandler.setWelcomeFiles(new String[]{"index.html"});
        resourceHandler.setDirectoriesListed(true);
        //Local a partir da qual serão procurados os ficheiros.
        resourceHandler.setResourceBase(".");
        //caminho a onde estará à escuta o servlet do tipo BlockingServlet
        handler.addServletWithMapping(BlockingServlet.class, "/get_rest");

        HandlerList handlerList = new HandlerList();
        handlerList.setHandlers(new Handler[]{resourceHandler, handler});

        server.setHandler(handlerList);
        server.start();
    }

    public static void main(String[] args) throws Exception {
        JettyTestServer jettyTestServer = new JettyTestServer();
        jettyTestServer.start();
    }

}