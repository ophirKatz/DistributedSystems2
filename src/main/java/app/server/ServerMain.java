package app.server;

import app.server.leaderelection.main.LeaderElectionLauncher;
import app.server.leaderelection.nodes.ProcessNode;
import app.server.servers.ServerProcess;
import app.server.servers.jersey.JerseyContextServiceBinder;
import app.server.servers.jersey.resources.ContainerResource;
import app.server.servers.jersey.resources.ItemStorageResource;
import app.server.servers.jersey.resources.ShippingResource;
import org.apache.commons.cli.CommandLine;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

/**
 * Created by ophir on 10/01/18.
 */
public class ServerMain {

    private static String BASE_URI = "http://localhost:<port>/shipchain/";

    private static HttpServer buildHttpServer(int port) {
        // Assigning BASE_URI with root uri for specific port of server.
        BASE_URI = BASE_URI.replace("<port>", String.valueOf(port));

        // Creating a resource config to bind the resource classes with the http server.
        // Also binding [injecting] shared objects to services.
        final ResourceConfig rc = new ResourceConfig()
                .register(new JerseyContextServiceBinder())
                .register(ContainerResource.class)
                .register(ItemStorageResource.class)
                .register(ShippingResource.class)
                .packages(true, "app");

        // Creating the http server with the resource config and the jersey DI binding module.
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }


    public static void usage() {
        System.err.println("Usage : shipchain -start server -id <server-id> -port <server-port>");
        System.exit(0);
    }

    public static ServerProcess server;

    public static void main(CommandLine cmd) {
        try {
            // 0. Get arguments.
            if (!cmd.hasOption("id") || !cmd.hasOption("port")) {
                usage();
            }

            final int id = Integer.parseInt(cmd.getOptionValue("id"));
            final int httpPort = Integer.parseInt(cmd.getOptionValue("port"));

            // 1. Run leader election.
            ProcessNode processNode = LeaderElectionLauncher.launch(id);

            // 2. Open the server, connect to ServerGroup.
            ServerMain.server = new ServerProcess(processNode.getNodePath(), processNode.getLeaderNodePath());

            // 3. Connect [inject] the server to ProcessNode.
            processNode.setServer(ServerMain.server);

            // 4. Open Http server and connect [inject] the server to Services.
            final HttpServer httpServer = buildHttpServer(httpPort);
            System.out.println("Starting HTTP server on address = " + BASE_URI);
            httpServer.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(9);
        } finally {
            if (ServerMain.server != null) ServerMain.server.leaveGroup();
        }
    }
}
