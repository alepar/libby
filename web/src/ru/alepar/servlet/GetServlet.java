package ru.alepar.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.alepar.ebook.format.EbookType;
import ru.alepar.io.IOUtils;
import ru.alepar.web.AppHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

public class GetServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(GetServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String path = request.getParameterNames().nextElement();

        EbookType type = EbookTypeFilter.ebookType(request);
        File in = AppHolder.getFile(path);
        File out = AppHolder.convertFile(in, type);
        String outName = AppHolder.convertName(in.getName(), type);

        sendFile(response, out, outName);
    }

    private void sendFile(HttpServletResponse response, File src, String fileName) throws IOException {
        response.setContentType("application/octet-stream");
        response.setContentLength((int) src.length());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");

        FileInputStream is = new FileInputStream(src);
        try {
            IOUtils.copy(is, response.getOutputStream());
        } finally {
            is.close();
        }
    }

}
