import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileDownloader {
    private static final String TARGET_ADDRESS = "http://server/downloadtest/FileDownload.java";

    public static void main(String[] args) {
        HttpURLConnection conn = null;
        BufferedInputStream input = null;
        BufferedOutputStream output = null;

        try {
            var url = new URL(TARGET_ADDRESS);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            input = new BufferedInputStream(conn.getInputStream());
            output = new BufferedOutputStream(new FileOutputStream("FileDownload.java"));

            int ch;
            while ((ch = input.read()) != -1) {
                output.write(ch);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                if (output != null) {
                    output.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
