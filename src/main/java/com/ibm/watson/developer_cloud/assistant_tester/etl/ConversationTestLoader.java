package com.ibm.watson.developer_cloud.assistant_tester.etl;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.ibm.watson.developer_cloud.assistant_tester.ConversationTest;
import com.ibm.watson.developer_cloud.assistant_tester.Turn;

/**
 * Reads a file containing conversational test cases in CSV-like format.
 * 
 * Test cases start with:
 * #test_case_name
 * (optional) context variable key/value pairs to set
 * 
 * Subsequent lines include:
 * utterance
 * (optional) expected output (substring)
 * (optional) expected context variable key/value pairs
 */
public class ConversationTestLoader {

	public List<ConversationTest> read(String filename) {
		List<ConversationTest> tests = new ArrayList<ConversationTest>();
		
		ConversationTest c = null;
		List<String> lines = getLines(filename);
		for(String line : lines) {
			
			if(line.startsWith("#")) {
				String[] context = null;
				String[] parts = line.split(",");
				if(parts.length > 1) {
					context = new String[parts.length - 1];
					for(int i=1; i<parts.length; i++) {
						context[i-1] = parts[i];
					}
				}
				c = new ConversationTest(parts[0], context);
				tests.add(c);
			}
			else if(line.replaceAll(",","").length() == 0) {
				//continue... just a spacing line
			}
			else {
				String[] parts = line.split(",");
				String input = parts[0];
				String output = null;
				if(parts.length > 1) {
					output = parts[1];
				}
				String[] context = null;
				if(parts.length > 2) {
					context = new String[parts.length - 2];
					for(int i=2; i<parts.length; i++) {
						context[i-2] = parts[i];
					}
				}
				Turn t = new Turn(input, output, context);
				c.getTurns().add(t);
			}
			
		}
		
		return tests;
	}
	
	private List<String> getLines(String filename) {
		try {
			List<String> lines = IOUtils.readLines(new FileInputStream(filename));
			//remove BOM
			if(!lines.isEmpty() && lines.get(0).charAt(0) == 65279) {
				lines.set(0, lines.get(0).substring(1));
			}
			return lines;
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
}
