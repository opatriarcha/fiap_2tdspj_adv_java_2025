package br.com.empresa.springCrud.AppExample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AppExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppExampleApplication.class, args);
	}

}
