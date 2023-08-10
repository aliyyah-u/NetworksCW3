import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        String emailFromClient;
        while (socket.isConnected()) {
            try {
                emailFromClient = bufferedReader.readLine();
                broadcastMessage(emailFromClient);
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void sendToOneClient(String userName, Map<String, Client> clients) throws IOException {
        Client c = clients.get(userName);

        java.net.Socket socket = c.getSocket();

        // Sending the response back to the client.
        OutputStream os = socket.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedWriter bufferedWriter = new BufferedWriter(osw);
        bufferedWriter.write("Some message");
        bufferedWriter.flush();
    }

    while(true)

    {
        String input = in.readLine();
        if (input == null) //if there is no input,do nothing
        {
            return;
        }

        //when a user sends a message to a specific user
        else if (input.contains(">>"))   //checks whether the message contains a >>
        {
            String person = input.substring(0, input.indexOf(">"));    //extract the name of the destination user
            for (HashMap.Entry<String, PrintWriter> entry : writersMap.entrySet())  //find the destination user from the users list
            {
                if (entry.getKey().matches(person))  //if the destination user is found
                {
                    PrintWriter writer = entry.getValue();
                    writer.println("MESSAGE " + name + ": " + input);
                }
            }
        } else {
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientEmail + "has left the server");
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
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