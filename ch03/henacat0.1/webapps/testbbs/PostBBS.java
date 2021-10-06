import java.io.IOException;

import com.example.henacat.servlet.ServletException;
import com.example.henacat.servlet.http.HttpServlet;
import com.example.henacat.servlet.http.HttpServletRequest;
import com.example.henacat.servlet.http.HttpServletResponse;

public class PostBBS extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Message newMessage = new Message(req.getParameter("title"), req.getParameter("handle"),
                req.getParameter("message"));
        Message.messageList.add(0, newMessage);
        System.out.println("Got Message from: " + Message.messageList.get(Message.messageList.size() - 1).handle);

        resp.sendRedirect("/testbbs/ShowBBS");
    }
}
