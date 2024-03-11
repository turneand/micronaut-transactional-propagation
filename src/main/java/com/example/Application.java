package com.example;

import io.micronaut.data.r2dbc.operations.R2dbcOperations;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.runtime.Micronaut;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

public class Application {
    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }

  @Controller
  public static class SimpleController {
    @Inject MyService service;
	@Inject R2dbcOperations operations;

    @Get("/hello")
	@Transactional
    public Publisher<String> hello() {
	  return service.getMessage();
    }

    @Get("/helloManual")
    public Publisher<String> helloManual() {
	  return operations.withTransaction(x -> service.getMessage());
    }
  }

  @Singleton
  public static class MyService {
    @Transactional(Transactional.TxType.MANDATORY)
	public Mono<String> getMessage() {
	  return Mono.just("hello");
    }
  }
}
