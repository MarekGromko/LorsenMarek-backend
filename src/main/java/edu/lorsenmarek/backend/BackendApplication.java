package edu.lorsenmarek.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The application main entrypoint
 */
@SpringBootApplication
public class BackendApplication {
	private BackendApplication() {}
	/**
	 * Application entrypoint
	 * @param args args are mostly ignored
	 */
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
