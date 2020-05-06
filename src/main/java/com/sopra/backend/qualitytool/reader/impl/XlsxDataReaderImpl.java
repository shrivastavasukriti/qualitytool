package com.sopra.backend.qualitytool.reader.impl;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.sopra.backend.qualitytool.dto.Dto;

public class XlsxDataReaderImpl {

	public List<Dto> read(String file) {
		List<Dto> list = new ArrayList<>();
		try (XSSFWorkbook book = new XSSFWorkbook(new FileInputStream(file));) {
			XSSFSheet sheet = book.getSheetAt(0);
			//TODO
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
}
