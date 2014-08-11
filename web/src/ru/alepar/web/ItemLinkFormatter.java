package ru.alepar.web;

import ru.alepar.lib.model.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ItemLinkFormatter implements ItemVisitor {

    private String link;

    @Override
    public void onBook(Book book) {
        link = String.format(
                "<a href='get?%s'>\n" +
                "  %s\n" +
                "</a>",
                encode(book.path), book.name
        );
    }

    @Override
    public void onAuthor(Author author) {
        link = String.format(
                "<b><a href='?path=%s'>\n" +
                "  %s\n" +
                "</a></b>",
                encode(author.path), author.name
        );
    }

    @Override
    public void onFolder(Folder folder) {
        link = String.format(
                "<a href='?path=%s'>\n" +
                "  %s\n" +
                "</a>",
                encode(folder.path), folder.name
        );
    }

    public String getLink(Item item){
        item.visit(this);
        return link;
    }

    private static String encode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
