package ca.atb.hackathon_2020;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Hackathon2020Application {

	public static final String ISO8601_TIMESTAMP_FORMAT = "yyyy-MM-dd\'T\'HH:mm:ss\'Z\'";

	public static void main(String[] args) {
		SpringApplication.run(Hackathon2020Application.class, args);
	}

}
