package ru.alepar.lib.index;

import org.apache.lucene.queryParser.ParseException;

import java.io.IOException;
import java.util.List;

public interface Index {


    void addPath(String path, String indexWords, Double boost) throws IOException;

    List<String> find(String query) throws ParseException, IOException;

    long size();
}
