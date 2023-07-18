import java.io.*;
import java.net.*;
import java.util.ArrayList;

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

        public class MySizeLimitedArrayList extends ArrayList<Socket> {
            @Override
            public boolean add(Socket e) {
                if (this.size() < 10) {
                    return super.add(e);
                }
                return false;
            }
        }

    }
}