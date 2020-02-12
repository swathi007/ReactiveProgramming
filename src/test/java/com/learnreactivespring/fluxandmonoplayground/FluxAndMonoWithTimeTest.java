package com.learnreactivespring.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoWithTimeTest {

    @Test
    public void infiniteSequence(){
        Flux<Long> infiniteFlux= Flux.interval(Duration.ofMillis(100))
                .log();

        infiniteFlux.subscribe((element)->System.out.println("Value is: "+element));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void infiniteSequenceTest(){
        Flux<Long> infiniteFlux= Flux.interval(Duration.ofMillis(100))
                .take(3)
                .log();

       // infiniteFlux.subscribe((element)->System.out.println("Value is: "+element));

        StepVerifier.create(infiniteFlux)
                .expectSubscription()
                .expectNext(0L, 1L,2L)
                .verifyComplete();
    }
    @Test
    public void infiniteSequenceTest_usingMap(){
        Flux<Integer> infiniteFlux= Flux.interval(Duration.ofMillis(100))
                .delayElements(Duration.ofSeconds(1))
                .map(l -> new Integer(l.intValue()))
                .take(3)
                .log();

        // infiniteFlux.subscribe((element)->System.out.println("Value is: "+element));

        StepVerifier.create(infiniteFlux)
                .expectSubscription()
                .expectNext(0, 1,2)
                .verifyComplete();
    }
}
