import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * GenerateImage
 */
public class GenerateImage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.setContentType("image/png");

        var ses = req.getSession(true);
        var counter = (Integer) ses.getAttribute("Counter");
        if (counter == null) {
            counter = Integer.valueOf(1);
        } else {
            counter++;
        }
        ses.setAttribute("Counter", counter);
        var counterStr = new DecimalFormat("000000").format(counter);
        var image = new BufferedImage(100, 25, BufferedImage.TYPE_INT_RGB);
        var g = image.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.setColor(Color.WHITE);
        var font = new Font(Font.SANS_SERIF, Font.PLAIN, 20);
        g.setFont(font);
        var fm = g.getFontMetrics();
        var strWidth = fm.stringWidth(counterStr);
        var strAscent = fm.getAscent();
        var x = (image.getWidth() - strWidth) / 2;
        int y = image.getHeight() - ((image.getHeight() - strAscent) / 2);
        g.drawString(counterStr, x, y);
        g.dispose();

        ImageIO.write(image, "png", resp.getOutputStream());
    }
}