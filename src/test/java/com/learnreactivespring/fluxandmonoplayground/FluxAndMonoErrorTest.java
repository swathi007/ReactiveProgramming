package com.learnreactivespring.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoErrorTest {

    @Test
    public void fluxErrorHandling(){
        Flux<String> stringFlux= Flux.just("A","B","C","D")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("D"))
                .onErrorResume(e -> {
                    System.out.println("Exception is ::"+e);
                    return Flux.just("default","default1");
                });
        StepVerifier.create(stringFlux.log())
                .expectNext("A","B","C","D")
                //.expectError(RuntimeException.class) //fails as it expects default flux
                .expectNext("default","default1")
                .verifyComplete();
    }
    @Test
    public void fluxErrorHandling_onErrorReturn(){
        Flux<String> stringFlux= Flux.just("A","B","C","D")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("D"))
                .onErrorReturn("default");
        StepVerifier.create(stringFlux.log())
                .expectNext("A","B","C","D")
                //.expectError(RuntimeException.class) //fails as it expects default flux
                .expectNext("default")
                .verifyComplete();
    }
    @Test
    public void fluxErrorHandling_onErrorMap(){
        Flux<String> stringFlux= Flux.just("A","B","C","D")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("D"))
                .onErrorMap((e) -> new CustomException(e));
        StepVerifier.create(stringFlux.log())
                .expectNext("A","B","C","D")
                //.expectError(RuntimeException.class) //fails as it expects default flux
                .expectError(CustomException.class)
                .verify();
    }
    @Test
    public void fluxErrorHandling_onErrorMap_withretry(){
        Flux<String> stringFlux= Flux.just("A","B","C","D")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("D"))
                .onErrorMap((e) -> new CustomException(e))
                .retry(2);
        StepVerifier.create(stringFlux.log())
                .expectNext("A","B","C","D")
                .expectNext("A","B","C","D")
                .expectNext("A","B","C","D")
                //.expectError(RuntimeException.class) //fails as it expects default flux
                .expectError(CustomException.class)
                .verify();
    }

    @Test
    public void fluxErrorHandling_onErrorMap_withretryBackoff(){
        Flux<String> stringFlux= Flux.just("A","B","C","D")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("D"))
                .onErrorMap((e) -> new CustomException(e))
                .retryBackoff(2, Duration.ofSeconds(5));
        StepVerifier.create(stringFlux.log())
                .expectNext("A","B","C","D")
                .expectNext("A","B","C","D")
                .expectNext("A","B","C","D")
                //.expectError(RuntimeException.class) //fails as it expects default flux
                .expectError(IllegalStateException.class)
                .verify();
    }
}
