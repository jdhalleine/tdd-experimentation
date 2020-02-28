package com.jdhalleine.tuto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.stream.Stream;

public class ChuckFactsApiTest {

    @Test
    public void testChuckAPIWithReactStyle() throws InterruptedException {
        WebClient client3 = WebClient
                .builder()
                .baseUrl("https://api.chucknorris.io")
                .build();

        WebClient.RequestBodySpec uri1 = client3
                .method(HttpMethod.GET)
                .uri("/jokes/random");


        WebClient.RequestBodySpec uri2 = client3
                .method(HttpMethod.GET)
                .uri("/jokes/random");

        WebClient.ResponseSpec retrieve = uri1.retrieve();
        WebClient.ResponseSpec retrieve2 = uri2.retrieve();

        Mono<String> stringMono = retrieve.bodyToMono(String.class);
        Mono<Joke> jokeMono1 = stringMono.map(s -> {
            try {
                return (Joke) new ObjectMapper().reader().forType(Joke.class).readValue(s);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }).doOnNext(o -> System.out.println(o.getValue()));

        Mono<String> stringMono2 = retrieve.bodyToMono(String.class);
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

        Flux.interval(Duration.ofSeconds(1)).flatMap(aLong -> {
            //Mono<String> s = retrieve.bodyToMono(String.class);
            return retrieve.bodyToMono(Joke.class);
        })//.doOnNext(o -> System.out.println(o.getValue()))
                .log();
                //.subscribe(joke -> System.out.println(joke.getValue()));

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
        Mono.just("test").doOnNext(System.out::println).subscribe();
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


}