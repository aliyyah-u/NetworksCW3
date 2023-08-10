import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class EmailServer {
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter = null;
        BufferedReader bufferedReader = null;
        ServerSocket serverSocket = null;

        serverSocket = new ServerSocket(587);

        while (true) {
            try {
                //establishes connection
                socket = serverSocket.accept();
                inputStreamReader = new InputStreamReader(socket.getInputStream());
                outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
                bufferedReader = new BufferedReader(inputStreamReader);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                while (true) {
                    String emailFromClient = bufferedReader.readLine();
                    System.out.println("Client " + emailFromClient);
                    bufferedWriter.write("email received");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();

                    if (emailFromClient.equalsIgnoreCase("BYE"))
                        break;
                }
                socket.close();
                inputStreamReader.close();
                outputStreamWriter.close();
                bufferedWriter.close();
                bufferedReader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}