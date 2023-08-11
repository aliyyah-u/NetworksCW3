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

                // Process client command (you can extend this for handling different SMTP commands).
                String response = processClientCommand(clientInput);

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

    private String processClientCommand(String clientInput) {
        // Add your logic here to process SMTP commands and generate responses.
        // For simplicity, let's echo back the client's input.
        return "250 " + clientInput + " OK";
    }
}
