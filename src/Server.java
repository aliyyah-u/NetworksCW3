import SMTP.MySMTP;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Server {
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        try {
            List<ClientHandler> clients = new ArrayList<>(); // Initialize the clients list

            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(socket, clients); // Pass the clients list
                clients.add(clientHandler); // Add the new client handler to the list
                Thread thread = new Thread(clientHandler);
                thread.start();
                // Pass the SMTP server information and start the SMTP logic
                MySMTP mySMTP = new MySMTP("localhost", 25); // Replace with your SMTP server info
                mySMTP.startSMTP();
            }
        }
    } catch(
    IOException e)

    {
        e.printStackTrace();
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(25);
        System.out.println("Waiting for a client...");
        Server server = new Server(serverSocket);
        server.startServer();


        // Pass the serverSocket to MySMTP class
        MySMTP mySMTP = new MySMTP(serverSocket);
        mySMTP.startSMTP();
    }
}

