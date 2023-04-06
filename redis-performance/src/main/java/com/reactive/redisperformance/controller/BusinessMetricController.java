package com.reactive.redisperformance.controller;

import com.reactive.redisperformance.service.BusinessMetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("product/metrics")
public class BusinessMetricController {

    @Autowired
    private BusinessMetricService service;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<Integer, Double>> getMetrics() {
        return this.service.topNProducts()
                .repeatWhen(l -> Flux.interval(Duration.ofSeconds(3)));
    }

}
