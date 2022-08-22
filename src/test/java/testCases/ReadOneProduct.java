package testCases;

import static io.restassured.RestAssured.given;

import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ReadOneProduct {

	@Test
	public void readOneProduct() {
		/*
		 * given : All input details(baseURI, Headers, Authorization, payload/Body,
		 * QueryParameters) when : Submit api requests(HTTP method, ENdpoint/Resource)
		 * Then: validate response(status code,Headers, responseTime, Payload/Body)
		 * https://techfios.com/api-prod/api/product /read.php 
		 * 
		 * 02. ReadOneProduct
		 * HTTP Method : GET EndpointURL =
		 * https://techfios.com/api-prod/api/product/read_one.php?id=65 Query Params:
		 * id=5019 Authorization: Basic auth = username,password Headers: Content-Type =
		 * application/json Status Code:200
		 */
SoftAssert softAssert = new SoftAssert();
		Response response =

				given()
						.baseUri("https://techfios.com/api-prod/api/product")
						.header("Content-Type", "application/json")
						.header("Authorization","Beader kslmdfmskldfmsdn")
						.queryParam("id", "5031").
				when()
						.get("/read_one.php").
				then()
						.extract().response();
		int responseStatusCode = response.getStatusCode();
		System.out.println("Status Code : " + responseStatusCode);
		Assert.assertEquals(responseStatusCode, 200);

		String responseHeader = response.getHeader("Content-Type");
		System.out.println("Header Type : " + responseHeader);
		Assert.assertEquals("application/json", responseHeader);

		long responseResponseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
		System.out.println("Response time : " + responseResponseTime);
		if (responseResponseTime < 5000) {
			System.out.println("Response time within Range");
		} else {
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

		System.out.println("Product ID : " + productID+"\nProduct Name : " + productName);
		Assert.assertEquals(productID, "5031");
		Assert.assertEquals(productName, "Amazing Pillow 5.0 by Deepika");
		
		System.out.println("Product Description : " + productDescription);
		softAssert.assertEquals(productDescription, "The best pillow for amkhjazing programmers.");
		
		System.out.println("Product Price :" + productPrice);
		Assert.assertEquals(productPrice, "299");
		
		System.out.println("Product Category ID : " + productCategory_id);
		Assert.assertEquals(productCategory_id, "2");
		
		System.out.println("Product Category Name : " + productCategory_name);
		Assert.assertEquals(productCategory_name, "Electronics");
		
		softAssert.assertAll();

	}

}
