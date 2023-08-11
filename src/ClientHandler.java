import SMTP.SMTPClient;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String fromAddress;

    private String rcptAddress;

    private List<ClientHandler> clients;

    public ClientHandler(Socket socket, List<ClientHandler> clients) throws IOException {
        this.socket = socket;
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.clients = clients;
    }

    @Override
    public void run() {
        try {
            // Display SMTP initial greeting to the client.
            String serverGreeting = "220 SMTP Server Ready";
            bufferedWriter.write(serverGreeting);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            boolean shouldRun = true;
            while (shouldRun) {
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

                // Check if the QUIT command was received
                if (clientInput.toUpperCase().equals("QUIT")) {
                    shouldRun = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                // Remove this client handler from the list
                synchronized (clients) {
                    clients.remove(this);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String processClientCommand(String clientInput, BufferedReader bufferedReader, BufferedWriter bufferedWriter) throws IOException {
        if (clientInput.toUpperCase().startsWith("HELO")) {
            return "250 Hello";
        } else if (clientInput.toUpperCase().startsWith("MAIL FROM:")) {
            fromAddress = extractEmailAddress(clientInput, "MAIL FROM:");
            return "250 Ok";
        } else if (clientInput.toUpperCase().startsWith("RCPT TO:")) {
            rcptAddress = extractEmailAddress(clientInput, "RCPT TO:");
            return "250 Ok";
        } else if (clientInput.toUpperCase().equals("DATA")) {
            if (fromAddress == null) {
                return "500 Missing sender address. Use MAIL FROM: <email>";
            }
            if (rcptAddress == null) {
                return "500 Missing recipient address. Use RCPT TO: <email>";
            }
            // Respond to DATA command
            bufferedWriter.write("354 Enter message, ending with a line with a single full stop (.)");
            bufferedWriter.newLine();
            bufferedWriter.flush();

            // Use a separate input stream to read the email content
            InputStream is = socket.getInputStream();
            BufferedReader emailBufferedReader = new BufferedReader(new InputStreamReader(is));

            StringBuilder emailContent = new StringBuilder();
            String line;


            // Read and construct email content line by line until a line with a single full stop is encountered
            while (true) {
                line = emailBufferedReader.readLine();
                if (line.equals(".")) {
                    break;
                }
                emailContent.append(line).append("\r\n");
            }

            // Construct email headers
            StringBuilder headers = new StringBuilder();
            headers.append("From: ").append(extractEmailAddress(emailContent.toString(), "From:")).append("\r\n");
            headers.append("To: ").append(extractEmailAddress(emailContent.toString(), "To:")).append("\r\n");
            headers.append("Subject: ").append(extractHeaderValue(emailContent.toString(), "Subject:")).append("\r\n");


            // Construct the complete email message by combining headers and content
            String emailMessage = headers.toString() + "\r\n" + emailContent.toString();

            // Extract 'From' and 'To' addresses from headers
            String from = extractEmailAddress(emailMessage, "From:");
            String to = extractEmailAddress(emailMessage, "To:");

            // Extract 'Subject' from headers
            String subject = extractHeaderValue(emailMessage, "Subject:");

            // Process the email message
            String messageID = generateMessageID(); // Generate a unique message ID
            sendEmailUsingSMTPClient(from, headers.toString(), emailContent.toString());


            // Call sendEmailUsingSMTPClient method with the stored "From" address
            sendEmailUsingSMTPClient(fromAddress, headers.toString(), emailContent.toString());

            //SMTP response code and message to indicate the successful email queueing
            return "250 Ok: queued as " + messageID;
        } else if (clientInput.toUpperCase().equals("QUIT")) {
            //SMTP response code to indicate that it is closing the connection with the client.
            return "221 Bye";
        } else {
            return "500 Command not recognized";
        }
    }

    private String generateMessageID() {
        // Generate a unique message ID using UUID
        return "<" + UUID.randomUUID().toString() + ">";
    }

    // Inside the ClientHandler class
    private void sendEmailUsingSMTPClient(String from, String headers, String emailContent) {
        // Extract 'From' and 'To' addresses from headers
        String to = extractEmailAddress(headers, "To:");

        // Extract 'Subject' from headers
        String subject = extractHeaderValue(headers, "Subject:");

        // Generate the current date in RFC 822 format
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
        String dateHeader = "Date: " + dateFormat.format(new Date());

        // Create the full set of headers including the new "Date" header
        StringBuilder newHeaders = new StringBuilder();
        newHeaders.append(headers).append("\r\n").append(dateHeader);

        // Create an instance of SMTPClient and send the email
        SMTPClient smtpClient = new SMTPClient("localhost", 25);
        smtpClient.sendEmail(from, to, subject, newHeaders.toString(), emailContent);
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