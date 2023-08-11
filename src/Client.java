import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private String userName;
    private Socket socket = null;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;

    public Client(String userName, Socket socket) {
        try {
            this.userName = userName;
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public java.net.Socket getSocket() {
        return this.socket;
    }


//
public void sendMessage() {
    try {
        Scanner scanner = new Scanner(System.in);
        while (socket.isConnected()) {
            System.out.print("Enter message (use >>recipientUsername to send to a specific user): ");
            String input = scanner.nextLine();
            System.out.println("You entered: " + input); // Add this line to print the entered message

            if (input.trim().isEmpty()) {
                continue; // Skip empty messages
            }

            // Check if the message is intended for a specific user
            if (input.startsWith(">>")) {
                bufferedWriter.write(input); // Send as-is
            } else {
                bufferedWriter.write(">>" + input); // Convert to direct message format
            }

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
                String emailFromUmailUsers;
                while (socket.isConnected()) {
                    try {
                        emailFromUmailUsers = bufferedReader.readLine();
                        System.out.println(emailFromUmailUsers);
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
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.println("You entered: " + email);

        Socket socket = new Socket("localhost", 587);
        Client client = new Client(email, socket);
        client.listenForMessage();
        client.sendMessage();


    }
}
