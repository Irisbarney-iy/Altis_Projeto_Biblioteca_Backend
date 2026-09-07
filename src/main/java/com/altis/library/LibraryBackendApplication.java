package com.altis.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class LibraryBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryBackendApplication.class, args);
	}

	@GetMapping("/")
	public String teste(){
		return "Teste brabo";
	}

}
