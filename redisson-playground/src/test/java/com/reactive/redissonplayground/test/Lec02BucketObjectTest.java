package com.reactive.redissonplayground.test;

import com.reactive.redissonplayground.test.dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

public class Lec02BucketObjectTest extends BaseTest {

    @Test
    public void keyValueObjectTest() {

        Student student = new Student("Marshall", 10, "Atlanta", Arrays.asList(1, 2, 3));
        //RBucketReactive<Student> bucket = this.client.getBucket("student:1", JsonJacksonCodec.INSTANCE);
        RBucketReactive<Student> bucket = this.client.getBucket("student:1", new TypedJsonJacksonCodec(Student.class));
        Mono<Void> set = bucket.set(student);
        Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();
        StepVerifier.create(set.concatWith(get))
                .verifyComplete();
    }


}
