package ru.alepar.lib.traum;

import ru.alepar.lib.model.Item;

public interface ItemStorage {
    Item get(String path);
}
