package SMTP;

import java.io.*;
import java.net.*;

public class SMTPClient {
    private String SMTPServer;
    private int SMTPPort;
    private Socket client;


    public SMTPClient(String SMTPServer, int SMTPPort) {
        this.SMTPServer = SMTPServer;
        this.SMTPPort = SMTPPort;
    }

    public void startSMTP() {
        try {
            // Attempt to create a client socket connected to the SMTP server program.
            Socket client = new Socket(SMTPServer, SMTPPort);

            // Create a buffered reader for line-oriented reading from the standard input device.

            BufferedReader stdin;
            stdin = new BufferedReader(new InputStreamReader(System.in));

            // Create a buffered reader for line-oriented reading from the socket.

            InputStream is = client.getInputStream();
            BufferedReader sockin;
            sockin = new BufferedReader(new InputStreamReader(is));

            // Create a print writer for line-oriented writing to the socket.

            OutputStream os = client.getOutputStream();
            PrintWriter sockout;
            sockout = new PrintWriter(os, true); // true for auto-flush

            // Display SMTP greeting from SMTP server program.

            System.out.println("S:" + sockin.readLine());

            while (true) {
                // Display a client prompt.

                System.out.print("C:");

                // Read a command string from the standard input device.

                String cmd = stdin.readLine();

                // Write the command string to the SMTP server program.

                sockout.println(cmd);

                // Read a reply string from the SMTP server program.

                String reply = sockin.readLine();

                // Display the first line of this reply string.

                System.out.println("S:" + reply);

                // If the DATA command was entered and it succeeded, keep
                // writing all lines until a line is detected that begins
                // with a . character. These lines constitute an email
                // message.


                if (cmd.toLowerCase().startsWith("data") &&
                        reply.substring(0, 3).equals("354")) {
                    // Prompt user for email details
                    System.out.print("Enter sender email: ");
                    String from = stdin.readLine();
                    System.out.print("Enter recipient email: ");
                    String to = stdin.readLine();
                    System.out.print("Enter email subject: ");
                    String subject = stdin.readLine();

                    // Send the headers
                    sockout.println("From: " + from);
                    sockout.println("To: " + to);
                    sockout.println("Subject: " + subject);
                    sockout.println(); // Blank line to separate headers and body
                    sockout.flush();

                    // Read and send the email content line by line until a line with a single full stop is encountered
                    while (true) {
                        String line = stdin.readLine();
                        sockout.println(line);
                        sockout.flush();
                        if (line.equals(".")) {
                            break;
                        }
                    }

                    // Read a reply string from the SMTP server program.
                    reply = sockin.readLine();

                    // Display the first line of this reply string.
                    System.out.println("S:" + reply);

                    continue;
                }

                // If the QUIT command was entered, quit.

                if (cmd.toLowerCase().startsWith("quit"))
                    break;
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        } finally {
            try {
                // Attempt to close the client socket.
                if (client != null)
                    client.close();
            } catch (IOException e) {
            }
        }

    }

    public void sendEmail(String from, String to, String subject, String body) {
        try {
            Socket client = new Socket(SMTPServer, SMTPPort);

            BufferedReader sockin = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter sockout = new PrintWriter(client.getOutputStream(), true);

            System.out.println("S:" + sockin.readLine());

            // Send HELO command and receive response
            sockout.println("HELO localhost");
            String reply = sockin.readLine();
            System.out.println("S:" + reply);

            // Send MAIL FROM command and receive response
            sockout.println("MAIL FROM:<" + from + ">");
            reply = sockin.readLine();
            System.out.println("S:" + reply);

            // Send RCPT TO command and receive response
            sockout.println("RCPT TO:<" + to + ">");
            reply = sockin.readLine();
            System.out.println("S:" + reply);

            // Send DATA command and receive response
            sockout.println("DATA");
            reply = sockin.readLine();
            System.out.println("S:" + reply);

            // Send email headers and body
            sockout.println("From: " + from);
            sockout.println("To: " + to);
            sockout.println("Subject: " + subject);
            sockout.println(); // Blank line to separate headers and body
            sockout.println(body);
            sockout.println(".");
            reply = sockin.readLine();
            System.out.println("S:" + reply);

            // Send QUIT command and receive response
            sockout.println("QUIT");
            reply = sockin.readLine();
            System.out.println("S:" + reply);

            client.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}




