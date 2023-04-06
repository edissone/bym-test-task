## Startup notes
### Testing
- For running API tests is required to follow step-by-step:
1. `docker compose -f compose-test.yml up -d` - startup test docker compose environment 
2. Once elasticsearch cluster in container has been started - perform queries from `/dumps/elastic/initiate-elastic.http`
3. Run test-class

All data used for tests is present in `/src/test/resources/data/test-data.json`

## Task description

1. Create a Spring-boot service application with REST CRUD methods for the following entities: Category, Product, Order
   and OrderItem.

- Category contains: name and a list of products.
- Product contains: price, sku and name.
- Order Item contains: “quantity” value and has connection to Product.
- Order contains: a list of order items and a total amount of the order.

2. Add report controller, which returns date and sum of income for each day in JSON format (2016-08-22: 250.65,
   2016-08.23: 571.12 ... etc).
3. Integrate ElasticSearch and add “search” method, which must return the list of orders by part of the product name.
   Technology stack: Spring-boot, Hibernate, Liquibase, PostgreSQL, ElasticSearch.
   Will be a plus:

- add appropriate tests to REST methods;
- add integration with Swagger.