package com.sopra.backend.qualitytool.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sopra.backend.qualitytool.controller.FileController;
import com.sopra.backend.qualitytool.dto.FileDto;
import com.sopra.backend.qualitytool.model.destination.QualityDataColumnsDto;
import com.sopra.backend.qualitytool.model.source.DesignReviewSourceFileDto;
import com.sopra.backend.qualitytool.model.source.QualityDataDto;
import com.sopra.backend.qualitytool.model.source.SuiviCpSourceFileDtoWrapper;
import com.sopra.backend.qualitytool.model.source.TestExecutionSourceFileDto;
import com.sopra.backend.qualitytool.model.source.TestPlanSourceFileDto;
import com.sopra.backend.qualitytool.reader.DesignReviewDataReader;
import com.sopra.backend.qualitytool.reader.SuiviCpDataReader;
import com.sopra.backend.qualitytool.reader.TestExecutionDataReader;
import com.sopra.backend.qualitytool.reader.TestPlanDataReader;
import com.sopra.backend.qualitytool.service.FileService;
import com.sopra.backend.qualitytool.writer.QualityDataWriter;

@Service
public class FileServiceImpl implements FileService {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

	@Autowired
	private DesignReviewDataReader designReviewDataReader;
	@Autowired
	private QualityDataWriter qualityDataWriter;

	@Autowired
	private TestExecutionDataReader testExecutionDataReader;

	@Autowired
	private TestPlanDataReader testPlanDataReader;

	@Autowired
	private SuiviCpDataReader suiviCpDataReader;

