package testCases;

import static io.restassured.RestAssured.given;

import java.util.HashMap;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class DeleteOneProduct {
	/*
	 * given : All input details(baseURI, Headers, Authorization, payload/Body,
	 * QueryParameters) when : Submit api requests(HTTP method, ENdpoint/Resource)
	 * Then: validate response(status code,Headers, responseTime, Payload/Body)
	 * https://techfios.com/api-prod/api/product /read.php UpdateOneProduct HTTP
	 * Method : PUT EndpointURL =
	 * https://techfios.com/api-prod/api/product/update.php Authorization: Basic
	 * auth = username,password Headers: Content-Type = application/json;
	 * charset=UTF-8 Payload/Body: { "id" : "5104", "name" : "Amazing Pillow 3.0",
	 * "price" : "255", "description" : "The best pillow for amazing programmers.",
	 * "category_id" : 2, "created" : "2018-08-01 00:35:07" } Status Code:201
	 */
	String baseuri = "https://techfios.com/api-prod/api/product";
	String endPointURIDelete = "/delete.php";
	String endPointURICreate = "/create.php";
	HashMap<String, String> payloadBodyData;
	HashMap<String, String> deletepayloadBodyData;
	String expectedResponseHeader = "application/json; charset=UTF-8";
	String expectedBodyMessage = "Product was created.";
	String expectedResponseMessage = "Product was deleted.";
	String expectedResponseAfterDeletion = "Product does not exist.";
	String deleteProductID;

	SoftAssert softAssert = new SoftAssert();

	public HashMap<String, String> getCreatePayloadMethod() {

		payloadBodyData = new HashMap<String, String>();
		payloadBodyData.put("name", "Alan Walker Album");
		payloadBodyData.put("price", "199");
		payloadBodyData.put("description", "The best Songs for amazing programmers.");
		payloadBodyData.put("category_id", "5");

		return payloadBodyData;
	}

	public HashMap<String, String> deletePayloadMethod() {

		deletepayloadBodyData = new HashMap<String, String>();

		deletepayloadBodyData.put("id", deleteProductID);
		

		return deletepayloadBodyData;
	}

	@Test(priority = 1)
	public void createOneProduct() {

		Response response = 
				given()
						.baseUri(baseuri)
						.header("Content-Type", "application/json; charset=UTF-8")
						.body(getCreatePayloadMethod()).
				when()
						.post(endPointURICreate).
				then()
						.extract().response();

		int statusCode = response.getStatusCode();
		softAssert.assertEquals(statusCode, 201, "Status Code not Matching");

		String actualResponseheader = response.getHeader("Content-Type");
		softAssert.assertEquals(actualResponseheader, expectedResponseHeader, "Response Headers does not Match !!");

		String responseBody = response.getBody().asString();
		JsonPath jsonPath = new JsonPath(responseBody);
		String actualBodyMessage = jsonPath.getString("message");
		System.out.println(actualBodyMessage);
		softAssert.assertEquals(actualBodyMessage, expectedBodyMessage, "Body Message does not Match");

		softAssert.assertAll();
	}
	@Test(priority = 2)
	public void readAllProducts() {
		Response response =
				given()
						.baseUri("https://techfios.com/api-prod/api/product")
						.header("Content-Type", "application/json")
						.auth().preemptive().basic("demo@techfios.com", "abc123").
				when()
						.get("/read.php").
				then()
						.extract().response();

		String responseBody = response.getBody().asString();
		JsonPath jsonPath = new JsonPath(responseBody);
		deleteProductID = jsonPath.get("records[0].id");
	}

	@Test(priority = 3)
	public void deleteOneProduct() {
		Response response = 
				given()
						.baseUri(baseuri)
						.header("Content-Type", "application/json; charset=UTF-8")
						.body(deletePayloadMethod()).
				when()
						.put(endPointURIDelete).
				then()
						.extract().response();

		int actualStatusCode = response.getStatusCode();
		softAssert.assertEquals(actualStatusCode, 200, " Update Status code misMatch !");

		String actualResponseBody = response.getBody().asString();
		JsonPath jsonPath = new JsonPath(actualResponseBody);

		String actualResponseMessage = jsonPath.getString("message");
		softAssert.assertEquals(actualResponseMessage, expectedResponseMessage,
				"Update Response Message does not match!");
		
		softAssert.assertAll();
	}

	@Test(priority = 4)
	public void validateIfDeleted() {
		Response response =

				given()
						.baseUri("https://techfios.com/api-prod/api/product")
						.header("Content-Type", "application/json")
						.header("Authorization", "Beader kslmdfmskldfmsdn")
						.queryParam("id", deleteProductID).
				when()
						.get("/read_one.php").
				then()
						.extract().response();

		int actualDeleteResponseStatusCode = response.getStatusCode();
		softAssert.assertEquals(actualDeleteResponseStatusCode, 404, "Deleted Status code does not Match!");
		
		String responseBody = response.getBody().asString();
		JsonPath jsonPath = new JsonPath(responseBody);

		String actualResponseAfterDeletion = jsonPath.getString("message");
		softAssert.assertEquals(actualResponseAfterDeletion, expectedResponseAfterDeletion, "Product Deletion Message Not Matching!");

		softAssert.assertAll();
	}

}
