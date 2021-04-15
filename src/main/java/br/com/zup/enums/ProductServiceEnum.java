package br.com.zup.enums;

import br.com.zup.service.ProductService;
import br.com.zup.service.impl.ProductServiceImpl;

public enum ProductServiceEnum {

	GET_SERVICE_IMPL {

		@Override
		public ProductService getService() {			
			return new ProductServiceImpl();
		}
		
	};
	
	public abstract ProductService getService();
}
