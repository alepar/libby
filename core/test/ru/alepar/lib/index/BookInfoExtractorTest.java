package ru.alepar.lib.index;

import org.junit.Test;

import java.io.File;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class BookInfoExtractorTest {

    private final BookInfoExtractor extractor = new BookInfoExtractor();

    @Test
    public void simplesCaseIeThirdSubfolderIsAuthorFilenameIsBookname() throws Exception {
        String author = "Лукьяненко Сергей";
        String bookName = "Геном";
        String path = createPath("ru", "Л", author);
        String fullPath = path + bookName + ".fb2.zip";

        BookInfoExtractor.Info info = extractor.extract(fullPath);
        assertThat(info.book, equalTo(new Book(fullPath, bookName)));
        assertThat(info.author, equalTo(new Author(path, author)));
    }

    private static String createPath(String... strs) {
        StringBuilder result = new StringBuilder();
        for (String str : strs) {
            result.append(str);
            result.append(File.separatorChar);
        }
        return result.toString();
    }
}
