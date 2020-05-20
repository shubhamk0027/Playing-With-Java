package com.springboot.com;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;


@Component
public class SimpleFilter implements Filter {

    @Override
    public void destroy() {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterchain)
            throws IOException, ServletException {

        System.out.println("Remote Host:"+request.getRemoteHost());
        System.out.println("Remote Address:"+request.getRemoteAddr());
        filterchain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterconfig) throws ServletException {}
}

/**
 * A filter is an object used to intercept the HTTP requests and responses of your application.
 * This filter is registered on its own by SpringBoot
 *
 * we can perform two operations at two instances −
 * Before sending the request to the controller
 * Before sending a response to the client.
 */