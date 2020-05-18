package com.sopra.backend.qualitytool.service.impl;

import java.util.ArrayList;
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
import com.sopra.backend.qualitytool.model.source.EffortsDto;
import com.sopra.backend.qualitytool.model.source.QualityDataDto;
import com.sopra.backend.qualitytool.model.source.SuiviCpSourceFileDto;
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

	@Override
	public void processQualityData(FileDto fileDto) {
		try {
			List<EffortsDto> effortsDtoList = new ArrayList<>();
			if (Objects.nonNull(fileDto.getSuiviCpFilePath())) {
				List<SuiviCpSourceFileDto> suiviCpSourceFileDtoList = new ArrayList<>();
				/* Reading and verifying SuiviCp file */
				List<XSSFSheet> suiviCPspreadsheetList = suiviCpDataReader.readSuiviCpFile(fileDto.getSuiviCpFilePath(),
						suiviCpSourceFileDtoList);
				if (Objects.nonNull(suiviCPspreadsheetList) && suiviCPspreadsheetList.size() > 0) {
					LOGGER.info("Suivi Cp file read successfully !!!");
					suiviCpDataReader.filteringData(suiviCPspreadsheetList, suiviCpSourceFileDtoList, effortsDtoList);
				}
			}

			if (Objects.nonNull(fileDto.getDesignReviewFilePath())) {
				List<QualityDataDto> listQualityData = new ArrayList<>();
				DesignReviewSourceFileDto designReviewSourceFile = new DesignReviewSourceFileDto();
				/* Reading and verifying source file */
				XSSFSheet designReviewSheet = designReviewDataReader.readDesignReviewFile(fileDto.getDesignReviewFilePath(),
						designReviewSourceFile);
				if (Objects.nonNull(designReviewSheet)) {
					LOGGER.info("Review Report read succesfully !!");
					designReviewDataReader.filteringData(designReviewSheet, designReviewSourceFile, listQualityData);
					/* Writing to file */
					if (Objects.nonNull(listQualityData)) {
						LOGGER.info("Review Report filtered successfully !!!");
						QualityDataColumnsDto qualityDataColumnsDto = new QualityDataColumnsDto();
						XSSFSheet qualityDataSheet = qualityDataWriter.readQualityFile(destinationFilename,
								qualityDataColumnsDto);
						if (Objects.nonNull(qualityDataSheet)) {
							List<Boolean> recordCreatedList = qualityDataWriter.updateOrCreateRecord(qualityDataColumnsDto,
									qualityDataSheet, listQualityData, effortsDtoList, listQualityData.get(0).getPhase());
							if (!recordCreatedList.contains(Boolean.FALSE)) {
								Boolean flagWriteFile = Boolean.FALSE;
								flagWriteFile = qualityDataWriter.writeToFile(destinationFilename);
								if (flagWriteFile) {
									LOGGER.info("Quality Data file written successfully for Design/Code Phase!!");
								}
							}
						}
					}
				}
			}

			if (Objects.nonNull(fileDto.getTestExecutionFilePath())) {
				List<QualityDataDto> listQualityData = new ArrayList<>();
				TestExecutionSourceFileDto testExecutionSourceFile = new TestExecutionSourceFileDto();
				/* Reading and verifying Test Execution file */
				XSSFSheet testExecutionSheet = testExecutionDataReader
						.readTestExecutionFile(fileDto.getTestExecutionFilePath(), testExecutionSourceFile);
				if (Objects.nonNull(testExecutionSheet)) {
					LOGGER.info("Test Execution sheet read successfully !!!");
					testExecutionDataReader.filteringData(testExecutionSheet, testExecutionSourceFile, listQualityData);
					LOGGER.info("\n\n\n\nlist quality data, size: " + listQualityData.size());
					for (QualityDataDto quality : listQualityData) {
						LOGGER.info(quality.getGroup() + " " + quality.toString());

					}
					/* Writing to file */
					if (Objects.nonNull(listQualityData)) {
						LOGGER.info("Test Execution sheet filtered successfully !!!");
						QualityDataColumnsDto QualityDataColumnsDto = new QualityDataColumnsDto();
						XSSFSheet qualityDataSheet = qualityDataWriter.readQualityFile(destinationFilename,
								QualityDataColumnsDto);
						if (Objects.nonNull(qualityDataSheet)) {
							LOGGER.info("Quality Data sheet read successfully!!");
							List<Boolean> recordCreatedList = qualityDataWriter.updateOrCreateRecord(QualityDataColumnsDto,
									qualityDataSheet, listQualityData, effortsDtoList, listQualityData.get(0).getPhase());
							if (!recordCreatedList.contains(Boolean.FALSE)) {
								Boolean flagWriteFile = Boolean.FALSE;
								flagWriteFile = qualityDataWriter.writeToFile(destinationFilename);
								if (flagWriteFile) {
									LOGGER.info("Quality Data file written successfully for Test Execution Phase!!");
								}
							}
						}
					}
				}
			}
			if (Objects.nonNull(fileDto.getTestPlanFilePath())) {
				List<QualityDataDto> listQualityData = new ArrayList<>();
				TestPlanSourceFileDto testPlanSourceFileDto = new TestPlanSourceFileDto();
				/* Reading and verifying Test Plan file */
				XSSFSheet testPlanSheet = testPlanDataReader.readTestPlanFile(fileDto.getTestPlanFilePath(),
						testPlanSourceFileDto);
				LOGGER.info(testPlanSourceFileDto.getClassOfBugColumnIndex().toString());
				if (Objects.nonNull(testPlanSheet)) {
					testPlanDataReader.filteringData(testPlanSheet, testPlanSourceFileDto, listQualityData);
					LOGGER.info(listQualityData.get(0).getGroup());
					/* Writing to file */
					if (Objects.nonNull(listQualityData)) {
						QualityDataColumnsDto qualityDataColumnsDto = new QualityDataColumnsDto();
						XSSFSheet qualityDataSheet = qualityDataWriter.readQualityFile(destinationFilename,
								qualityDataColumnsDto);
						if (Objects.nonNull(qualityDataSheet)) {
							LOGGER.info("phase:" + listQualityData.get(0).getPhase());
							List<Boolean> recordCreatedList = qualityDataWriter.updateOrCreateRecord(qualityDataColumnsDto,
									qualityDataSheet, listQualityData, effortsDtoList, listQualityData.get(0).getPhase());
							if (!recordCreatedList.contains(Boolean.FALSE)) {
								Boolean flagWriteFile = Boolean.FALSE;
								flagWriteFile = qualityDataWriter.writeToFile(destinationFilename);
								if (flagWriteFile) {
									LOGGER.info("Quality Data file written successfully for Test Plan Phase!!");
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}

	}

}
