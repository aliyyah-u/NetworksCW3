import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SMTPServer {
    private ServerSocket serverSocket;
    private List<ClientHandler> clients;
    private int maxClients;

    public SMTPServer(int port, int maxClients) throws IOException {
        serverSocket = new ServerSocket(port);
        clients = new ArrayList<>();
        this.maxClients = maxClients;
    }

    public void startServer() {
        try {
            System.out.println("SMTP server is listening on port " + serverSocket.getLocalPort());

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Check if the maximum number of clients is reached
                if (clients.size() < maxClients) {
                    synchronized (clients) {
                        ClientHandler clientHandler = new ClientHandler(clientSocket,clients);
                        clients.add(clientHandler);
                        Thread thread = new Thread(clientHandler);
                        thread.start();
                    }
                } else {
                    // Reject the client if the maximum number is reached
                    System.out.println("Maximum number of clients reached. Rejecting connection.");
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = 25;
        int maxClients = 5;

        try {
            System.out.println("SMTP Server started on port " + port);
            SMTPServer smtpServer = new SMTPServer(port, maxClients);
            smtpServer.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}