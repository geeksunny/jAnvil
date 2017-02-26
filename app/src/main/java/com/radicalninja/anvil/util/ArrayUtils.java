package com.radicalninja.anvil.util;

import java.util.Collection;

public class ArrayUtils {

    public static boolean isEmpty(final Collection collection) {
        return null == collection || collection.size() == 0;
    }

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

    public static boolean anyNull(final Object ... objects) {
        for (final Object object : objects) {
            if (null == object) {
                return true;
            }
        }
        return false;
    }

}
