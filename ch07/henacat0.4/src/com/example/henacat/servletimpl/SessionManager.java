package com.example.henacat.servletimpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SessionManager {
    private final ScheduledExecutorService scheduler;
    @SuppressWarnings("unused")
    private final ScheduledFuture<?> cleanHandle;
    private final int CLEAN_INTERVAL = 60; /* seconds */
    private final int SESSION_TIMEOUT = 10; /* minutes */
    private Map<String, HttpSessionImpl> sessions = new ConcurrentHashMap<String, HttpSessionImpl>();
    private SessionIdGenerator sessionIdGenerator;

    synchronized HttpSessionImpl getSession(String id) {
        var ret = sessions.get(id);
        if (ret != null) {
            ret.access();
        }
        return ret;
    }

    public HttpSessionImpl createSession() {
        var id = this.sessionIdGenerator.generateSessionId();
        var session = new HttpSessionImpl(id);
        sessions.put(id, session);
        return session;
    }

    private synchronized void cleanSession() {
        for (var it = sessions.keySet().iterator(); it.hasNext();) {
            var id = it.next();
            var session = this.sessions.get(id);
            if (session.getLastAccessedTime() < (System.currentTimeMillis() - (SESSION_TIMEOUT * 60 * 1000))) {
                it.remove();
            }
        }
    }

    public SessionManager() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        var cleaner = new Runnable() {
            public void run() {
                cleanSession();
            }
        };
        this.cleanHandle = scheduler.scheduleWithFixedDelay(cleaner, CLEAN_INTERVAL, CLEAN_INTERVAL, TimeUnit.SECONDS);
        this.sessionIdGenerator = new SessionIdGenerator();
    }
}
