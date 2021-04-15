package br.com.zup.handler;

import static br.com.zup.enums.ProductServiceEnum.GET_SERVICE_IMPL;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import br.com.zup.dto.ProductDTO;
import br.com.zup.entity.Product;
import br.com.zup.response.ApiGatewayResponse;
import br.com.zup.response.Response;
import br.com.zup.service.ProductService;

public class ListProductsHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private final Logger logger = Logger.getLogger(this.getClass());
	
	private ProductService service = GET_SERVICE_IMPL.getService();

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		try {
			
			List<ProductDTO> dtoList = service.listAll();
			// send the response back
			return ApiGatewayResponse.builder()
					.setStatusCode(200)
					.setObjectBody(dtoList)
					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
					.build();
		} catch (Exception ex) {
			logger.error("Error in listing products: " + ex);

			// send the error response back
			Response responseBody = new Response("Error in listing products: ", input);
			return ApiGatewayResponse.builder()
					.setStatusCode(500)
					.setObjectBody(responseBody)
					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
					.build();
		}
	}
}
