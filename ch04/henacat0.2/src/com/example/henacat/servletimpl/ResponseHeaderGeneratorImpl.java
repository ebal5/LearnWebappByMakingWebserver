package com.example.henacat.servletimpl;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import com.example.henacat.servlet.http.Cookie;
import com.example.henacat.util.ResponseHeaderGenerator;
import com.example.henacat.util.Util;

public class ResponseHeaderGeneratorImpl implements ResponseHeaderGenerator {
    private ArrayList<Cookie> cookies;

    private static String getCookieDateString(Calendar cal) {
        var df = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss", Locale.US);
        df.setTimeZone(cal.getTimeZone());
        return df.format(cal.getTime()) + " GMT";
    }

    private String asCookieString(String attr, String val, boolean isFirst) {
        return (val == null) ? "" : (isFirst ? "" : "; ") + attr + "=" + val;
    }

    public void generate(OutputStream output) throws IOException {
        for (var cookie : cookies) {
            String header;
            header = "Set-Cookie: " + cookie.getName() + "=" + cookie.getValue();
            header += asCookieString("Domain", cookie.getDomain(), false);
            if (cookie.getMaxAge() > 0) {
                var cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                cal.add(Calendar.SECOND, cookie.getMaxAge());
                header += "; Expires=" + getCookieDateString(cal);
            } else if (cookie.getMaxAge() == 0) {
                var cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                cal.set(1970, 0, 1, 0, 0, 10);
                header += "; Expires=" + getCookieDateString(cal);
            }
            header += asCookieString("Path", cookie.getPath(), false);
            header += cookie.getSecure() ? "; Secure" : "";
            header += cookie.isHttpOnly() ? "; HttpOnly" : "";

            Util.writeLine(output, header);
        }
    }

    public ResponseHeaderGeneratorImpl(ArrayList<Cookie> cookies) {
        this.cookies = cookies;
    }
}
