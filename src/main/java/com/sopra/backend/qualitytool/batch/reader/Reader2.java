package com.sopra.backend.qualitytool.batch.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;


public class Reader2 implements ItemReader<String>{

	private String[] messages = {"Hello Tool Demo 2!", "Spring Batch 2!"};
	
	private int count=0;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		
		if(count < messages.length){
			return messages[count++];
		}else{
			count=0;
		}
		return null;
	}
	
}