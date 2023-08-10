import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientEmail;
    private MyServer server;

    public ClientHandler(Socket socket, MyServer server) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientEmail = bufferedReader.readLine();
            this.server = server;
            server.addClient(clientEmail, new PrintWriter(bufferedWriter, true));
        } catch (IOException e) {
            closeEverything();
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        try {
            while ((messageFromClient = bufferedReader.readLine()) != null) {
                // Forward the message to the intended recipient
                String[] parts = messageFromClient.split(": ", 2);
                if (parts.length == 2) {
                    String recipient = parts[0];
                    String message = parts[1];
                    server.forwardMessage(recipient, clientEmail + ": " + message);
                }
            }
        } catch (IOException e) {
            closeEverything();
        }
    }

    public void closeEverything() {
        server.removeClient(clientEmail);
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
