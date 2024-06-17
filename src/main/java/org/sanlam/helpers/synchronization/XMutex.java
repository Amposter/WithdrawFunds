// From: https://www.north-47.com/java-multithreading-synchronizing-code-blocks-by-value-of-object/
package org.sanlam.helpers.synchronization;

public class XMutex<T> {
    private final T key;

    public XMutex(T key) {
        this.key = key;
    }

    public T getKey() {
        return key;
    }
}
