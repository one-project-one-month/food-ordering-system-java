package org._p1m.foodorderingsystem.config.beans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class RedisHealthChecker {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @PostConstruct
    public void checkRedisConnection() {
        try (RedisConnection connection = redisConnectionFactory.getConnection()) {
            String ping = connection.ping();
            if ("PONG".equals(ping)) {
                System.out.println("✅ Redis is connected successfully.");
            } else {
                System.out.println("⚠️ Redis connection failed. Response: " + ping);
            }
        } catch (Exception e) {
            System.err.println("❌ Redis is NOT connected. Error: " + e.getMessage());
        }
    }
}
