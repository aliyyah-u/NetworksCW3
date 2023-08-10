import java.net.*;
import java.io.*;

public class MyServer {
    private ServerSocket serverSocket;

    public MyServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startMyServer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("A Umail client has connected!");
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {

        }
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
        ServerSocket serverSocket = new ServerSocket(587);
        System.out.println("Waiting for a client...");
        MyServer myServer = new MyServer(serverSocket);
        myServer.startMyServer();
    }
}
