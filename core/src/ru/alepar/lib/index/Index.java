package ru.alepar.lib.index;

import org.apache.lucene.queryParser.ParseException;

import java.io.IOException;
import java.util.SortedSet;

public interface Index {


    void addPath(String path, String indexWords) throws IOException;

    SortedSet<String> find(String query) throws ParseException, IOException;

}
