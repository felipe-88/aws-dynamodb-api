package br.com.zup.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.zup.dto.ProductDTO;
import br.com.zup.entity.Product;
import br.com.zup.repository.ProductRepository;
import br.com.zup.service.ProductService;

public class ProductServiceImpl implements ProductService {

	private final ProductRepository repository = new ProductRepository();	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public ProductDTO save(JsonNode body) throws IOException {
		repository.save(objectMapper.treeToValue(body, Product.class));
		return objectMapper.treeToValue(body, ProductDTO.class);		
	}

	@Override
	public Boolean delete(JsonNode body) throws IOException {
		return repository.delete(body.get("id").asText());		
	}

	@Override
	public ProductDTO get(JsonNode body) throws IllegalArgumentException, IOException {
		return objectMapper.convertValue(
								repository.get(body.get("id").asText()), 
								ProductDTO.class);	
	}

	@Override
	public List<ProductDTO> listAll() throws IOException {
		return repository.list().stream()
				.map(p -> objectMapper.convertValue(p, ProductDTO.class))
				.collect(Collectors.toList());		
	}

}
