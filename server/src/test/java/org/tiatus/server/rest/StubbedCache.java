package org.tiatus.server.rest;

import org.infinispan.AdvancedCache;
import org.infinispan.Cache;
import org.infinispan.CacheCollection;
import org.infinispan.CacheSet;
import org.infinispan.commons.util.concurrent.NotifyingFuture;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.filter.KeyFilter;
import org.infinispan.lifecycle.ComponentStatus;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.notifications.cachelistener.filter.CacheEventConverter;
import org.infinispan.notifications.cachelistener.filter.CacheEventFilter;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by johnreynolds on 10/04/2017.
 */
public class StubbedCache implements Cache {
    @Override
    public void putForExternalRead(Object o, Object o2) {

    }

    @Override
    public void putForExternalRead(Object o, Object o2, long l, TimeUnit timeUnit) {

    }

    @Override
    public void putForExternalRead(Object o, Object o2, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {

    }

    @Override
    public void evict(Object o) {

    }

    @Override
    public Configuration getCacheConfiguration() {
        return null;
    }

    @Override
    public EmbeddedCacheManager getCacheManager() {
        return null;
    }

    @Override
    public AdvancedCache getAdvancedCache() {
        return null;
    }

    @Override
    public ComponentStatus getStatus() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public Object get(Object key) {
        return null;
    }

    @Override
    public CacheSet keySet() {
        return null;
    }

    @Override
    public CacheCollection values() {
        return null;
    }

    @Override
    public CacheSet<Entry> entrySet() {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getVersion() {
        return null;
    }

    @Override
    public Object put(Object o, Object o2) {
        return null;
    }

    @Override
    public Object put(Object o, Object o2, long l, TimeUnit timeUnit) {
        return null;
    }

    @Override
    public Object putIfAbsent(Object o, Object o2, long l, TimeUnit timeUnit) {
        return null;
    }

    @Override
    public void putAll(Map map, long l, TimeUnit timeUnit) {

    }

    @Override
    public Object replace(Object o, Object o2, long l, TimeUnit timeUnit) {
        return null;
    }

    @Override
    public boolean replace(Object o, Object o2, Object v1, long l, TimeUnit timeUnit) {
        return false;
    }

    @Override
    public Object put(Object o, Object o2, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
        return null;
    }

    @Override
    public Object putIfAbsent(Object o, Object o2, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
        return null;
    }

    @Override
    public void putAll(Map map, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {

    }

    @Override
    public Object replace(Object o, Object o2, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
        return null;
    }

    @Override
    public boolean replace(Object o, Object o2, Object v1, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
        return false;
    }

    @Override
    public Object remove(Object o) {
        return null;
    }

    @Override
    public void putAll(Map m) {

    }

    @Override
    public Object putIfAbsent(Object key, Object value) {
        return null;
    }

    @Override
    public boolean remove(Object key, Object value) {
        return false;
    }

    @Override
    public boolean replace(Object key, Object oldValue, Object newValue) {
        return false;
    }

    @Override
    public Object replace(Object key, Object value) {
        return null;
    }

    @Override
    public NotifyingFuture putAsync(Object o, Object o2) {
        return null;
    }

    @Override
    public NotifyingFuture putAsync(Object o, Object o2, long l, TimeUnit timeUnit) {
        return null;
    }

    @Override
    public NotifyingFuture putAsync(Object o, Object o2, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
        return null;
    }

    @Override
    public NotifyingFuture<Void> putAllAsync(Map map) {
        return null;
    }

    @Override
    public NotifyingFuture<Void> putAllAsync(Map map, long l, TimeUnit timeUnit) {
        return null;
    }

    @Override
    public NotifyingFuture<Void> putAllAsync(Map map, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
        return null;
    }

    @Override
    public NotifyingFuture<Void> clearAsync() {
        return null;
    }

    @Override
    public NotifyingFuture putIfAbsentAsync(Object o, Object o2) {
        return null;
    }

    @Override
    public NotifyingFuture putIfAbsentAsync(Object o, Object o2, long l, TimeUnit timeUnit) {
        return null;
    }

    @Override
    public NotifyingFuture putIfAbsentAsync(Object o, Object o2, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
        return null;
    }

    @Override
    public NotifyingFuture removeAsync(Object o) {
        return null;
    }

    @Override
    public NotifyingFuture<Boolean> removeAsync(Object o, Object o1) {
        return null;
    }

    @Override
    public NotifyingFuture replaceAsync(Object o, Object o2) {
        return null;
    }

    @Override
    public NotifyingFuture replaceAsync(Object o, Object o2, long l, TimeUnit timeUnit) {
        return null;
    }

    @Override
    public NotifyingFuture replaceAsync(Object o, Object o2, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
        return null;
    }

    @Override
    public NotifyingFuture<Boolean> replaceAsync(Object o, Object o2, Object v1) {
        return null;
    }

    @Override
    public NotifyingFuture<Boolean> replaceAsync(Object o, Object o2, Object v1, long l, TimeUnit timeUnit) {
        return null;
    }

    @Override
    public NotifyingFuture<Boolean> replaceAsync(Object o, Object o2, Object v1, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
        return null;
    }

    @Override
    public NotifyingFuture getAsync(Object o) {
        return null;
    }

    @Override
    public boolean startBatch() {
        return false;
    }

    @Override
    public void endBatch(boolean b) {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void addListener(Object o, KeyFilter keyFilter) {

    }

    @Override
    public void addFilteredListener(Object o, CacheEventFilter cacheEventFilter, CacheEventConverter cacheEventConverter, Set set) {

    }

    @Override
    public void addListener(Object o, CacheEventFilter cacheEventFilter, CacheEventConverter cacheEventConverter) {

    }

    @Override
    public void addListener(Object o) {

    }

    @Override
    public void removeListener(Object o) {

    }

    @Override
    public Set<Object> getListeners() {
        return null;
    }
}
