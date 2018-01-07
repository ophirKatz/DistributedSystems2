package servers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * Created by ophir on 02/01/18.
 */
public class ServerCommunication {

    public static class ServerCom {

        public void runCommunication(List<String> data) throws IOException {
            ServerSocket serverSocket = new ServerSocket(8888);
            Socket client = serverSocket.accept();

            OutputStream outputStream = client.getOutputStream();

            // Communicate
            PrintWriter printWriter = new PrintWriter(outputStream);
            printWriter.write(data.get(0));
            printWriter.close();
            outputStream.close();

            client.close();
        }

    }

    public static class ClientCom {
        public void initiateCommunication() throws IOException {
            String host = "b";
            Socket socket = new Socket(host, 8888);
            InputStream inputStream = socket.getInputStream();
            // Get service (probably a string..) from server

            inputStream.close();
            socket.close();
        }
    }
}
