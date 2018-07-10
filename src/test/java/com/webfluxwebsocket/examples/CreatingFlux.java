package com.webfluxwebsocket.examples;

import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

/**
 * @see https://github.com/ZhongyangMA/webflux-streaming-demo/wiki/An-Overview-of-Reactive-Programming
 * (webflux-streaming-demo) https://github.com/ZhongyangMA/webflux-streaming-demo
 */
public class CreatingFlux {

    public static void main(String[] args) {
        // by static methods:
        List<String> words = Arrays.asList("aa","bb","cc","dd");

        Flux<String> listWords = Flux.fromIterable(words);    //todo: create from iterable collection
        Flux<String> justWords = Flux.just("Hello","World");  //todo: specify the elements by 'just'
        listWords.subscribe(System.out::println);
        justWords.subscribe(System.out::println);

        // using generate() method to generate a Flux:
        // by invoking next(), complete() and error() of SynchronousSink, generate() generates the elements of Flux one by one synchronously.
        Flux.generate(sink -> {
            sink.next("Hello");
            sink.complete();
        }).subscribe(System.out::println);

        // using create() method to create a Flux sequence:
        // different with generate(), create() uses FluxSink object, which could create more than one elements in one invocation asynchronously.
        Flux.create(sink -> {
            for (int i = 0; i < 10; i++) sink.next(i);
            sink.complete();
        }).subscribe(System.out::println);
    }

}
