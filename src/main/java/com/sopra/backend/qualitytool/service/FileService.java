package com.sopra.backend.qualitytool.service;

import com.sopra.backend.qualitytool.dto.FileDto;

public interface FileService {
	/**
	 * Method to read folder and extract file names for processing Quality Data 
	 * 
	 * @param fileDto
	 */
	public void inputFolderPaths(FileDto fileDto);
}
