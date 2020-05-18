package com.sopra.backend.qualitytool.service.impl;

import java.util.Objects;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sopra.backend.qualitytool.controller.FileController;
import com.sopra.backend.qualitytool.dto.FileDto;
import com.sopra.backend.qualitytool.model.destination.QualityDataWriteDto;
import com.sopra.backend.qualitytool.model.source.DesignReviewSourceFileDto;
import com.sopra.backend.qualitytool.model.source.QualityDataDto;
import com.sopra.backend.qualitytool.reader.DesignReviewDataReader;
import com.sopra.backend.qualitytool.service.FileService;
import com.sopra.backend.qualitytool.writer.QualityDataWriter;

@Service
public class FileServiceImpl implements FileService{

	private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);
	
	@Autowired
	private DesignReviewDataReader designReviewDataReader;
	@Autowired
	private QualityDataWriter designReviewDataWriter;

	@Value("${destinationFilename}")
	private String destinationFilename;

	
	private FileServiceImpl() {
	}

	@Override
	public void processQualityData(FileDto fileDto) {
		try {
			if (Objects.nonNull(fileDto.getDesignReviewFilePath())) {
				QualityDataDto qualityDataDto = new QualityDataDto();
				DesignReviewSourceFileDto designReviewSourceFile = new DesignReviewSourceFileDto();
				/* Reading and verifying source file */
				XSSFSheet designReviewSheet = designReviewDataReader.readDesignReviewFile(fileDto.getDesignReviewFilePath(),designReviewSourceFile);
				if (Objects.nonNull(designReviewSheet)) {
					LOGGER.info("File read succesfully !!");					
					Boolean flagDataUpdated = designReviewDataReader.populateQualityDataDto(designReviewSheet,designReviewSourceFile,qualityDataDto);
					if(flagDataUpdated){
				    	designReviewDataReader.filteringData(designReviewSheet,designReviewSourceFile,qualityDataDto);
					}
				/* Writing to file */ 
				if(Objects.nonNull(qualityDataDto)){
					XSSFSheet qualityDataSheet = designReviewDataWriter.readQualityFile(destinationFilename,
							qualityDataDto);
						if (Objects.nonNull(qualityDataSheet)){
							QualityDataWriteDto qualityDataWriteDto = new QualityDataWriteDto();
							Boolean recordCreated=designReviewDataWriter.updateOrCreateRecord(qualityDataDto, qualityDataWriteDto, qualityDataSheet);
							if(recordCreated)
							designReviewDataWriter.writeToFile(destinationFilename);
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}

}
