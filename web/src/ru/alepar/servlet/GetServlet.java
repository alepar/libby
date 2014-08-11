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
import java.util.regex.Pattern;

public class GetServlet extends HttpServlet {

    private static final Pattern CLEANUP_SPECIAL_CHARS = Pattern.compile("[\\s]+");

    private final LibbyApp app = LibbyApp.Instance.get();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String path = request.getParameterNames().nextElement();
        final EbookType type = EbookTypeFilter.ebookType(request);

        final File in = app.getFile(path);
        final String outName = app.convertName(in.getName(), type);

        final File out = app.getCachedFile(path, type);
        if (!out.isFile() || !out.canRead()) {
            app.convertFile(in, out, type);
        }

        sendFile(response, out, outName);
    }

    private static void sendFile(HttpServletResponse response, File src, String fileName) throws IOException {
        response.setContentType("application/octet-stream");
        response.setContentLength((int) src.length());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + makeOutFileName(fileName) + '"');

        FileInputStream is = new FileInputStream(src);
        try {
            IOUtils.copy(is, response.getOutputStream());
        } finally {
            is.close();
        }
    }

    private static String makeOutFileName(String fileName) {
        ToLatTranslit translit = new SomeTranslit();
        fileName = translit.lat(fileName.toLowerCase());
        fileName = CLEANUP_SPECIAL_CHARS.matcher(fileName).replaceAll("_");
        return fileName;
    }

}
