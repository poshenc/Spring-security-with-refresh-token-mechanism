package com.example.jwtDemo;

import com.example.jwtDemo.data.AppUser;
import com.example.jwtDemo.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JwtDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtDemoApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UserService userService) {
		return args -> {
			userService.saveUser(new AppUser(null, "Johnson", "johnson@eunodata.com", "1234"));
			userService.saveUser(new AppUser(null, "Handel", "handeln@eunodata.com", "1234"));
			userService.saveUser(new AppUser(null, "Jess", "jess@eunodata.com", "1234"));
			userService.saveUser(new AppUser(null, "Shawn", "shawn@eunodata.com", "1234"));
		};
	}
}
