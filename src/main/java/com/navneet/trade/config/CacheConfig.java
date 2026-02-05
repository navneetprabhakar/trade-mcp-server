package com.navneet.trade.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.caffeine.CaffeineCacheManager;

/**
 * @author navneet.prabhakar
 */
@Configuration
@EnableCaching
public class CacheConfig {

  // Define Caffeine cache with a default expiration time of 60 minutes
  @Bean
  public Caffeine<Object, Object> caffeineConfig() {
    return Caffeine.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES);
  }

  // Configure CacheManager to use Caffeine
  @Bean
  public CacheManager cacheManager(Caffeine<Object, Object> caffeine) {
    CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager("growwToken");
    caffeineCacheManager.setCaffeine(caffeine);
    return caffeineCacheManager;
  }

}
