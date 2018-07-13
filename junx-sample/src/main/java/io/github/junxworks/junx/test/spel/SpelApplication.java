package io.github.junxworks.junx.test.spel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.junxworks.junx.spel.function.FunctionLoader;

@FunctionLoader
@SpringBootApplication
public class SpelApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpelApplication.class, args);
	}
}
