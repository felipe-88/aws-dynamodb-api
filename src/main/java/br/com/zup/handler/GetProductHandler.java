package br.com.zup.handler;

import static br.com.zup.enums.ProductServiceEnum.GET_SERVICE_IMPL;

import java.util.Collections;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.zup.dto.ProductDTO;
import br.com.zup.response.ApiGatewayResponse;
import br.com.zup.response.Response;
import br.com.zup.service.ProductService;

public class GetProductHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private final Logger logger = Logger.getLogger(this.getClass());
	
	private ProductService service = GET_SERVICE_IMPL.getService();

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

		try {
			// get the 'body' from input
			JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
			
			ProductDTO productDTO = service.get(body);

			// send the response back
			if (productDTO != null) {
				return ApiGatewayResponse.builder()
						.setStatusCode(200)
						.setObjectBody(productDTO)
						.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
						.build();
			} else {
				return ApiGatewayResponse.builder()
						.setStatusCode(404)
						.setObjectBody("Product with id: '" + body.get("id").asText() + "' not found.")
						.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
						.build();
			}
		} catch (Exception ex) {
			logger.error("Error in retrieving product: " + ex);

			// send the error response back
			Response responseBody = new Response("Error in retrieving product: ", input);
			return ApiGatewayResponse.builder()
					.setStatusCode(500)
					.setObjectBody(responseBody)
					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
					.build();
		}
	}
}
