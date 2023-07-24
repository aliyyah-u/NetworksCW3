import java.io.DataInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class EmailServer {
    public static void main(String[] args){
        try{
            ServerSocket serverSocket=new ServerSocket(587);

            //establishes connection
            Socket socket=serverSocket.accept();
            InetAddress clientAddress = socket.getInetAddress();

            // to get address in text form
            String clientAddressString = clientAddress.getHostAddress();

            DataInputStream dis=new DataInputStream(socket.getInputStream());
            String  str=(String)dis.readUTF();
            System.out.println("message= "+str);
            serverSocket.close();
        }catch(Exception e){System.out.println(e);}
    }
}