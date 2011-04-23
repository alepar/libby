package ru.alepar.lib.traum;

import ru.alepar.lib.model.Item;
import ru.alepar.lib.model.ItemVisitor;

public class TraumIndexer {

    private final Iterable<String> feeder;
    private final ItemStorage storage;
    private final ItemVisitor itemSink;

    private int counter;

    public TraumIndexer(Iterable<String> feeder, ItemStorage storage, ItemVisitor itemSink) {
        this.feeder = feeder;
        this.storage = storage;
        this.itemSink = itemSink;
    }

    public void go() {
        for (String filePath : feeder) {
            counter++;
            try {
                Item item = storage.get(filePath);
                item.visit(itemSink);
            } catch (Exception e) {
                throw new RuntimeException("failed to index traum lib", e);
            }
        }
    }

    public int getCounter() {
        return counter;
    }

}
