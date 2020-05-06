package com.sopra.backend.qualitytool.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ToolController {
	private static final Logger log = LoggerFactory.getLogger(ToolController.class);
	
	@RequestMapping("/quality/{name}")
	@ResponseBody
	public String test(@PathVariable("name") String name) {
		log.info(" Test Controller Method");
		return name;
	}
}
