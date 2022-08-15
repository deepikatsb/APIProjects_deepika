package testCases;

import static io.restassured.RestAssured.given;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ReadOneProduct {

	@Test
	public void readOneProduct() {
		/*
		 given : All input details(baseURI, Headers, Authorization, payload/Body, QueryParameters)
		 when : Submit api requests(HTTP method, ENdpoint/Resource)
		 Then: validate response(status code,Headers, responseTime, Payload/Body)
		 https://techfios.com/api-prod/api/product /read.php
		 */
		
		Response response = 
				
				given()
						.baseUri("https://techfios.com/api-prod/api/product")
						.header("Content-Type", "application/json")
						.auth().preemptive().basic("demo@techfios.com", "abc123")
						.queryParam("id", "5031").
				when()
						.get("/read_one.php").
				then()
						.extract().response();
		int responseStatusCode = response.getStatusCode();
		System.out.println("Status Code : " + responseStatusCode);
		Assert.assertEquals(responseStatusCode,200);
		
		String responseHeader = response.getHeader("Content-Type");
		System.out.println("Header Type : " + responseHeader);
		Assert.assertEquals("application/json", responseHeader);
		
		long responseResponseTime = response.getTime();
		System.out.println("Response time : " + responseResponseTime);
		if(responseResponseTime < 5000) {
			System.out.println("Response time within Range");
		}else {
			System.out.println("Response time is out of range");
		}
		
		String responseBody = response.getBody().asString();
		JsonPath jsonPath = new JsonPath(responseBody);
		String productID = jsonPath.getString("id");
		String productName = jsonPath.getString("name");
		String productDescription = jsonPath.getString("description");
		String productPrice = jsonPath.getString("price");
		String productCategory_id = jsonPath.getString("category_id");
		String productCategory_name = jsonPath.getString("category_name");
		
		System.out.println("Product ID : " + productID + "\n Product Name : " + productName);
		System.out.println("Product Description : " + productDescription);
		System.out.println("Product Price" + productPrice);
		System.out.println("Product Category ID : "+ productCategory_id);
		System.out.println("Product Category Name : "+ productCategory_name);
		
	}
	
}
