import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private List<ClientHandler> clients;

    public ClientHandler(Socket socket, List<ClientHandler> clients) throws IOException {
        try {
            this.socket = socket;
            this.clients = clients;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        try {
            while (socket.isConnected()) {
                String input = bufferedReader.readLine();
                if (input == null) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                    return;
                }

                // Handle SMTP commands
                if (input.toUpperCase().startsWith("HELO")) {
                    // Respond to HELO command
                    bufferedWriter.write("250 Hello");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                } else if (input.toUpperCase().startsWith("MAIL FROM:")) {
                    // Respond to MAIL FROM command
                    bufferedWriter.write("250 Ok");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                } else if (input.toUpperCase().startsWith("RCPT TO:")) {
                    // Respond to RCPT TO command
                    bufferedWriter.write("250 Ok");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                } else if (input.toUpperCase().equals("DATA")) {
                    // Respond to DATA command
                    bufferedWriter.write("354 Start mail input");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();

                    // Read email content
                    StringBuilder emailContent = new StringBuilder();
                    String line;
                    while (!(line = bufferedReader.readLine()).equals(".")) {
                        emailContent.append(line).append("\r\n");
                    }

                    // Send email using your SMTPDemo class
                    boolean emailSent = sendEmailUsingSMTPDemo(emailContent.toString());

                    // Respond based on the success of email sending
                    if (emailSent) {
                        bufferedWriter.write("250 Ok");
                    } else {
                        bufferedWriter.write("500 Error in sending email");
                    }
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                } else if (input.toUpperCase().equals("QUIT")) {
                    // Respond to QUIT command
                    bufferedWriter.write("221 Bye");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    closeEverything(socket, bufferedReader, bufferedWriter);
                    return;
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

    private boolean sendEmailUsingSMTPDemo(String emailContent) {
        // Use your SMTPDemo class to send the email
        // You can modify your SMTPDemo class to accept email content as parameters
        // and return a boolean indicating success or failure
        try {
            // Example usage:
            String from = "sender@example.com";
            String to = "recipient@example.com";
            String mailHost = "mail.example.com";
            MySMTP.SMTP mail = new MySMTP.SMTP(mailHost);
            return mail.send(new StringReader(emailContent), from, to);
        } catch (IOException e) {
            return false;
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