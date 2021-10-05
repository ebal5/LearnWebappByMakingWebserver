import java.net.ServerSocket;

public class Main {
    public static void main(String[] args) throws Exception {
        try (var server = new ServerSocket(8001)) {
            for (;;) {
                var socket = server.accept();
                var serverThread = new ServerThread(socket);
                var thread = new Thread(serverThread);
                thread.start();
            }
        }
    }
}
