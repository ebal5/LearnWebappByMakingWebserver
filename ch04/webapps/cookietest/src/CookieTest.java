import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieTest extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.setContentType("text/plain");
        var out = resp.getWriter();
        String counterStr = null;
        var cookies = req.getCookies();
        if (cookies == null) {
            out.println("cookies = null");
        } else {
            out.println("cookies.length.." + cookies.length);
            for (var cookie : cookies) {
                out.println("cookie: " + cookie.getName() + "/" + cookie.getValue());
                if (cookie.getName().equals("COUNTER")) {
                    counterStr = cookie.getValue();
                }
            }
        }
        var counter = (counterStr == null) ? 1 : Integer.parseInt(counterStr) + 1;
        var newCookie = new Cookie("COUNTER", String.valueOf(counter));
        resp.addCookie(newCookie);
    }
}