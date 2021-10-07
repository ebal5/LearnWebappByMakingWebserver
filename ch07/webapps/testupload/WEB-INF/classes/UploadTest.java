import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@MultipartConfig(maxFileSize = 100000, maxRequestSize = 100000, fileSizeThreshold = 100000)
/**
 * UploadTest
 */
public class UploadTest extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        synchronized (System.out) {
            System.out.println("I GOT REQUEST");
        }
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain;charset=UTF-8");
        var out = resp.getWriter();

        for (var part : req.getParts()) {
            out.println("name.." + part.getName());
            for (var headerName : part.getHeaderNames()) {
                out.println(headerName + "=" + part.getHeader(headerName));
            }
            out.println("Content-Type.." + part.getContentType());
            out.println(("Name.." + part.getName() + "/size.." + part.getSize()));
            var reader = new java.io.InputStreamReader(part.getInputStream(), "UTF-8");
            int ch;
            while ((ch = reader.read()) >= 0) {
                out.print((char) (ch & 0xffff));
            }
            reader.close();
            out.println("\n===================================");
        }
        out.println("text_name=" + req.getParameter("text_name"));
    }
}