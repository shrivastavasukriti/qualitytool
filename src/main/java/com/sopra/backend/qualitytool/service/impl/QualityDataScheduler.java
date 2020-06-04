package com.sopra.backend.qualitytool.service.impl;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sopra.backend.qualitytool.dto.FileDto;
import com.sopra.backend.qualitytool.service.FileService;

@Component
public class QualityDataScheduler{
	private static final Logger LOGGER = LoggerFactory.getLogger(QualityDataScheduler.class);

	@Autowired
	private FileService fileService;

	@Scheduled(cron = "0 */1 * * * ?")
	public void qualityDatascheduler() {
		FileDto fileDto = new FileDto();
		LOGGER.info("Quality Data Processing started at : " + Instant.now());
		fileService.processQualityData(fileDto);
		LOGGER.info("Quality Data Processing completed at : " + Instant.now());
	}

}
