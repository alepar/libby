package ru.alepar.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;

public class LoggingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    public void init(FilterConfig config) throws ServletException {

    }

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        if (req instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) req;
            String xForwardedFor = request.getHeader("X-Forwarded-For");
            String remoteHost = xForwardedFor == null || xForwardedFor.isEmpty() ? request.getRemoteHost() : xForwardedFor;

            String url = request.getRequestURI();
            String queryString = request.getQueryString();
            queryString = queryString == null || queryString.isEmpty() ? "" : "?" + queryString;
            queryString = URLDecoder.decode(queryString, "utf8");

            log.info("client = {}; url = {}{}", new Object[]{remoteHost, url, queryString});
        }


        chain.doFilter(req, resp);
    }

}
