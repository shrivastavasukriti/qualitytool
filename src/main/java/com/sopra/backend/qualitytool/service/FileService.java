package com.sopra.backend.qualitytool.service;

import com.sopra.backend.qualitytool.dto.FileDto;

public interface FileService {
    /**
     * Method to process files for Quality Data
     * @param fileDto
     */
	public void processQualityData(FileDto fileDto);
}
