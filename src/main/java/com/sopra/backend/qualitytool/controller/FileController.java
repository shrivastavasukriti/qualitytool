package com.sopra.backend.qualitytool.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sopra.backend.qualitytool.dto.FileDto;
import com.sopra.backend.qualitytool.service.FileService;

@RestController
public class FileController {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);
	
	@Autowired 
	private FileService fileService;
	
	@PostMapping("/quality/processQualityData")
	public void processQualityData(@RequestBody FileDto fileDto) {
		LOGGER.info(" Design Review File path !!" + fileDto.getDesignReviewFilePath());
		fileService.processQualityData(fileDto);
    
	}

}
