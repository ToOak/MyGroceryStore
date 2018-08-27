package com.example.xushuailong.mygrocerystore.utils;

import java.util.Collection;

public class ListUtil {
    public static int getSize(Collection collection) {
        return collection == null ? 0 : collection.size();
    }
}
