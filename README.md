# Mintyn Assessment
## Task 1
________________________________________________________________________________________________________________________________________________________

## Project Requirements:
________________________________________________________________________________________________________________________________________________________

1. [Apache Kafka](https://kafka.apache.org/)
2. [MongoDB](https://www.mongodb.com/)
3. [Apache Maven 3.6.3](https://maven.apache.org/)
4. [Java 17](https://sdkman.io/jdks) (I highly recommend install Java 17 and Maven from SDKMAN)


## Running the Project
________________________________________________________________________________________________________________________________________________________

-  Find the`docker-compose.yml` file is the root directory. The file contains commands that pull public images of Kafka and ZooKeeper.
The file configures Kafka to run on `localhost:29029`.


- Locate the `application-xxx.properties` file under the _src/main/resources_ directory and rename it to `application-local.properties`.
replace the **<MONGO_DATABASE_URI>** placeholder with the URI of a database running on your local engine


- Start the project using maven: `mvn spring-boot:run`


________________________________________________________________________________________________________________________________________________________
## Controller Endpoints

1. `POST - /orders`:
    - create a new order in repository. The new order is in _PENDING_ state.
    - if quantity of items to be order for a product in the order is greater than the stock level, the order set as _FAILED_,
   saved to the database and a response is sent to the client saying that the product is out of stock.
    - else if the stock levels for all the products are not exceeded by the order quantities of each product, 
   then the stock levels of each product in the order is updated.
   - order is sent via a _process_ topic to a _ProcessOrderWorker_ which makes the order as _SUCCESSFUL_ and then saves it into the database
   - the now successful order is now sent via a _report_ topic to Order Reporting App.


2. `POST - /products`:
   create a new product.


3. `GET- /products?pageSize=&pageNumber=`: get all products via pagination. Default value of _pageSize_ is 5 and the default value of _pageNumber_ is 0.


4. `PATCH- /products`: update product