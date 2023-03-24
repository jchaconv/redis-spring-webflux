package com.reactive.redisspring.fib.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class FibService {

    /*@Cacheable(value = "math:fib", key = "#index")
    public int getFib(int index, String name) {
        System.out.println("Calculating fib for " + index + ", name: " + name);
        return this.fib(index);
    }*/

    //for cache evict example
    @Cacheable(value = "math:fib", key = "#index")
    public int getFib(int index) {
        System.out.println("Calculating fib for " + index);
        return this.fib(index);
    }

    @CacheEvict(value = "math:fib", key = "#index")
    public void clearCache(int index) {
        System.out.println("clearing hash key");
    }

    @Scheduled(fixedRate = 10_000)
    @CacheEvict(value = "math:fib", allEntries = true)
    public void clearCache() {
        System.out.println("clearing all math:fib keys");
    }

    private int fib(int index) {
        if(index < 2)
            return index;
        return fib(index -1) + fib(index - 2);
    }

}
