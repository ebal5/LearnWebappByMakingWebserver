import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ShowBBS
 */
public class ShowBBS extends HttpServlet {

    private String escapeHtml(String src) {

        return src.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'",
                "&#39;");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        var out = resp.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>テスト掲示板</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>テスト掲示板</h1>");
        out.println("<form action='/testbbs/PostBBS' method='post'>");
        out.println("タイトル: <input type='text' name='title' size='60'><br>");
        out.println("ハンドル名: <input type='text' name='title' size='60'>");
        out.println("<textarea name='message' rows='4' cols='60'></textarea></br>");
        out.println("<input type='submit'>");
        out.println("</form>");
        out.println("<hr>");

        Message.messageList.forEach((message) -> {
            out.println("<p>『" + escapeHtml(message.title) + "』&nbsp;&nbsp;" + escapeHtml(message.handle)
                    + " さん&nbsp;&nbsp;" + escapeHtml(message.date.toString()) + "</p>");
            out.println("<p>");
            out.println(escapeHtml(message.message).replace("\r\n", "<br/>"));
            out.println("</p><hr/>");
        });
        out.println("</body>");
        out.println("</html>");
    }

}
