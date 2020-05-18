package com.sopra.backend.qualitytool.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.sopra.backend.qualitytool")
public class ToolsApplication {

	private static final Logger log = LoggerFactory.getLogger(ToolsApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ToolsApplication.class, args);
		log.info("!! Application Started !!");
	}

}