package com.learnreactivespring.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class FluxAndMonoFactoryTest {
    List<String> names= Arrays.asList("Adam","Anna","Jack","Jenny");
    @Test
    public void fluxUsingIterable(){
        Flux<String> namesFlux= Flux.fromIterable(names).log();
        StepVerifier.create(namesFlux)
                .expectNext("Adam","Anna","Jack","Jenny")
                .verifyComplete();
    }
    @Test
    public void fluxUsingArray(){
        String[] namesArray=new String[]{"Adam","Anna","Jack","Jenny"};
        Flux<String> namesFlux= Flux.fromArray(namesArray).log();
        StepVerifier.create(namesFlux)
                .expectNext("Adam","Anna","Jack","Jenny")
                .verifyComplete();
    }
    @Test
    public void fluxUsingStream(){
        Flux<String> namesFlux= Flux.fromStream(names.stream()).log();
        StepVerifier.create(namesFlux)
                .expectNext("Adam","Anna","Jack","Jenny")
                .verifyComplete();
    }

    @Test
    public void MonoUsingJustorEmpty(){
        Mono<String> mono=Mono.justOrEmpty(null);
        StepVerifier.create(mono.log())
                .verifyComplete();
    }

    @Test
    public void MonoUsingSupplier(){
        Supplier<String> stringSupplier=() ->"adam";
        Mono<String> monoString= Mono.fromSupplier(stringSupplier);
        StepVerifier.create(monoString.log())
                .expectNext("adam")
                .verifyComplete();
    }

    @Test
    public void FluxUsingRange(){
        Flux<Integer> integerFlux=Flux.range(1, 5);
        StepVerifier.create(integerFlux.log())
                .expectNext(1,2,3,4,5)
                .verifyComplete();
    }
    @Test
    public void FluxUsingFilter(){
        Flux<String> namesFlux=Flux.fromIterable(names)
                .filter(s -> s.startsWith("A"));
        StepVerifier.create(namesFlux.log())
                .expectNext("Adam","Anna")
                .verifyComplete();
    }
    @Test
    public void FluxUsingFilterLength(){
        Flux<String> namesFlux=Flux.fromIterable(names)
                .filter(s -> s.length()>4);
        StepVerifier.create(namesFlux.log())
                .expectNext("Jenny")
                .verifyComplete();
    }
}
