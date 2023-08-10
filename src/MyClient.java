import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class MyClient {

    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String email;

    public MyClient(Socket socket, String email) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.email = email;
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage() {
        try {
            Scanner scanner = new Scanner(System.in);

            while (socket.isConnected()) {
                System.out.print("Enter recipient email: ");
                String recipient = scanner.nextLine();

                System.out.print("Enter message: ");
                String message = scanner.nextLine();

                // Format the message as "recipientEmail: messageContent"
                String formattedMessage = recipient + ": " + message;

                bufferedWriter.write(formattedMessage);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }


    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromServer;
                while (socket.isConnected()) {
                    try {
                        messageFromServer = bufferedReader.readLine();
                        if (messageFromServer != null) {
                            System.out.println(messageFromServer);
                        }
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }


    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
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

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your email: ");
        String email = scanner.nextLine();

       /* if (m.containsKey(email)) {
            System.out.println("yay! your email is part of the database");

            System.out.println("Please enter your password: ");
            String clientPassword = scanner.nextLine();
            System.out.println(clientPassword);

            if (m.get(email).equals(clientPassword)) {
                System.out.println("yay! your password is part of the database");

                System.out.println("Which email will you send your email to? ");
                String sendEmailTo = scanner.nextLine();

                if (m.containsKey(sendEmailTo)) {
                    System.out.println("yay! this email is part of the database");

                    System.out.println("Please enter the subject (title) of the email you want to send: ");
                    String sendEmailSubject = scanner.nextLine();

                    System.out.println("Please enter the date: ");
                    String sendEmailDate = scanner.nextLine();
                    System.out.println("Please enter the content of the email you want to send: ");
                    String sendEmailContent = scanner.nextLine();
                    System.out.println("Please enter the signature of the email you want to send: ");
                    String sendEmailSignature = scanner.nextLine();
                } else {
                    System.out.println("oh no! this email isn't part of the database");
                }
            } else {
                System.out.println("Incorrect password - try again...");
            }
        } else {
            System.out.println("your email isn't part of the database");
        }
*/
        Socket socket = new Socket("localhost", 587);
        MyClient myClient = new MyClient(socket, email);
        myClient.listenForMessage();
        myClient.sendMessage();


    }

}
