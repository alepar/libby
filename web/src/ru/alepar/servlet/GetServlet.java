package ru.alepar.servlet;

import ru.alepar.ebook.format.EbookType;
import ru.alepar.io.IOUtils;
import ru.alepar.lib.translit.SomeTranslit;
import ru.alepar.lib.translit.ToLatTranslit;
import ru.alepar.web.LibbyApp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class GetServlet extends HttpServlet {

    private final LibbyApp app = LibbyApp.Instance.get();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String path = request.getParameterNames().nextElement();

        EbookType type = EbookTypeFilter.ebookType(request);
        File in = app.getFile(path);
        File out = app.convertFile(in, type);
        String outName = app.convertName(in.getName(), type);

        sendFile(response, out, outName);
    }

    private void sendFile(HttpServletResponse response, File src, String fileName) throws IOException {
        response.setContentType("application/octet-stream");
        response.setContentLength((int) src.length());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + makeOutFileName(fileName) + "\"");

        FileInputStream is = new FileInputStream(src);
        try {
            IOUtils.copy(is, response.getOutputStream());
        } finally {
            is.close();
        }
    }

    private static String makeOutFileName(String fileName) throws UnsupportedEncodingException {
        ToLatTranslit translit = new SomeTranslit();
        fileName = translit.lat(fileName.toLowerCase());
        fileName = fileName.replaceAll("[\\s]+", "_");
        return fileName;
//        return URLEncoder.encode(fileName, "UTF-8");
    }

}
