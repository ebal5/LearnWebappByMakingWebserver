import java.io.*;
import java.net.*;

public class TcpServer {
    public static void main(String[] args) throws Exception {
        try (ServerSocket server = new ServerSocket(8001);
                var fos = new FileOutputStream("server_recv.txt");
                var fis = new FileInputStream("server_send.txt");) {
            System.out.println("クライアントからの接続を待ちます");
            var socket = server.accept();
            System.out.println("クライアント接続");

            int ch;
            var input = socket.getInputStream();
            while ((ch = input.read()) != 0) {
                fos.write(ch);
            }
            var output = socket.getOutputStream();
            while ((ch = fis.read()) != -1) {
                output.write(ch);
            }
            socket.close();
            System.out.println("通信を終了しました");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}