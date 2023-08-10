import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MyServer {
        private ServerSocket serverSocket;
        private Map<String, PrintWriter> clients;
    private Set<String> connectedClients;


    public MyServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.clients = new HashMap<>();
        this.connectedClients = new HashSet<>();
    }


    public void startMyServer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("A Umail client has connected!");
                printConnectedClients();  // Display connected clients
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
        connectedClients.add(clientEmail);
    }


    public synchronized void removeClient(String clientEmail) {
        clients.remove(clientEmail);
        connectedClients.remove(clientEmail);
    }


    public synchronized void forwardMessage(String recipientEmail, String message, String senderEmail) {
        PrintWriter writer = clients.get(recipientEmail);
        if (writer != null) {
            System.out.println("Forwarding message to: " + recipientEmail);
            // Printing the message on the server side
            String fullMessage = senderEmail + ": " + message;
            System.out.println("Forwarding message: " + fullMessage);

            writer.println(fullMessage);
            writer.flush();

            System.out.println("Message forwarded to: " + recipientEmail);
        }
    }

    public void printConnectedClients() {
        System.out.println("Connected Clients:");
        for (String clientEmail : connectedClients) {
            System.out.println("- " + clientEmail);
        }
    }




    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(587);
        System.out.println("Waiting for a client...");
        MyServer myServer = new MyServer(serverSocket);
        myServer.startMyServer();
    }
}
