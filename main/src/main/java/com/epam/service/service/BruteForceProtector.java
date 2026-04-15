package com.epam.service.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BruteForceProtector {

    private static final int MAX_ATTEMPTS = 3;
    private static final long LOCK_TIME_MS = 5 * 60 * 1000;

    private final Map<String, Integer> attemptsCache = new ConcurrentHashMap<>();
    private final Map<String, Long> lockTimeCache = new ConcurrentHashMap<>();

    public void loginFailed(String key) {
        int attempts = attemptsCache.getOrDefault(key, 0);
        attempts++;
        attemptsCache.put(key, attempts);

        if (attempts >= MAX_ATTEMPTS) {
            lockTimeCache.put(key, System.currentTimeMillis() + LOCK_TIME_MS);
            attemptsCache.remove(key);
        }
    }

    public boolean isBlocked(String key) {
        Long lockTime = lockTimeCache.get(key);
        if (lockTime == null) {
            return false;
        }

        if (System.currentTimeMillis() > lockTime) {
            lockTimeCache.remove(key);
            return false;
        }

        return true;
    }

    public void loginSucceeded(String key) {
        attemptsCache.remove(key);
        lockTimeCache.remove(key);
    }
}