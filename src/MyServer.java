import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class MyServer {
    public static void main(String[] args){
        try{
            ServerSocket serverSocket=new ServerSocket(587);
            Socket socket=serverSocket.accept();//establishes connection
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