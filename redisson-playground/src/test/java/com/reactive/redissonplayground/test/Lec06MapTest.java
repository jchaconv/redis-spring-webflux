package com.reactive.redissonplayground.test;

import com.reactive.redissonplayground.test.dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.api.RMapReactive;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

public class Lec06MapTest extends BaseTest {

    @Test
    public void mapTest1() {

        RMapReactive<String, String> map = this.client.getMap("user:1", StringCodec.INSTANCE);

        Mono<String> name = map.put("name", "Julio");
        Mono<String> age = map.put("age", "27");
        Mono<String> city = map.put("city", "Lima");

        StepVerifier.create(name.concatWith(age).concatWith(city).then())
                .verifyComplete();
    }

    @Test
    public void mapTest2() {

        RMapReactive<String, String> map = this.client.getMap("user:2", StringCodec.INSTANCE);

        Map<String, String> javaMap = Map.of(
                "name", "Ruti",
                "age", "30",
                "city", "Lima"
        );

        StepVerifier.create(map.putAll(javaMap).then())
                .verifyComplete();
    }

    @Test
    public void mapTest3() {

        TypedJsonJacksonCodec codec = new TypedJsonJacksonCodec(Integer.class, Student.class);
        RMapReactive<Integer, Student> map = this.client.getMap("users", codec);

        Student student1 = new Student("Julio", 27, "lima", List.of(1, 2, 3));
        Student student2 = new Student("Ruti", 30, "lima", List.of(10, 20, 30));

        Mono<Student> mono1 = map.put(1, student1);
        Mono<Student> mono2 = map.put(2, student2);

        StepVerifier.create(mono1.concatWith(mono2).then())
                .verifyComplete();
    }


}
