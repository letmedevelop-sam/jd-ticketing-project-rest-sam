package com.cybertek;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//Container will handle bean staff
@SpringBootApplication  //@SpringBootApplication covers @ComponentScan - @Configuration  - @EnableAutoConfiguration
public class TicketingProjectRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketingProjectRestApplication.class, args);
	}

	@Bean			//@SpringBootApplication covers @ComponentScan - @Configuration  - @EnableAutoConfiguration
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}


}
