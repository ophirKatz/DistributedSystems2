package client;

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

    public static Pair<ActionType, String> getUserInput() throws IOException {
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
        return new Pair<>(actionType, baseMapping + "/" + br.readLine());
    }

    public static Response handleUserInput(final String path, final ActionType actionType) {
        switch (actionType) {
            case ADD:   // POST
                break;
            case QUERY: // GET
                Client client = Client.create();
                WebResource baseResource = client.resource("http://localhost:8080" + path);

                break;
        }
        return null;
    }

    public static void main(String[] args) {

        while (true) {
            // 1. Request user input (in the format of a path with query parameters)
            Pair<ActionType, String> userInput;
            try {
                userInput = getUserInput();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            // 2. Create an http rest message and send it

            // 3. Get response and analyze it

            // 4. Repeat
        }

    }
}
