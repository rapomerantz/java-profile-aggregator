package com.atticuspomerantz.profile.profile_aggregator.util;

import org.springframework.stereotype.Component;

import com.atticuspomerantz.profile.profile_aggregator.config.CacheConfig;

import java.util.Map;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * Generic in-memory cache utility with TTL-based expiration.
 * Can cache any object type (GitHub profiles, GitLab profiles, etc.).
 * 
 * Could be replaced in the future with a more robust caching solution (e.g., Redis).
 */
@Component
public class CacheUtility<T> {
    private static final Logger LOGGER = Logger.getLogger(CacheUtility.class.getName());
    private final Map<String, CachedEntry<T>> cache = new ConcurrentHashMap<>();
    private final long ttlMillis;

    public CacheUtility(CacheConfig cacheConfig) {
        this.ttlMillis = cacheConfig.getTtlMillis();
        LOGGER.info("Cache TTL set to " + ttlMillis / 1000 + " seconds");
    }

    /**
     * Retrieves a cached entry if it exists and is not expired.
     *
     * @param key Cache key (e.g., username).
     * @return Cached object or null if not found/expired.
     */
    public T get(String key) {
        CachedEntry<T> cached = cache.get(key);
        if (cached != null) {
            if (!cached.isExpired(ttlMillis)) {
                LOGGER.info("Cache HIT: Returning cached entry for key: " + key);
                return cached.value;
            } else {
                LOGGER.info("Cache EXPIRED: Removing expired entry for key: " + key);
                cache.remove(key);
            }
        } else {
            LOGGER.info("Cache MISS: No cache entry found for key: " + key);
        }
        return null;
    }

    /**
     * Stores an entry in the cache with a timestamp.
     *
     * @param key Cache key (e.g., username).
     * @param value The object to cache.
     */
    public void put(String key, T value) {
        cache.put(key, new CachedEntry<>(value));
        LOGGER.info("Cache UPDATED: Stored entry for key: " + key);
    }

    /** 
     * Internal class for caching objects with expiration timestamps 
     * 
     * This could be moved to its own file if it becomes more complex.
     * */
    private static class CachedEntry<T> {
        private final T value;
        private final long timestamp;

        public CachedEntry(T value) {
            this.value = value;
            this.timestamp = System.currentTimeMillis();
        }

        public boolean isExpired(long ttlMillis) {
            return (System.currentTimeMillis() - timestamp) > ttlMillis;
        }
    }
}