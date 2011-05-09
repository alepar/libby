package ru.alepar.lib.index;

import ru.alepar.lib.model.Item;
import ru.alepar.lib.translit.AleparTranslit;
import ru.alepar.lib.translit.ToRusTranslit;
import ru.alepar.lib.traum.ItemStorage;

import java.util.*;

public class Querier {

    private final Index index;
    private final ItemStorage storage;
    private final ToRusTranslit toRusTranslit = new AleparTranslit();

    public Querier(Index index, ItemStorage storage) {
        this.index = index;
        this.storage = storage;
    }

    public List<Item> find(String query) {
        try {
            Set<String> queries = new HashSet<String>();
            queries.add(query);
            queries.addAll(toRusTranslit.rus(query));

            String orQuery = makeOrQuery(queries);

            List<String> paths = index.find(orQuery);
            List<Item> items = new ArrayList<Item>(paths.size());
            for (String path : paths) {
                items.add(storage.get(path));
            }

            return items;
        } catch (Exception e) {
            throw new RuntimeException("query failed: " + query, e);
        }
    }

    private static String makeOrQuery(Collection<String> queries) {
        StringBuilder result = new StringBuilder();
        for (String query : queries) {
            if (result.length() > 0) {
                result.append(" OR ");
            }
            result
                    .append('(')
                    .append(query)
                    .append(')');
        }
        return result.toString();
    }
}
