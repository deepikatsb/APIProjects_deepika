package testCases;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class CreateOneProduct {
	/*
	 * given : All input details(baseURI, Headers, Authorization, payload/Body,
	 * QueryParameters) when : Submit api requests(HTTP method, ENdpoint/Resource)
	 * Then: validate response(status code,Headers, responseTime, Payload/Body)
	 * https://techfios.com/api-prod/api/product /read.php 03. CreateOneProduct HTTP
	 * Method : POST EndpointURL =
	 * https://techfios.com/api-prod/api/product/create.php Authorization: Basic
	 * auth = username,password Headers: Content-Type = application/json;
	 * charset=UTF-8 Payload/Body: { "name" : "Amazing Pillow 2.0 by TSB", "price" :
	 * "199", "description" : "The best pillow for amazing programmers.",
	 * "category_id" : 2, "category_name": "Electronics" } Status Code:201
	 */
String baseuri = "https://techfios.com/api-prod/api/product";
String endPointURI = "/create.php";
HashMap<String, String> payloadBodyData;
String expectedResponseHeader = "application/json; charset=UTF-8";
String expectedBodyMessage = "Product was created.";
String firstProductID;

SoftAssert softAssert = new SoftAssert();

public HashMap<String, String> getCreatePayloadMethod() {
	
	payloadBodyData = new HashMap<String, String>();
	payloadBodyData.put("name", "Alan Walker Album");
	payloadBodyData.put("price", "199");
	payloadBodyData.put("description", "The best Songs for amazing programmers.");
	payloadBodyData.put("category_id", "5");
	
	return payloadBodyData;
}
@Test(priority =1)
public void createOneProduct() {
	
	Response response = 
	given()
			.baseUri(baseuri)
			.header("Content-Type","application/json; charset=UTF-8")
			
			.body(getCreatePayloadMethod()).
	when()
			.post(endPointURI).
	then()
			.extract().response();
	

	int statusCode = response.getStatusCode();
	softAssert.assertEquals(statusCode, 201, "Status Code not Matching");
	
	String actualResponseheader = response.getHeader("Content-Type");
	softAssert.assertEquals(actualResponseheader, expectedResponseHeader,"Response Headers does not Match !!");
	
	long actualResponseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
	if(actualResponseTime < 2000)
		System.out.println("Response time in Range");
	else
		System.out.println("Response Time out of Range");
	
	String responseBody = response.getBody().asString();
	JsonPath jsonPath = new JsonPath(responseBody);
	String actualBodyMessage = jsonPath.getString("message");
	System.out.println(actualBodyMessage);
	softAssert.assertEquals(actualBodyMessage, expectedBodyMessage, "Body Message does not Match");
	
	softAssert.assertAll();
	
}
@Test(priority =2)
public void readAllProducts() {
	
	Response response = 
	
	given()
			.baseUri("https://techfios.com/api-prod/api/product")
			.header("Content-Type","application/json")
			.auth().preemptive().basic("demo@techfios.com", "abc123").
	when()
			.get("/read.php").
	then()
			.extract().response();
	
	String responseBody = response.getBody().asString();
	JsonPath jsonPath = new JsonPath(responseBody);
	firstProductID = jsonPath.get("records[0].id");
}
@Test(priority =3)
public void readOneProduct() {
	Response response =

			given()
					.baseUri("https://techfios.com/api-prod/api/product")
					.header("Content-Type", "application/json")
					.header("Authorization","Beader kslmdfmskldfmsdn")
					.queryParam("id", firstProductID).
			when()
					.get("/read_one.php").
			then()
					.extract().response();
	
	String responseBody = response.getBody().asString();
	JsonPath jsonPath = new JsonPath(responseBody);
	
	String productID = jsonPath.getString("id");
	softAssert.assertEquals(productID, firstProductID);
	
	String productName = jsonPath.getString("name");
	softAssert.assertEquals(productName, getCreatePayloadMethod().get("name"));
	
	String productDescription = jsonPath.getString("description");
	softAssert.assertEquals(productDescription, getCreatePayloadMethod().get("description"));
	
	String productPrice = jsonPath.getString("price");
	softAssert.assertEquals(productPrice, getCreatePayloadMethod().get("price"));
	
	String productCategory_id = jsonPath.getString("category_id");
	softAssert.assertEquals(productCategory_id, getCreatePayloadMethod().get("category_id"));
		
	softAssert.assertAll();

}


}
