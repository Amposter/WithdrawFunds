// From: https://www.north-47.com/java-multithreading-synchronizing-code-blocks-by-value-of-object/
package org.sanlam.helpers.synchronization;

import org.hibernate.validator.internal.util.ConcurrentReferenceHashMap;
import org.hibernate.validator.internal.util.ConcurrentReferenceHashMap.ReferenceType;

public class XMutexFactory<T> {

    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int CONCURRENCY_LEVEL = 16;
    private static final ReferenceType REFERENCE_TYPE = ReferenceType.WEAK;

    private final ConcurrentReferenceHashMap<T, XMutex<T>> map;

    public XMutexFactory() {
        map = new ConcurrentReferenceHashMap<>(
            INITIAL_CAPACITY,
            LOAD_FACTOR,
            CONCURRENCY_LEVEL,
            REFERENCE_TYPE,
            REFERENCE_TYPE,
            null
        );
    }

    public XMutex<T> getMutex(T key) {
        return map.computeIfAbsent(key, XMutex<T>::new);
    }
}