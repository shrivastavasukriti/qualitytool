package com.sopra.backend.qualitytool.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "com.sopra.backend.qualitytool")
@EntityScan("com.sopra.backend.qualitytool.entities")
@EnableJpaRepositories(basePackages = "com.sopra.backend.qualitytool.repository") 
public class ToolsApplication {

	private static final Logger log = LoggerFactory.getLogger(ToolsApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ToolsApplication.class, args);
		log.info("!! Application Started !!");
	}

}