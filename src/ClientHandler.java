import SMTP.SMTPClient;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

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

    private String processClientCommand(String clientInput, BufferedReader bufferedReader, BufferedWriter bufferedWriter) throws IOException {
        if (clientInput.toUpperCase().startsWith("HELO")) {
            return "250 Hello";
        } else if (clientInput.toUpperCase().startsWith("MAIL FROM:")) {
            return "250 Ok";
        } else if (clientInput.toUpperCase().startsWith("RCPT TO:")) {
            return "250 Ok";
        } else if (clientInput.toUpperCase().equals("DATA")) {
            // Respond to DATA command
            bufferedWriter.write("354 Enter message, ending with a line with a single full stop (.)");
            bufferedWriter.newLine();
            bufferedWriter.flush();

            StringBuilder emailContent = new StringBuilder();
            String line;

            // Read and construct email headers
            StringBuilder headers = new StringBuilder();
            while (!(line = bufferedReader.readLine()).isEmpty()) {
                headers.append(line).append("\r\n");
            }

            // Read email content line by line until a line with a single full stop is encountered
            while (!(line = bufferedReader.readLine()).equals(".")) {
                emailContent.append(line).append("\r\n");
            }

            // Construct the complete email message
            String emailMessage = headers.toString() + "\r\n" + emailContent.toString();

            // Process the email message (validate, send, etc.)
            String messageID = generateMessageID(); // Generate a unique message ID
            sendEmailUsingSMTPClient(headers.toString(), emailContent.toString());


            // Respond with the appropriate SMTP response
            return "250 Ok: queued as " + messageID;

        } else if (clientInput.toUpperCase().equals("QUIT")) {
            return "221 Bye";
        } else {
            return "500 Command not recognized";
        }
    }
    private String generateMessageID() {
        // Generate a unique message ID using UUID
        return "<" + UUID.randomUUID().toString() + ">";
    }
    private void sendEmailUsingSMTPClient(String headers, String body) {
        // Extract 'From' and 'To' addresses from headers
        String from = extractEmailAddress(headers, "From:");
        String to = extractEmailAddress(headers, "To:");

        // Extract 'Subject' from headers
        String subject = extractHeaderValue(headers, "Subject:");

        // Create an instance of SMTPClient and send the email
        SMTPClient smtpClient = new SMTPClient("localhost", 25);
        smtpClient.sendEmail(from, to, subject, body);
    }
    private String extractHeaderValue(String headers, String headerName) {
        // Find the index of the header
        int startIndex = headers.indexOf(headerName);
        if (startIndex == -1) {
            return "";
        }

        // Find the end of the line
        int endIndex = headers.indexOf("\r\n", startIndex);
        if (endIndex == -1) {
            return "";
        }

        // Extract and return the header value
        return headers.substring(startIndex + headerName.length(), endIndex).trim();
    }
    private String extractEmailAddress(String headers, String headerName) {
        // Extract the header value
        String headerValue = extractHeaderValue(headers, headerName);

        // Extract the email address within angle brackets
        int startIndex = headerValue.indexOf("<");
        int endIndex = headerValue.indexOf(">");
        if (startIndex != -1 && endIndex != -1) {
            return headerValue.substring(startIndex + 1, endIndex);
        }

        // If angle brackets are not present, return the entire header value (fallback)
        return headerValue;
    }

}