	@Value("${destinationFilename}")
	private String destinationFilename;

	
	public void processQualityData(List<FileDto> fileDtoList) {
		try {
			List<QualityDataDto> listQualityData = new ArrayList<>();
			for(FileDto fileDto:fileDtoList){
				
				if (Objects.nonNull(fileDto.getSuiviCpFilePath())) {
					SuiviCpSourceFileDtoWrapper suiviCpSourceFileDtoWrapper = new SuiviCpSourceFileDtoWrapper();
//				List<QualityDataDto> listQualityData = new ArrayList<>();
					/* Reading and verifying SuiviCp file */
					List<XSSFSheet> suiviCPspreadsheetList = suiviCpDataReader.readSuiviCpFile(fileDto.getSuiviCpFilePath(),
							suiviCpSourceFileDtoWrapper);
					if (Objects.nonNull(suiviCPspreadsheetList) && suiviCPspreadsheetList.size() > 0) {
						LOGGER.info("Suivi Cp file read successfully !!!");
						suiviCpDataReader.filteringData(suiviCPspreadsheetList, suiviCpSourceFileDtoWrapper, listQualityData);
					}
					LOGGER.info("Suivi Cp file filtered !!!");
//				QualityDataColumnsDto qualityDataColumnsDto = new QualityDataColumnsDto();
//				XSSFSheet qualityDataSheet = qualityDataWriter.readQualityFile(destinationFilename,
//						qualityDataColumnsDto);
//				if (Objects.nonNull(qualityDataSheet)) {
//					qualityDataWriter.updateOrCreateRecord(qualityDataColumnsDto,
//							qualityDataSheet, listQualityData);
//						Boolean flagWriteFile = Boolean.FALSE;
//						flagWriteFile = qualityDataWriter.writeToFile(destinationFilename);
//						if (flagWriteFile) {
//							LOGGER.info("Quality Data file written for Efforts!!\n\n");
//						}
//				}
				}
				
				
				if (Objects.nonNull(fileDto.getDesignReviewFilePath())) {
//				List<QualityDataDto> listQualityData = new ArrayList<>();
					DesignReviewSourceFileDto designReviewSourceFile = new DesignReviewSourceFileDto();
					/* Reading and verifying source file */
					XSSFSheet designReviewSheet = designReviewDataReader.readDesignReviewFile(fileDto.getDesignReviewFilePath(),
							designReviewSourceFile);
					if (Objects.nonNull(designReviewSheet)) {
						LOGGER.info("Review Report read succesfully !!");
						designReviewDataReader.filteringData(designReviewSheet, designReviewSourceFile, listQualityData);
						/* Writing to file */
//					if (Objects.nonNull(listQualityData)) {
//						LOGGER.info("Review Report filtered !!!");
//						QualityDataColumnsDto qualityDataColumnsDto = new QualityDataColumnsDto();
//						XSSFSheet qualityDataSheet = qualityDataWriter.readQualityFile(destinationFilename,
//								qualityDataColumnsDto);
//						if (Objects.nonNull(qualityDataSheet)) {
//							qualityDataWriter.updateOrCreateRecord(qualityDataColumnsDto,
//								qualityDataSheet, listQualityData);
//								Boolean flagWriteFile = Boolean.FALSE;
//								flagWriteFile = qualityDataWriter.writeToFile(destinationFilename);
//								if (flagWriteFile) {
//									LOGGER.info("Quality Data file written for Design/Code Phase!!\n\n");
//								}
//						}
//					}
					}
				}
				
				if (Objects.nonNull(fileDto.getTestExecutionFilePath())) {
//				List<QualityDataDto> listQualityData = new ArrayList<>();
					TestExecutionSourceFileDto testExecutionSourceFile = new TestExecutionSourceFileDto();
					/* Reading and verifying Test Execution file */
					XSSFSheet testExecutionSheet = testExecutionDataReader
							.readTestExecutionFile(fileDto.getTestExecutionFilePath(), testExecutionSourceFile);
					if (Objects.nonNull(testExecutionSheet)) {
						LOGGER.info("Test Execution file read successfully !!!");
						testExecutionDataReader.filteringData(testExecutionSheet, testExecutionSourceFile, listQualityData);
						/* Writing to file */
//					if (Objects.nonNull(listQualityData)) {
//						LOGGER.info("Test Execution file filtered !!!");
//						QualityDataColumnsDto QualityDataColumnsDto = new QualityDataColumnsDto();
//						XSSFSheet qualityDataSheet = qualityDataWriter.readQualityFile(destinationFilename,
//								QualityDataColumnsDto);
//						if (Objects.nonNull(qualityDataSheet)) {
//							qualityDataWriter.updateOrCreateRecord(QualityDataColumnsDto,
//									qualityDataSheet, listQualityData);
//								Boolean flagWriteFile = Boolean.FALSE;
//								flagWriteFile = qualityDataWriter.writeToFile(destinationFilename);
//								if (flagWriteFile) {
//									LOGGER.info("Quality Data file written for Test Execution Phase!!\n\n");
//								}
//						}
//					}
					}
				}
				if (Objects.nonNull(fileDto.getTestPlanFilePath())) {
//				List<QualityDataDto> listQualityData = new ArrayList<>();
					TestPlanSourceFileDto testPlanSourceFileDto = new TestPlanSourceFileDto();
					/* Reading and verifying Test Plan file */
					XSSFSheet testPlanSheet = testPlanDataReader.readTestPlanFile(fileDto.getTestPlanFilePath(),
							testPlanSourceFileDto);
					if (Objects.nonNull(testPlanSheet)) {
						LOGGER.info("Test Plan file read successfully !!!");
						testPlanDataReader.filteringData(testPlanSheet, testPlanSourceFileDto, listQualityData);
						/* Writing to file */
//					if (Objects.nonNull(listQualityData)) {
//						LOGGER.info("Test Plan file filtered !!!");
//						QualityDataColumnsDto qualityDataColumnsDto = new QualityDataColumnsDto();
//						XSSFSheet qualityDataSheet = qualityDataWriter.readQualityFile(destinationFilename,
//								qualityDataColumnsDto);
//						if (Objects.nonNull(qualityDataSheet)) {
//							qualityDataWriter.updateOrCreateRecord(qualityDataColumnsDto,
//									qualityDataSheet, listQualityData);
//								Boolean flagWriteFile = Boolean.FALSE;
//								flagWriteFile = qualityDataWriter.writeToFile(destinationFilename);
//								if (flagWriteFile) {
//									LOGGER.info("Quality Data file written for Test Plan Phase!!");
//								}
//						}
//					}
					}
				}
			}
			System.out.println("\n\n\n"+listQualityData);
			if (Objects.nonNull(listQualityData)) {
				QualityDataColumnsDto qualityDataColumnsDto = new QualityDataColumnsDto();
				XSSFSheet qualityDataSheet = qualityDataWriter.readQualityFile(destinationFilename,
						qualityDataColumnsDto);
				if (Objects.nonNull(qualityDataSheet)) {
					qualityDataWriter.updateOrCreateRecord(qualityDataColumnsDto,
							qualityDataSheet, listQualityData);
					Boolean flagWriteFile = Boolean.FALSE;
					flagWriteFile = qualityDataWriter.writeToFile(destinationFilename);
					if (flagWriteFile) {
						LOGGER.info("Quality Data file written !!");
					}
				}
			}
			LOGGER.info("\n\n!! Application Terminated !!");
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
				e.printStackTrace();
			}
		}

	
	@Override
	public void inputFolderPaths(FileDto folderPaths){
		System.out.println("inside input Folder paths");
		List<FileDto> filePathsList = new ArrayList<>();
		if (Objects.nonNull(folderPaths.getDesignReviewFilePath())) {
			File designReviewFolderPath = new File(folderPaths.getDesignReviewFilePath());
			for (File file : designReviewFolderPath.listFiles()) {
				FileDto filePaths = new FileDto();
				Long currentDate = new Date().getTime(); 
				System.out.println(file.getName());
				Long lastModified = file.lastModified();
				System.out.println(file.lastModified());
				if((currentDate - lastModified)>0){
					filePaths.setDesignReviewFilePath(file.getPath());
					filePathsList.add(filePaths);
					
				}
				
			}
		}
		System.out.println(filePathsList);
		processQualityData(filePathsList);
		
	}

}
