package ru.alepar.lib.traum;

import org.junit.Test;
import ru.alepar.lib.index.Author;
import ru.alepar.lib.index.Book;

import java.io.File;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class TraumBookInfoExtractorTest {

    private final TraumBookInfoExtractor extractor = new TraumBookInfoExtractor();

    @Test
    public void simplesCaseIeThirdSubfolderIsAuthorFilenameIsBookname() throws Exception {
        String author = "Лукьяненко Сергей";
        String bookName = "Геном";
        String path = createPath("ru", "Л", author);
        String fullPath = path + bookName + ".fb2.zip";

        TraumBookInfoExtractor.Info info = extractor.extract(fullPath);
        assertThat(info.book, equalTo(new Book(fullPath, bookName, null)));
        assertThat(info.author, equalTo(new Author(path, author)));
    }

    @Test
    public void bookWithSeriesInPathExtractSeriesToProperBookField() throws Exception {
        String author = "Лукьяненко Сергей";
        String bookName = "Калеки";
        String seriesName = "Геном";
        String path = createPath("ru", "Л", author);
        String fullPath = path + seriesName + File.separatorChar + bookName + ".fb2.zip";

        TraumBookInfoExtractor.Info info = extractor.extract(fullPath);
        assertThat(info.book, equalTo(new Book(fullPath, bookName, seriesName)));
        assertThat(info.author, equalTo(new Author(path, author)));
    }

    @Test
    public void bookWithNoAuthorInfoGetsPathIntoNameClearedOffNonWordCharacters() throws Exception {
        String path = createPath("ru", "_", "_периодика", "Журнал PC Magazine", "Журнал PC Magazine_RE #01_2009.fb2.zip");

        TraumBookInfoExtractor.Info info = extractor.extract(path);
        assertThat(info.author, equalTo(null));
        assertThat(info.book, equalTo(new Book(path, "ru периодика Журнал PC Magazine Журнал PC Magazine RE 01 2009", null)));
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
