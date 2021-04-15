package br.com.zup.repository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import br.com.zup.adapter.DynamoDBAdapter;
import br.com.zup.entity.Product;

public class ProductRepository {

	// get the table name from env. var. set in serverless.yml
    private static final String PRODUCTS_TABLE_NAME = System.getenv("PRODUCTS_TABLE_NAME");

    private static DynamoDBAdapter dbAdapter;
    private final AmazonDynamoDB client;
    private final DynamoDBMapper mapper;
    
    private Logger logger = Logger.getLogger(this.getClass());
    
	public ProductRepository() {
		// build the mapper config
        DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
            .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(PRODUCTS_TABLE_NAME))
            .build();
        // get the db adapter
        this.dbAdapter = DynamoDBAdapter.getInstance();
        this.client = this.dbAdapter.getDbClient();
        // create the mapper with config
        this.mapper = this.dbAdapter.createDbMapper(mapperConfig);
	}
    
	 // methods
    public Boolean ifTableExists() {
        return this.client.describeTable(PRODUCTS_TABLE_NAME).getTable().getTableStatus().equals("ACTIVE");
    }

    public List<Product> list() throws IOException {
      DynamoDBScanExpression scanExp = new DynamoDBScanExpression();
      List<Product> results = this.mapper.scan(Product.class, scanExp);
      for (Product p : results) {
        logger.info("Products - list(): " + p.toString());
      }
      return results;
    }

    public Product get(String id) throws IOException {
        Product product = null;

        HashMap<String, AttributeValue> av = new HashMap<String, AttributeValue>();
        av.put(":v1", new AttributeValue().withS(id));

        DynamoDBQueryExpression<Product> queryExp = new DynamoDBQueryExpression<Product>()
            .withKeyConditionExpression("id = :v1")
            .withExpressionAttributeValues(av);

        PaginatedQueryList<Product> result = this.mapper.query(Product.class, queryExp);
        if (result.size() > 0) {
          product = result.get(0);
          logger.info("Products - get(): product - " + product.toString());
        } else {
          logger.info("Products - get(): product - Not Found.");
        }
        return product;
    }

    public void save(Product product) throws IOException {
        logger.info("Products - save(): " + product.toString());
        this.mapper.save(product);
    }

    public Boolean delete(String id) throws IOException {
        Product product = null;

        // get product if exists
        product = get(id);
        if (product != null) {
          logger.info("Products - delete(): " + product.toString());
          this.mapper.delete(product);
        } else {
          logger.info("Products - delete(): product - does not exist.");
          return false;
        }
        return true;
    }
    
}
