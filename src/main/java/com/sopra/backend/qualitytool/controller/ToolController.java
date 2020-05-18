package com.sopra.backend.qualitytool.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ToolController {
	private static final Logger log = LoggerFactory.getLogger(ToolController.class);

	// @RequestMapping("/quality/{name}")
	// public String test(@PathVariable("name") String name) {
	// log.info(" Test Controller Method");
	// return name;
	// }

	@RequestMapping("/quality/readXLSFile")
	public void readXLSFile(@RequestParam("file") MultipartFile excelDataFile) {
		log.info(" Read Excel File");
		try {
			System.out.println(excelDataFile.getInputStream().toString());
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

}
