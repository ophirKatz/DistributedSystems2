package app;

import app.client.ClientMain;
import app.server.ServerMain;
import org.apache.commons.cli.*;

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
        options.addOption("-start", true, "starts the application as the server or client");
        options.addOption("-id", true, "id of the server being run");
        options.addOption("-port", true, "port of the server being run");

        return options;
    }

    public static void main(String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(setupCliOptions(), args);
            if (!cmd.hasOption("-start")) {
                usage();
            }
            String startAs = cmd.getOptionValue("-start");
            switch (startAs) {
                case "server":
                    ServerMain.main(args, cmd);
                    break;
                case "client":
                    ClientMain.main(args);
                    break;
                default:
                    usage();
            }
        } catch (ParseException e) {
            System.exit(1);
            e.printStackTrace();
        }
    }
}
