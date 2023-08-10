import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientEmail;

    private List<ClientHandler> clients;

    public ClientHandler(Socket socket, List<ClientHandler> clients) throws IOException {
        this.clients = clients; // Initialize the clients list
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientEmail = bufferedReader.readLine();
            System.out.println("Client email: " + clientEmail);
            clientHandlers.add(this);
            broadcastMessage("SERVER: " + clientEmail + " has joined the server");
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }


    @Override
    public void run() {
        String messageFromClient;
        try {
            while (socket.isConnected()) {
                messageFromClient = bufferedReader.readLine();
                if (messageFromClient == null) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                    break;
                } else if (messageFromClient.startsWith(">>")) {
                    String recipientUsername = messageFromClient.substring(2, messageFromClient.indexOf(" ")).trim();
                    String messageToSend = messageFromClient.substring(messageFromClient.indexOf(" ") + 1);
                    sendToSpecificClient(recipientUsername, messageToSend);
                } else {
                    broadcastMessage(clientEmail + ": " + messageFromClient);
                }
            }
        } catch (IOException e) {
            try {
                closeEverything(socket, bufferedReader, bufferedWriter);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    public void sendToSpecificClient(String recipientUsername, String message) throws IOException {
        for (ClientHandler handler : clientHandlers) {
            if (handler.clientEmail.equals(recipientUsername)) {
                handler.sendMessage("DIRECT MESSAGE from " + clientEmail + ": " + message);
                return;
            }
        }
        // If recipient not found, send a notification to sender
        sendMessage("User " + recipientUsername + " not found or offline.");
    }

    public void sendMessage(String message) throws IOException {
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }


    public synchronized void broadcastMessage(String messageToSend) throws IOException {
        for (ClientHandler clientHandler : clients) {
            if (!clientHandler.clientEmail.equals(clientEmail)) {
                clientHandler.sendMessage(messageToSend);
            }
        }
    }


    public void removeClientHandler() throws IOException {
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientEmail + " has left the server");
    }


    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) throws IOException {
        removeClientHandler();
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