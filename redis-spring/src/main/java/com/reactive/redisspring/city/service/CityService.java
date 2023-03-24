package com.reactive.redisspring.city.service;

import com.reactive.redisspring.city.client.CityClient;
import com.reactive.redisspring.city.dto.City;
import org.redisson.api.RMapCacheReactive;
import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CityService {

    @Autowired
    private CityClient client;

    private RMapReactive<String, City> cityMap;
    //private RMapCacheReactive<String, City> cityMap; //TTL

    public CityService(RedissonReactiveClient client) {
        this.cityMap = client.getMap("city", new TypedJsonJacksonCodec(String.class, City.class));
        //this.cityMap = client.getMapCache("city", new TypedJsonJacksonCodec(String.class, City.class)); //TTL
    }

    //get from cache, if empty get from db|source put it on cache
    /*public Mono<City> getCity(final String zipCode) {
        return this.cityMap.get(zipCode) //get from cache
                .switchIfEmpty( //do this if empty
                        this.client.getCity(zipCode) //get from external service
                                //.flatMap(c -> this.cityMap.fastPut(zipCode, c) //put in cache
                                .flatMap(c -> this.cityMap.fastPut(zipCode, c, 10, TimeUnit.SECONDS) //put in cache + TTL
                                        .thenReturn(c)
                                )
                );
    }*/

    public Mono<City> getCity(final String zipCode) {
        return this.cityMap.get(zipCode)
                //.switchIfEmpty(this.client.getCity(zipCode)) Both are fine
                .onErrorResume(ex -> this.client.getCity(zipCode));
    }

    @Scheduled(fixedRate = 10_000)
    public void updateCity() {
        this.client.getAll()
                .collectList()
                .map(list -> list.stream().collect(Collectors.toMap(City::getZip, Function.identity())))
                .flatMap(this.cityMap::putAll)
                .subscribe();
    }

}
