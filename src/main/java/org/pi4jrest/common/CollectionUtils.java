package org.pi4jrest.common;

import java.util.Iterator;

public class CollectionUtils {

    public static <T> T get(Iterable<T> iterable, int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Negative index is not allowed!");
        }

        Iterator<T> iterator = iterable.iterator();
        T next = iterator.next();
        while (index-- > 0) {
            if (!iterator.hasNext()) {
                throw new IndexOutOfBoundsException(String.valueOf(index));
            }

            next = iterator.next();
        }

        return next;
    }
}
