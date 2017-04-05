package org.tiatus.server.rest;

/**
 * Created by johnreynolds on 05/04/2017.
 */
public class CacheEntry<T> {
    private String eTag;
    private T entry;

    public CacheEntry(String eTag, T entry) {
        this.entry = entry;
        this.eTag = eTag;
    }

    public T getEntry() {
        return entry;
    }

    public String getETag() {
        return eTag;
    }
}
