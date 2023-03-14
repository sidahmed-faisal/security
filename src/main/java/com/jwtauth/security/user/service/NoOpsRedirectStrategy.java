package com.jwtauth.security.user.service;

import org.springframework.security.web.RedirectStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NoOpsRedirectStrategy implements RedirectStrategy {
    @Override
    public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String s)
            throws IOException {
        //no op
    }
}
