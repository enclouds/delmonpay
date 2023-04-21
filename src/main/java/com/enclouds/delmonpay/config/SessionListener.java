package com.enclouds.delmonpay.config;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {

    //private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        se.getSession().setMaxInactiveInterval(60*60*3); //세션만료 3시간
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
    }

}