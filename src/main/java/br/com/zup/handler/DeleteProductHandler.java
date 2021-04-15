package br.com.zup.handler;

import static br.com.zup.enums.ProductServiceEnum.GET_SERVICE_IMPL;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.zup.response.ApiGatewayResponse;
import br.com.zup.response.Response;
import br.com.zup.service.ProductService;

public class DeleteProductHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private final Logger logger = Logger.getLogger(this.getClass());

	private ProductService service = GET_SERVICE_IMPL.getService();

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

		try {
			// get the 'body' from input
			JsonNode body = new ObjectMapper().readTree((String) input.get("body"));

			Boolean success = service.delete(body);

			// send the response back
			if (success) {
				return ApiGatewayResponse.builder()
						.setStatusCode(204)
						.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
						.build();
			} else {
				return ApiGatewayResponse.builder()
						.setStatusCode(404)
						.setObjectBody("Product with id: '" + body.get("id").asText() + "' not found.")
						.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
						.build();
			}
		} catch (IOException ex) {
			logger.error("Error in deleting product: " + ex);

			// send the error response back
			Response responseBody = new Response("Error in deleting product: ", input);
			return ApiGatewayResponse.builder()
					.setStatusCode(500)
					.setObjectBody(responseBody)
					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
					.build();
		}
	}
}
