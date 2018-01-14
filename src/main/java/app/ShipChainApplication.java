package app;

import app.client.ClientMain;
import app.server.ServerMain;
import org.apache.commons.cli.*;

import java.io.IOException;

/**
 * Created by ophir on 10/01/18.
 */
public class ShipChainApplication {

    private static void usage() {
        System.err.println("Usage : shipchain -start [server/client] [-id <server-id>] [-port <server-port>]");
        System.exit(0);
    }

    private static Options setupCliOptions() {
        Options options = new Options();
        options.addOption("start", true, "starts the application as the server or client");
        options.addOption("id", true, "id of the server being run");
        options.addOption("port", true, "port of the server being run");

        return options;
    }

    public static void main(String[] args) {
        // new JHades().overlappingJarsReport();
        System.out.println("Starting ShipChain Application...");
        CommandLineParser parser = new DefaultParser();
        try {
            Utils.readApplicationConfiguration();
            System.out.println("Parsing Arguments...");
            CommandLine cmd = parser.parse(setupCliOptions(), args);
            if (!cmd.hasOption("start")) {
                usage();
            }
            String startAs = cmd.getOptionValue("start");
            switch (startAs) {
                case "server":
                    ServerMain.main(cmd);
                    break;
                case "client":
                    ClientMain.main(cmd);
                    break;
                default:
                    usage();
            }
        } catch (ParseException | org.json.simple.parser.ParseException | IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
