package com.jdhalleine.tuto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.util.function.SupplierUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.List.*;

public class ChuckFactsApiTest {

    @Test
    public void testChuckAPIWithReactStyle() throws InterruptedException {
        WebClient client3 = WebClient
                .builder()
                .baseUrl("https://api.chucknorris.io")
                .build();

        WebClient.RequestBodySpec request1 = client3
                .method(HttpMethod.GET)
                .uri("/jokes/random");


        WebClient.RequestBodySpec request2 = client3
                .method(HttpMethod.GET)
                .uri("/jokes/random");

        WebClient.ResponseSpec response1 = request1.retrieve();
        WebClient.ResponseSpec response2 = request2.retrieve();

        Mono<String> stringMono = response1.bodyToMono(String.class);
        Mono<Joke> jokeMono1 = stringMono.map(s -> {
            try {
                return (Joke) new ObjectMapper().reader().forType(Joke.class).readValue(s);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }).doOnNext(o -> System.out.println(o.getValue()));

        Mono<String> stringMono2 = response1.bodyToMono(String.class);
        Mono<Joke> jokeMono2 = stringMono2.map(s -> {
            try {
                return (Joke) new ObjectMapper().reader().forType(Joke.class).readValue(s);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }).doOnNext(o -> System.out.println(o.getValue()));

        Flux<Joke> stringFlux = jokeMono1.concatWith(jokeMono2);
        stringFlux.subscribe(joke -> System.out.println(joke));

        Flux.interval(Duration.ofSeconds(1))
                .flatMap(aLong -> {
            return response1.bodyToMono(Joke.class);
        }).subscribe(joke -> System.out.println(joke.getValue()));

        Thread.sleep(5000);


        //.collectList().block();

    }

    @Test
    public void testFluxInterval() throws InterruptedException {
        Flux.interval(Duration.ofSeconds(1))
                .log()
                .subscribe(System.out::println);
        Thread.sleep(10000);
    }


    @Test
    public void testFluxError(){
        Flux.range(1, 5)
                .doOnNext(integer -> {
                    if (integer==2) {
                        throw new RuntimeException("Integer equal" + integer);
                    }
                })
                .concatWith(Flux.error(new Exception("Flux tout pourri")))
                .doOnError(throwable -> System.out.println(throwable.getMessage()))
                .onErrorResume(throwable -> {
                                return Flux.range(10, 4)
                                        .doOnNext(integer -> {
                                            if (integer==12){
                                                throw new RuntimeException("2em flux tout pourri aussi");
                                            }
                                        })
                                        .onErrorReturn(1)
                                        ;
                                })
                .log()
                .subscribe();
    }

    @Test
    public void testMono(){
        //System.out.println(Mono.just("test").doOnNext(System.out::println).log().block());
        Flux.range(1, 9).flatMap(integer -> Mono.just(integer).log()).log().subscribe(integer -> System.out.println(integer));

    }

    @Test
    public void testFluxNex() throws InterruptedException {
        Flux.interval(Duration.ofSeconds(1)).take(10).log().subscribe(aLong -> System.out.println(aLong));
        Thread.sleep(10000);
    }

    @Test
    public void testFilterFlux(){

        Flux<String> filter = Flux.fromStream(Stream.<String>of("123,", "ezfoizjefoize",
                                                                "zefpkozepofkezpofk",
                                                                "ezfpkpzeofkopkezfopzekfpokzef"))
                .filter(s -> s.length() > 6);

        StepVerifier.create(filter)
                .expectNextCount(10)
                .expectNext("ezfoizjefoize")
                .expectComplete();

        StepVerifier
                .withVirtualTime(() -> Mono.delay(Duration.ofHours(3)))
                .expectSubscription()
                .expectNoEvent(Duration.ofHours(2))
                .thenAwait(Duration.ofHours(1))
                .expectNextCount(1)
                .expectComplete()
                .verify();

    }

    @Test
    public void testFlatMap() throws InterruptedException {

        Flux<String> stringFlux =
                Flux.range(1, 5)
                .window(2)
                .flatMap(integerFlux -> integerFlux.flatMap(this::getDataForFlapMap))
                //                        .subscribeOn(Schedulers.parallel()))
                //.flatMap(this::getDataForFlapMap)
                //.subscribeOn(Schedulers.parallel())
                //.map(integer -> integer * integer)
                .log();

                stringFlux.subscribe(s -> System.out.println(s));

     //   StepVerifier.create(stringFlux).expectNextCount(5).verifyComplete();
        //Thread.sleep(5000);

        /*
        Flux.range(1, 5)
                .map(i -> i * i )
                .log()
                .subscribe(System.out::println);

         */
    }

    private Mono<String>  getDataForFlapMap(Integer i){
        Map<Integer, String> m = Map.of(1, "totot",
                                        2, "titi",
                                        3, "tutu",
                                        4, "tata",
                                        5, "tete",
                                        6, "pepe");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Mono.just(m.getOrDefault(i, "default value"));
    }

    @Test
    public void testFluxInteger(){
         Flux.range(5, 6)
                 .doOnNext(integer -> System.out.println("From do on next " + integer))
                 .log()
                 .subscribe(System.out::println,
                         throwable -> {},
                         () -> {},
                         subscription ->subscription.request(3));
    }

    @Test
    public void testFluxJoke(){

        Joke j1 = Joke.builder().value("J1").build();
        Joke j2 = Joke.builder().value("J2").build();
        Joke j3 = Joke.builder().value("J3").build();

        Stream<Joke> jokeStream = Stream.of(j1,j2,j3);
        Flux.fromStream(jokeStream)
                .doOnNext(joke -> System.out.println("From do on Next" + joke.getValue()))
                .log()
                .subscribe(System.out::println);

    }

    @Test
    public void testMonoNew(){

    }


}
