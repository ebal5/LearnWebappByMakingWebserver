import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PostBBS extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Message newMessage = new Message(req.getParameter("title"), req.getParameter("handle"),
                req.getParameter("message"));
        Message.messageList.add(0, newMessage);
        System.out.println("Got Message from: " + newMessage.title);

        resp.sendRedirect("/testbbsauth/ShowBBS");
    }
}
