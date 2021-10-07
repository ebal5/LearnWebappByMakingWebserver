import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetAddress extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.setContentType("text/plain;charset=UTF-8");
        var out = resp.getWriter();
        var postalCode = req.getParameter("postalCode");
        String ret;
        if (postalCode == null) {
            ret = "不明";
        } else {
            switch (postalCode) {
                case "162-0846":
                    ret = "東京都新宿区市谷左内町";
                    break;
                case "100-0014":
                    ret = "東京都千代田区永田町";
                    break;
                default:
                    ret = "不明";
            }
        }
        out.print(ret);
    }
}