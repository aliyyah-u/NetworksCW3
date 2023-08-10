import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.io.PrintWriter;

public class MyServer {
        private ServerSocket serverSocket;
        private Map<String, PrintWriter> clients;

        public MyServer(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;
            this.clients = new HashMap<>();
        }

    public void startMyServer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("A Umail client has connected!");
                ClientHandler clientHandler = new ClientHandler(socket, this);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void addClient(String clientEmail, PrintWriter writer) {
        clients.put(clientEmail, writer);
    }

    public synchronized void removeClient(String clientEmail) {
        clients.remove(clientEmail);
    }

    public synchronized void forwardMessage(String recipientEmail, String message) {
        PrintWriter writer = clients.get(recipientEmail);
        if (writer != null) {
            writer.println(message);
            writer.flush();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(587);
        System.out.println("Waiting for a client...");
        MyServer myServer = new MyServer(serverSocket);
        myServer.startMyServer();
    }
}
