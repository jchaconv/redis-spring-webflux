package com.reactive.redissonplayground.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.RScoredSortedSetReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Function;

public class Lec15SortedSetTest extends BaseTest {

    @Test
    public void sortedTest() {

        RScoredSortedSetReactive<Object> sortedSet = this.client.getScoredSortedSet("student:score", StringCodec.INSTANCE);

        Mono<Void> mono = sortedSet.addScore("julio", 12.25)
                .then(sortedSet.add(23.26, "ruti"))//Aquí se invierte el orden xq solo es add()
                .then(sortedSet.addScore("aaron", 7))
                .then();

        StepVerifier.create(mono).verifyComplete();

        sortedSet.entryRange(0, 1)
                .flatMapIterable(Function.identity())
                .map(scoreEntry -> scoreEntry.getScore() + " : " + scoreEntry.getValue())
                .doOnNext(System.out::println)
                .subscribe();

        sleep(1000);

    }


}
