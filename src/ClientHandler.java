import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    @Override
    public void run() {
        try {
            // Display SMTP greeting to the client.
            String serverGreeting = "220 SMTP Server Ready";
            bufferedWriter.write(serverGreeting);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            while (true) {
                // Read client input.
                String clientInput = bufferedReader.readLine();

                if (clientInput == null) {
                    break;  // Client disconnected.
                }

                /// Process client command with bufferedReader and bufferedWriter.
                String response = processClientCommand(clientInput, bufferedReader, bufferedWriter);


                // Send response back to the client.
                bufferedWriter.write(response);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String processClientCommand(String clientInput, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        if (clientInput.toUpperCase().startsWith("HELO")) {
            return "250 Hello";
        } else if (clientInput.toUpperCase().startsWith("MAIL FROM:")) {
            return "250 Ok";
        } else if (clientInput.toUpperCase().startsWith("RCPT TO:")) {
            return "250 Ok";
        } else if (clientInput.toUpperCase().equals("DATA")) {
            return "354 Start mail input";
        } else if (clientInput.toUpperCase().equals("QUIT")) {
            return "221 Bye";
        } else {
            return "500 Command not recognized";
        }
    }

}
