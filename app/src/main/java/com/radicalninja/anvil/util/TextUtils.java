package com.radicalninja.anvil.util;

import java.util.Collection;

public class TextUtils {

    public static boolean isEmpty(final String text) {
        return null == text || text.length() == 0;
    }

    public static boolean addFormattedStrings(
            final Collection<String> to, final Collection<String> from, final String template) {
        if (ArrayUtils.anyNull(to, from, template)) {
            return false;
        }
        boolean result = false;
        for (final String string : from) {
            if (isEmpty(string)) {
                continue;
            }
            final String formatted = String.format(template, string);
            final boolean changed = to.add(formatted);
            if (!result) {
                result = changed;
            }
        }
        return result;
    }

}
