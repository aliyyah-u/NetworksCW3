/*public class EmailClient {
        public static void main(String[] args) {
            try{
                Socket socket=new Socket("localhost",587);
                DataOutputStream dout=new DataOutputStream(socket.getOutputStream());
                dout.writeUTF("Hello Email Server");
                dout.flush();
                dout.close();
                socket.close();
            }catch(Exception e){System.out.println(e);}
        }
}
*/