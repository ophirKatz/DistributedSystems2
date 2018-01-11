package app.server.leaderelection.main;

import app.server.leaderelection.nodes.ProcessNode;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This will be the launch class the ServerProcess Processes will run.
 * A process that acts as a server will run the launch function here and start leader election.
 * After that, the actual logic of the server will be called [while listening on the Watcher].
 */
public class LeaderElectionLauncher {

    private static final String localhost = "127.0.0.1";

    public static ProcessNode launch(final int id) throws IOException {
        final int zkPort = 2181;
        final String zkURL = localhost + ":" + String.valueOf(zkPort);

        final ExecutorService service = Executors.newSingleThreadExecutor();
        ProcessNode task = new ProcessNode(id, zkURL);
        final Future<?> status = service.submit(task);
        try {
            status.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            service.shutdown();
            System.exit(2);
        }
        return task;
    }
}
