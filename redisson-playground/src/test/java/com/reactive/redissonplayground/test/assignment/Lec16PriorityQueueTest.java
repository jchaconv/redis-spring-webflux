package com.reactive.redissonplayground.test.assignment;

import com.reactive.redissonplayground.test.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RScoredSortedSetReactive;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class Lec16PriorityQueueTest extends BaseTest {

    private PriorityQueue priorityQueue;

    @BeforeEach
    void setUpQueue() {
        RScoredSortedSetReactive<UserOrder> sortedSet = this.client.getScoredSortedSet("user:order:queue", new TypedJsonJacksonCodec(UserOrder.class));
        this.priorityQueue = new PriorityQueue(sortedSet);
    }

    /* Part1 - funcionó correctamente
    @Test
    void producer() {
        UserOrder u1 = new UserOrder(1, Category.GUEST);
        UserOrder u2 = new UserOrder(2, Category.STD);
        UserOrder u3 = new UserOrder(3, Category.PRIME);
        UserOrder u4 = new UserOrder(4, Category.STD);
        UserOrder u5 = new UserOrder(5, Category.GUEST);
        UserOrder u6 = new UserOrder(6, Category.PRIME);
        UserOrder u7 = new UserOrder(7, Category.STD);
        Mono<Void> mono = Flux.just(u1, u2, u3, u4, u5, u6, u7)
                .flatMap(this.priorityQueue::add)
                .then();
        StepVerifier.create(mono).verifyComplete();
    }*/

    @Test
    void producer() {
        Flux.interval(Duration.ofSeconds(1))
                .map(l -> (l.intValue() * 5))
                .doOnNext(i -> {
                    UserOrder u1 = new UserOrder(i + 1, Category.GUEST);
                    UserOrder u2 = new UserOrder(i + 2, Category.STD);
                    UserOrder u3 = new UserOrder(i + 3, Category.PRIME);
                    UserOrder u4 = new UserOrder(i + 4, Category.STD);
                    UserOrder u5 = new UserOrder(i + 5, Category.GUEST);
                    UserOrder u6 = new UserOrder(i + 6, Category.PRIME);
                    UserOrder u7 = new UserOrder(i + 7, Category.STD);
                    Mono<Void> mono = Flux.just(u1, u2, u3, u4, u5, u6, u7)
                            .flatMap(this.priorityQueue::add)
                            .then();
                    StepVerifier.create(mono).verifyComplete();
                }).subscribe();
        sleep(60_000);
    }

    @Test
    void consumer() {
        this.priorityQueue.takeItems()
                .delayElements(Duration.ofMillis(500))
                .doOnNext(System.out::println)
                .subscribe();
        sleep(600_000);
    }
}
