import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * GenerateCSV
 */
public class GenerateCSV extends HttpServlet {
    public static final String zodiacSignals[] = { "牡羊座", "牡牛座", "双子座", "蟹座", "獅子座", "乙女座", "天秤座", "蠍座", "射手座", "山羊座",
            "水瓶座", "魚座", };
    private static final String fortunes[] = { "ラッキー", "ふつう", "最悪" };

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.setContentType("text/csv;charset=UTF-8");
        resp.setHeader("Contetn-Disposition", "attachment; filename=\"horoscope.csv\"");
        var out = resp.getWriter();

        for (var signal : zodiacSignals) {
            out.print("\"" + signal + "\",");
            out.print("\"" + fortunes[(int) (Math.random() * fortunes.length)] + "\"\r\n");
        }
    }

}