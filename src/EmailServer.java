import java.net.ServerSocket;
import java.net.Socket;

public class EmailServer {
    public static void main(String[] args){
        try{
            ServerSocket serverSocket=new ServerSocket(587);

            //establishes connection
            Socket socket=serverSocket.accept();
            System.out.print(umailEmails.containsKey("glenda@umail.com"));
            System.out.print(umailEmails.containsKey("bethany@umail.com"));
            System.out.print(umailEmails.containsValue("monkey88!"));
            System.out.print(umailEmails.containsValue("shark66!"));
            serverSocket.close();
        }catch(Exception e){System.out.println(e);}
    }
}