package app.server;

import app.Utils;
import app.server.leaderelection.main.LeaderElectionLauncher;
import app.server.leaderelection.nodes.ProcessNode;
import app.server.servers.ServerProcess;
import app.server.servers.communication.NodeAddress;
import app.server.servers.jersey.JerseyContextServiceBinder;
import app.server.servers.jersey.model.AbstractTransaction;
import app.server.servers.jersey.resources.ContainerResource;
import app.server.servers.jersey.resources.ItemStorageResource;
import app.server.servers.jersey.resources.ShippingResource;
import app.server.servers.jersey.services.AbstractService;
import app.server.servers.jersey.services.ContainerService;
import app.server.servers.jersey.services.ShippingService;
import app.server.servers.jersey.services.StorageService;
import com.google.gson.Gson;
import org.apache.commons.cli.CommandLine;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

/**
 * Created by ophir on 10/01/18.
 */
public class ServerMain {

    private static String BASE_URI = "http://localhost:<port>/shipchain/";

    private static JerseyContextServiceBinder serviceBinder;

    public static ContainerService getContainerService() {
        return serviceBinder.getContainerService();
    }

    public static ShippingService getShippingService() {
        return serviceBinder.getShippingService();
    }

    public static StorageService getStorageService() {
        return serviceBinder.getStorageService();
    }

    public static AbstractService<? extends AbstractTransaction> getService() {
        return serviceBinder.getContainerService();
    }

    private static void startHttpServer(String port) {
        // Assigning BASE_URI with root uri for specific port of server.
        BASE_URI = BASE_URI.replace("<port>", port);

        // Creating a resource config to bind the resource classes with the http server.
        // Also binding [injecting] shared objects to services.
        ServerMain.serviceBinder = new JerseyContextServiceBinder();
        final ResourceConfig rc = new ResourceConfig()
                .register(serviceBinder)
                .register(ContainerResource.class)
                .register(ItemStorageResource.class)
                .register(ShippingResource.class)
                .packages(true, "app");

        // Creating the http server with the resource config and the jersey DI binding module.
        GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }


    public static void usage() {
        System.err.println("Usage : shipchain -start server -id <server-id> -port <server-port>");
        System.exit(0);
    }

    public static ServerProcess server;
    private static int serverId;

    public static int getServerId() {
        return ServerMain.serverId;
    }

    private static void calculateSerializedSize(String path) {
        Utils.Ser ser = new Utils.Ser(path);
        String s = new Gson().toJson(ser);
        NodeAddress.serializedSize = s.getBytes().length;
    }

    public static void main(CommandLine cmd) {
        System.out.println("Starting Server...");
        try {
            // 0. Get arguments.
            if (!cmd.hasOption("id") || !cmd.hasOption("port")) {
                usage();
            }

            ServerMain.serverId = Integer.parseInt(cmd.getOptionValue("id"));
            Utils.serverPort = cmd.getOptionValue("port");

            // 1. Run leader election.
            ProcessNode processNode = LeaderElectionLauncher.launch(ServerMain.serverId);
            ServerMain.calculateSerializedSize(processNode.getNodePath());

            // 2. Open the server, connect to ServerGroup.
            System.out.println("Starting server as " + (processNode.isLeader() ? "leader" : "non-leader"));
            ServerMain.server = new ServerProcess(processNode.isLeader(), processNode.getNodePath());

            // 3. Connect [inject] the server to ProcessNode.
            processNode.setServer(ServerMain.server);

            // 4. Open Http server and connect [inject] the server to Services.
            startHttpServer(Utils.serverPort);
            System.out.println("Starting HTTP server on address = " + BASE_URI);
            ServerMain.server.setReceiver();
            while (true) ;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(9);
        } finally {
            if (ServerMain.server != null) ServerMain.server.leaveGroup();
        }
    }
}
