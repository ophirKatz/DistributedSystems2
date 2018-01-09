package app.leaderelection.main;

import app.leaderelection.nodes.ProcessNode;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * This will be the main class the Server Processes will run.
 * A process that acts as a server will run the main function here and start leader election.
 * After that, the actual logic of the server will be called [while listening on the Watcher].
 *
 */
public class LeaderElectionLauncher {

    //private static final Logger LOG = Logger.getLogger(LeaderElectionLauncher.class);

    public static void main(String args[]) throws IOException {
        final int id = Integer.parseInt(args[0]);
        final String zkURL = "127.0.0.1:2181";


        final ExecutorService service = Executors.newSingleThreadExecutor();
        ProcessNode task = new ProcessNode(id, zkURL);
        final Future<?> status = service.submit(task);
        try {
            status.get();
        } catch (InterruptedException | ExecutionException e) {
            //LOG.fatal(e.getMessage(), e);
            e.printStackTrace();
            service.shutdown();
        }

        // TODO : publish id and leader id
        // TODO : go to main service
    }
}
