package com.radicalninja.anvil.util;

import java.util.Collection;

public class ArrayUtils {

    public static <T> boolean addAll(final Collection<T> collection, final T... objects) {
        boolean result = false;
        for (final T object : objects) {
            final boolean changed = collection.add(object);
            if (!result) {
                result = changed;
            }
        }
        return result;
    }

}
