package br.com.zup.response;

import java.util.Map;

import lombok.Data;

@Data
public class Response {

	private final String message;
	private final Map<String, Object> input;
	
}
