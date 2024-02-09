package com.heeju.shop.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;


public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    // AuthenticationEntryPoint: Used by Spring Web Security to configure the application as it performs
    // a specific action whenever an unauthenticated client attempts to access a private resource.
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         org.springframework.security.core.AuthenticationException authException)
            throws IOException, ServletException {

        if ("XMLHttpRequest".equals(request.getHeader("x-requested-with"))){
            // In the case of AJAX, a request is made with the value XMLHttpRequest set in the http request header.
            // If an unauthenticated user requests a resource through AJAX (in addition to It redirects you to the page.
            // (Users who are not authenticated at the time of request must not have X-Requested-with, otherwise it means something goes odd)

            // "X-Request-With": This header means that the request is AJAX. CORS (Cross Origin Resource Sharing) prevents CSRF attacks because AJAX requests cannot be made without server consent. In other words, it strengthens security. CORS is a method where the server and client decide whether to respond to each other's requests or responses through specified headers.
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        } else {
            response.sendRedirect("/members/login");
        }
    }

}
