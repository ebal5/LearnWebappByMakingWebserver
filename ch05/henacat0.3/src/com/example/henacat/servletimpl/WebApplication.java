package com.example.henacat.servletimpl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Map;

public class WebApplication {
    private static String WEBAPPS_DIR = "/workspace/ch05/henacat0.3/webapps";
    private static Map<String, WebApplication> webAppCollection = new HashMap<String, WebApplication>();
    String directory;
    ClassLoader classLoader;
    private Map<String, ServletInfo> servletCollection = new HashMap<String, ServletInfo>();
    private SessionManager sessionManager;

    private WebApplication(String dir) throws MalformedURLException {
        this.directory = dir;
        var fs = FileSystems.getDefault();
        var pathObj = fs.getPath(WEBAPPS_DIR + File.separator + dir);
        this.classLoader = URLClassLoader.newInstance(new URL[] { pathObj.toUri().toURL() });
    }

    public static WebApplication createInstance(String dir) throws MalformedURLException {
        var newApp = new WebApplication(dir);
        webAppCollection.put(dir, newApp);
        return newApp;
    }

    public void addServlet(String urlPattern, String servletClassName) {
        this.servletCollection.put(urlPattern, new ServletInfo(this, urlPattern, servletClassName));
    }

    public static WebApplication searchWebApplication(String dir) {
        return webAppCollection.get(dir);
    }

    public ServletInfo searchServlet(String path) {
        return servletCollection.get(path);
    }

    public SessionManager getSessionManager() {
        if (this.sessionManager == null) {
            this.sessionManager = new SessionManager();
        }
        return this.sessionManager;
    }
}
