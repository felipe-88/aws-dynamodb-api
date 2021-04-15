package br.com.zup.service;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import br.com.zup.dto.ProductDTO;

public interface ProductService {

	ProductDTO save(JsonNode body) throws IOException;
	
	Boolean delete(JsonNode body) throws IOException;
	
	ProductDTO get(JsonNode body) throws IllegalArgumentException, IOException;
	
	List<ProductDTO> listAll() throws IOException;
}
