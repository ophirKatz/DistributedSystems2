package app.client;

import app.server.servers.jersey.model.AbstractTransaction;
import app.server.servers.jersey.model.ContainerModel;
import app.server.servers.jersey.model.ShippingModel;
import app.server.servers.jersey.model.StorageModel;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Created by ophir on 08/01/18.
 */
public class ClientMain {

    public static final int HTTP_OK = 200;

    private static final String applicationPath = "/shipchain";

    private enum ActionType {
        ADD,
        QUERY
    }

    public static String port = "8282";

    static {
        // 5 tries to find a server to connect to.
        /*for (int i = 0; i < 5 && port == null; i++) {
            try {
                port = findRandomServerPortToConnectTo();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    private static <TransactionType extends AbstractTransaction> void handlePOST(Client client, String path, TransactionType transaction) {
        WebTarget transactionTarget = client.target("http://localhost:" + port + applicationPath)
                .path(path);
        Response response = transactionTarget
                .request()
                .post(Entity.json(transaction));
        if (response.getStatus() != HTTP_OK) {
            System.out.println("Error Code : " + response.getStatus());
        }
        System.out.println(response.readEntity(String.class));
    }

    private static void queryShipping(Client client, BufferedReader br) throws IOException {
        System.out.println("Please create a model");
        System.out.println("Enter ship id");
        String shipId = br.readLine();
        System.out.println("Enter port id");
        String portId = br.readLine();
        // Creating the model according to user input
        ShippingModel shippingModel = new ShippingModel(shipId, portId);

        System.out.println("What action do you wish to perform with the ship model?");
        System.out.println("\t[1] Leaving\n\t[2] Arriving");
        String actionInput = br.readLine();
        String action = actionInput.equals("1") ? "shipLeaving" : "shipArriving";

        handlePOST(client, "shipping/" + action, shippingModel);
    }

    private static void queryStorage(Client client, BufferedReader br) throws IOException {
        System.out.println("Please create a model");
        System.out.println("Enter container id");
        String containerId = br.readLine();
        System.out.println("Enter item id");
        String itemId = br.readLine();
        // Creating the model according to user input
        StorageModel storageModel = new StorageModel(containerId, itemId);

        System.out.println("What action do you wish to perform with the item model?");
        System.out.println("\t[1] Store\n\t[2] Remove");
        String actionInput = br.readLine();
        String action = actionInput.equals("1") ? "store" : "remove";

        handlePOST(client, "storage/" + action, storageModel);
    }

    private static void queryContainers(Client client, BufferedReader br) throws IOException {
        System.out.println("Please create a model");
        System.out.println("Enter container id");
        String containerId = br.readLine();
        System.out.println("Enter ship id");
        String shipId = br.readLine();
        // Creating the model according to user input
        ContainerModel containerModel = new ContainerModel(containerId, shipId);

        System.out.println("What action do you wish to perform with the container model?");
        System.out.println("\t[1] Load\n\t[2] Unload");
        String actionInput = br.readLine();
        String action = actionInput.equals("1") ? "load" : "unload";

        handlePOST(client, "containers/" + action, containerModel);
    }

    private static void performPOSTAction(Client client, BufferedReader br) throws IOException {
        // Type of resource
        System.out.println("Choose type of resource :\n\t[1] for shipping.\n\t[2] for containers.\n\t[3] for storage.");
        String type = br.readLine();

        switch (type) {
            case "1":       // Shipping
                queryShipping(client, br);
                break;
            case "2":        // Containers
                queryContainers(client, br);
                break;
            case "3": // Item Storage
                queryStorage(client, br);
                break;
        }
    }

    private static void performGETAction(Client client, BufferedReader br) throws IOException {
        // Type of resource
        System.out.println("Choose type of resource :\n\t[1] for shipping.\n\t[2] for containers.\n\t[3] for storage.");
        String type = br.readLine();
        int typeIndex = Integer.parseInt(type) - 1;
        String baseMapping = "/" + new String[]{"shipping", "containers", "storage"}[typeIndex];

        // Input query
        System.out.println("Input query on the resource :");
        String resourceQuery = br.readLine();
        String query = baseMapping + "/" + resourceQuery;

        String request = "http://localhost:" + port + applicationPath + query;
        System.out.println("Run request <" + request + "> on the server? [y/n]");
        if (br.readLine().equals("y")) {
            String stringResponse = client.target(request)
                    .request()
                    .get(String.class);
            System.out.println("*****************************************************\n");
            System.out.println("The response from the server was : " + stringResponse);
            System.out.println("\n*****************************************************");
        }
    }

    private static boolean getUserInputAndPerformAction() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Client client = ClientBuilder.newClient();
        // Action type
        System.out.println("Choose database action :\n\t[1]  Add to system.\n\t[2]  Query the resources.");
        String action = br.readLine();

        switch (action) {
            case "1":
                // Add to system via an HTTP POST method
                performPOSTAction(client, br);
                break;
            case "2":
                // Query the resources via an HTTP GET method
                performGETAction(client, br);
                break;
            default:
                if (action.contains("stop")) {
                    // Then stop the client
                    return false;
                }
        }
        return true;
    }

    public static void main(String[] args) {
        while (true) {
            try {
                if (!getUserInputAndPerformAction()) {
                    System.out.println("Stopping the client service");
                    Thread.sleep(100);
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
