import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Socket;

public class TcpClient {
    public static void main(String[] args) throws Exception {
        try (var socket = new Socket("server", 80);
                var fis = new FileInputStream("client_send.txt");
                var fos = new FileOutputStream("client_recv.txt");) {
            int ch;
            var output = socket.getOutputStream();
            while ((ch = fis.read()) != -1) {
                output.write(ch);
            }
            // output.write(0);
            var input = socket.getInputStream();
            while ((ch = input.read()) != -1) {
                fos.write(ch);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
