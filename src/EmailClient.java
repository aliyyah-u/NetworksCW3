import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class EmailClient {
    public static void main(String[] args) {
        Socket socket = null;
        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        String completeEmail;

        Map <String,String> m = new HashMap<>();
        m.put("kant@umail.com", "crocodile99!");
        m.put("ruby@umail.com", "monkey88!");
        m.put("joe@umail.com", "tiger772?");
        m.put("jack@umail.com","parrot7831?");
        m.put("fernanda@umail.com", "grasshopper87.");

        try {
            socket = new Socket("localhost", 587);
            inputStreamReader = new InputStreamReader(socket.getInputStream());
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            bufferedWriter = new BufferedWriter(outputStreamWriter);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("Please enter your email: ");
                String clientEmail = scanner.nextLine();

                if (m.containsKey(clientEmail)){
                    System.out.println("yay! your email is part of the database");
                    System.out.println("Please enter your password: ");
                    String clientPassword = scanner.nextLine();
                    System.out.println(clientPassword);
                    if (m.get(clientEmail).equals(clientPassword)){
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

                            completeEmail = (clientEmail + "/n" + sendEmailTo + "/n" + sendEmailDate + "/n" + sendEmailSubject + "/n" + sendEmailContent + "/n" + sendEmailSignature);
                            System.out.println(completeEmail);
                        }
                        else {
                            System.out.println("oh no! this email isn't part of the database");
                        }
                    }
                    else {
                        System.out.println("oh no! Incorrect password - try again...");
                    }
                }
                else {
                    System.out.println("oh no! your email isn't part of the database");
                }

                bufferedWriter.write(clientEmail);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                System.out.println("Server " + bufferedReader.readLine());

                if (clientEmail.equalsIgnoreCase("BYE"))
                    break;
            }
        }      catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null)
                    socket.close();
                if (inputStreamReader != null)
                    socket.close();
                if (outputStreamWriter != null)
                    socket.close();
                if (bufferedReader != null)
                    socket.close();
                if (bufferedWriter != null)
                    socket.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}

