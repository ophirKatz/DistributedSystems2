package app.client;

import javafx.util.Pair;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Created by ophir on 08/01/18.
 */
public class ClientMain {

    public static final int HTTP_CREATED = 201;

    private enum ActionType {
        ADD,
        QUERY
    }

    public static final String port = "8181";

    private static Pair<ActionType, String> getUserInput() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // Action type
        System.out.println("Choose database action :\n\t[1]  Add to system.\n\t[2]  Query the resources.");
        String action = br.readLine();
        int actionIndex = Integer.parseInt(action) - 1;
        ActionType actionType = ActionType.values()[actionIndex];

        // Type of resource
        System.out.println("Choose type of resource :\n\t[1] for shipping.\n\t[2] for containers.\n\t[3] for storage.");
        String type = br.readLine();
        int typeIndex = Integer.parseInt(type) - 1;
        String baseMapping = "/" + new String[]{"shipping", "containers", "storage"}[typeIndex];

        // Input query
        System.out.println("Input query on the resource :");
        String resourceQuery = br.readLine();
        return new Pair<>(actionType, baseMapping + "/" + resourceQuery);
    }

    private static void handleUserInput(final String path, final ActionType actionType) {
        Client client = ClientBuilder.newClient();
        String applicationPath = "/shipchain";
        Invocation.Builder builder = client.target("http://localhost:" + port + applicationPath)
                .path(path)
                .request(MediaType.APPLICATION_JSON);
        switch (actionType) {
            case ADD:   // POST
                //Form form = new Form().param()
                Response response = builder.post(null);
                //Response response = builder.post(Entity.form(form));
                if (response.getStatus() != HTTP_CREATED) {
                    System.out.println("ERROR for response");
                    System.exit(1);
                }
                break;

            case QUERY: // GET
                String stringResponse = builder.get(String.class);
                System.out.println("*****************************************************\n");
                System.out.println("The response from the server was : " + stringResponse);
                System.out.println("\n*****************************************************");
                break;
        }
    }

    public static void main(String[] args) {

        while (true) {
            // 1. Request user input (in the format of a path with query parameters)
            Pair<ActionType, String> userInput = null;
            try {
                userInput = getUserInput();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

            // Break condition :
            if (userInput.getValue().contains("stop")) {
                break;
            }

            // 2. Create an http rest message and send it
            handleUserInput(userInput.getValue(), userInput.getKey());
            // 3. Get response and analyze it - in handleUserInput function

            // 4. Repeat
        }
    }
}
