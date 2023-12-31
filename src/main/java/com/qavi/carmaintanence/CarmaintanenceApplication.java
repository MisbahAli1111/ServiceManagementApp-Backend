package com.qavi.carmaintanence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin
@EnableScheduling
public class CarmaintanenceApplication {
	public static final String secretJWT = "oilchangeapplicationlkajjqieojqiojeksmndvjasfjhasdifjiwef";
	public static final Long loginExpiryTimeMinutes = 120L;

	public static void main(String[] args) {
		SpringApplication.run(CarmaintanenceApplication.class, args);
	}
}
