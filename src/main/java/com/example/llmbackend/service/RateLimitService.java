package com.example.llmbackend.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.List;

@Service
public class RateLimitService {

    private final Map<String, List<Long>> requestTimestamps = new ConcurrentHashMap<>();
    private static final long TIME_WINDOW_MS = 60 * 1000; // 1 minute
    private static final int MAX_REQUESTS_PER_WINDOW = 30; // 30 requests per minute

    public boolean checkRateLimit(String clientId) {
        if (clientId == null) {
            clientId = "anonymous";
        }

        long currentTime = System.currentTimeMillis();
        List<Long> timestamps = requestTimestamps.computeIfAbsent(clientId, k -> new ArrayList<>());

        // Remove old timestamps outside the time window
        timestamps.removeIf(timestamp -> currentTime - timestamp > TIME_WINDOW_MS);

        // Check if rate limit is exceeded
        if (timestamps.size() >= MAX_REQUESTS_PER_WINDOW) {
            return false;
        }

        // Add current request timestamp
        timestamps.add(currentTime);
        return true;
    }

    public String getClientId(String apiKey, String remoteAddr) {
        if (apiKey != null && !apiKey.isEmpty()) {
            return "apiKey:" + apiKey;
        }
        return "ip:" + remoteAddr;
    }
}