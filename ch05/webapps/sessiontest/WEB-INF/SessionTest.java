import java.io.IOException;
import java.rmi.ServerException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionTest extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServerException {
        resp.setContentType("text/plain");
        var out = resp.getWriter();

        var ses = req.getSession(true);
        var counter = (Integer) ses.getAttribute("Counter");
        if (counter == null) {
            out.println("No Session");
            ses.setAttribute("Counter", 1);
        } else {
            out.println("Counter.." + counter);
            ses.setAttribute("Counter", counter + 1);
        }
    }
}
