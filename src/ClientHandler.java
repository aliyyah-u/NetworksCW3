import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientEmail;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientEmail = bufferedReader.readLine();
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
                } else if (messageFromClient.contains(">>")) {
                    String recipientUsername = messageFromClient.substring(0, messageFromClient.indexOf(">")).trim();
                    String messageToSend = messageFromClient.substring(messageFromClient.indexOf(">") + 2);
                    sendToSpecificClient(recipientUsername, messageToSend);
                } else {
                    broadcastMessage(clientEmail + ": " + messageFromClient);
                }
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendToSpecificClient(String recipientUsername, String message) {
        for (ClientHandler handler : clientHandlers) {
            if (handler.clientEmail.equals(recipientUsername)) {
                handler.sendMessage("DIRECT MESSAGE from " + clientEmail + ": " + message);
                return;
            }
        }
        // If recipient not found, send a notification to sender
        sendMessage("User " + recipientUsername + " not found or offline.");
    }

    public void sendMessage(String message) {
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void broadcastMessage(String messageToSend) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.clientEmail.equals(clientEmail)) {
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientEmail + " has left the server");
    }


    public void closeEverything (Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
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