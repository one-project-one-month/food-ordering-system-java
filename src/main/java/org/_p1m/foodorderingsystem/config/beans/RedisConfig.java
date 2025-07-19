package org._p1m.foodorderingsystem.config.beans;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RedisConfig {

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName("redis-19849.c13.us-east-1-3.ec2.redns.redis-cloud.com");
        redisConfig.setPort(19849);
        redisConfig.setPassword(RedisPassword.of("hprrsKlf2Zc6VjGoxwJkrZljsMGjHTKI"));

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
        	    .build();

        return new LettuceConnectionFactory(redisConfig, clientConfig);
    }
}
