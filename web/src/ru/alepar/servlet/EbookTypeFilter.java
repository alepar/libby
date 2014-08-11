package ru.alepar.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.alepar.ebook.format.EbookType;
import ru.alepar.web.LibbyApp;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "EbookTypeFilter")
public class EbookTypeFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(EbookTypeFilter.class);
    private static final String EBOOK_TYPE_KEY = "EbookType";

    private final LibbyApp app = LibbyApp.Instance.get();

    public static EbookType ebookType(HttpServletRequest request) {
        return (EbookType) request.getAttribute(EBOOK_TYPE_KEY);
    }

    public static void saveEbookType(EbookType type, HttpServletRequest request) {
        request.setAttribute(EBOOK_TYPE_KEY, type);
        request.getSession().setAttribute(EBOOK_TYPE_KEY, type);
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;

        String logString;
        EbookType type = (EbookType) request.getSession().getAttribute(EBOOK_TYPE_KEY);
        if (type == null) {
            logString = "detecting EbookType, type = ";
            type = app.detect(request.getHeader("User-Agent"));
        } else {
            logString = "got EbookType from session, type = ";
        }
        saveEbookType(type, request);
        log.debug(logString + type);

        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

}
