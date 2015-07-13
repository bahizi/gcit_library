package com.gcit.lms.dao;

import com.gcit.lms.domain.Author;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
public class Tes {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Author> authors = new ArrayList<Author>();
		Author auth = new Author();
		auth.setAuthorId(2);
		auth.setAuthorName("Andrei");
		Author auth2 = new Author();
		auth2.setAuthorId(1);
		HashMap<String, Boolean> a = new HashMap<String, Boolean>();
		a.put("ok", false);
		auth2.setAuthorName("Bogdan");
		authors.add(auth);
		authors.add(auth2);
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			String json = ow.writeValueAsString(a);
			System.out.println(json);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
