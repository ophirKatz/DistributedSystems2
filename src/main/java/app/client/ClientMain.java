package app.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import javafx.util.Pair;

import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Created by ophir on 08/01/18.
 */
public class ClientMain {

    private enum ActionType {
        ADD,
        QUERY
    }

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
        Client client = Client.create();
        switch (actionType) {
            case ADD:   // POST
                WebResource postRequest = client.resource("http://localhost:8080" + path);
                Response response = postRequest.post(Response.class, postRequest);
                if (response.getStatus() != 201) {
                    System.out.println("ERROR for response");
                    System.exit(1);
                }
                break;

            case QUERY: // GET
                WebResource getRequest = client.resource("http://localhost:8080" + path);
                String stringResponse = getRequest.getRequestBuilder().get(String.class);
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
