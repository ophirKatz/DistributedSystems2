package leaderelection.main;

import leaderelection.nodes.ProcessNode;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Sain Technology Solutions
 */
public class LeaderElectionLauncher {

    //private static final Logger LOG = Logger.getLogger(LeaderElectionLauncher.class);

    public static void main(String args[]) throws IOException {

        System.out.println(System.getProperty("user.dir") + "/src/start_zookeeper.sh");
        //Runtime.getRuntime().exec(System.getProperty("user.dir") + "/src/start_zookeeper.sh");
        //Thread.sleep(300);

        int id = 1;
        //id = Integer.parseInt(args[0]);
        final String zkURL = "127.0.0.1:2181";


        final ExecutorService service = Executors.newSingleThreadExecutor();
        final Future<?> status = service.submit(new ProcessNode(id, zkURL));
        try {
            status.get();
        } catch (InterruptedException | ExecutionException e) {
            //LOG.fatal(e.getMessage(), e);
            e.printStackTrace();
            service.shutdown();
        }
    }
}
