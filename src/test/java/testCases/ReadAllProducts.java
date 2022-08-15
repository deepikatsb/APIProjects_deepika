package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.util.concurrent.TimeUnit;

public class ReadAllProducts {

	@Test
	public void readAllProducts() {
		/*
		 given : All input details(baseURI, Headers, Authorization, payload/Body, QueryParameters)
		 when : Submit api requests(HTTP method, ENdpoint/Resource)
		 Then: validate response(status code,Headers, responseTime, Payload/Body)
		 https://techfios.com/api-prod/api/product /read.php
		 */
		Response response = 
		
		given()
				.baseUri("https://techfios.com/api-prod/api/product")
				.header("Content-Type","application/json")
				.auth().preemptive().basic("demo@techfios.com", "abc123").
		when()
				.get("/read.php").
		then()
				.extract().response();
		
		int statusCode = response.getStatusCode();
		System.out.println("Status code is " +statusCode);
		Assert.assertEquals(statusCode, 200);
		
		long responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
		System.out.println("Response Time : " + responseTime);
		if(responseTime < 5000) {
			System.out.println("Response time is within Range");
		}else {
			System.out.println("Response time is out of range");
		}
		
		String responseHeader = response.getHeader("Content-Type");
		System.out.println("Response Header : " +responseHeader);
		Assert.assertEquals(responseHeader, "application/json; charset=UTF-8", "Wrong header");
		
		String responseBody = response.getBody().asString();
		JsonPath jsonPath = new JsonPath(responseBody);
		String firstProductID = jsonPath.getString("records[0].id");
		System.out.println("firstProductID : " + firstProductID);
		if(firstProductID!=null) {
			System.out.println("Records are not NULL");
		}else {
			System.out.println("Records are null");
		}
				
		
	}
	
}
