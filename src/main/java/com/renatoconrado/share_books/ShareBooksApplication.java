package com.renatoconrado.share_books;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ShareBooksApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShareBooksApplication.class, args);
	}

}
