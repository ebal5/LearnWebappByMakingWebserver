import java.io.IOException;

import com.example.henacat.servlet.ServletException;
import com.example.henacat.servlet.http.Cookie;
import com.example.henacat.servlet.http.HttpServlet;
import com.example.henacat.servlet.http.HttpServletRequest;
import com.example.henacat.servlet.http.HttpServletResponse;

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
                System.out.println("cookie in req: " + cookie.getName() + "/" + cookie.getValue());

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