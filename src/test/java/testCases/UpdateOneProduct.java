package testCases;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UpdateOneProduct {
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
	String endPointURIUpdate = "/update.php";
	String endPointURICreate = "/create.php";
	HashMap<String, String> payloadBodyData;
	HashMap<String, String> updatepayloadBodyData;
	String expectedResponseHeader = "application/json; charset=UTF-8";
	String expectedBodyMessage = "Product was created.";
	String expectedResponseMessage = "Product was updated.";
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

	public HashMap<String, String> updatePayloadMethod() {

		updatepayloadBodyData = new HashMap<String, String>();

		updatepayloadBodyData.put("id", firstProductID);
		updatepayloadBodyData.put("name", "Chick Pea Album");
		updatepayloadBodyData.put("price", "555");
		updatepayloadBodyData.put("description", "The best Songs for amazing programmers.");
		updatepayloadBodyData.put("category_id", "5");

		return updatepayloadBodyData;
	}

	@Test(priority = 1)
	public void createOneProduct() {

		Response response = given().baseUri(baseuri).header("Content-Type", "application/json; charset=UTF-8")
				.body(getCreatePayloadMethod()).when().post(endPointURICreate).then().extract().response();

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

				given().baseUri("https://techfios.com/api-prod/api/product").header("Content-Type", "application/json")
						.auth().preemptive().basic("demo@techfios.com", "abc123").when().get("/read.php").then()
						.extract().response();

		String responseBody = response.getBody().asString();
		JsonPath jsonPath = new JsonPath(responseBody);
		firstProductID = jsonPath.get("records[0].id");
	}

	@Test(priority = 3)
	public void updateOneProduct() {
		Response response = given().baseUri(baseuri).header("Content-Type", "application/json; charset=UTF-8")
				.body(updatePayloadMethod()).when().put(endPointURIUpdate).then().extract().response();

		int actualStatusCode = response.getStatusCode();
		softAssert.assertEquals(actualStatusCode, 200, " Update Status code misMatch !");

		String actualResponseBody = response.getBody().asString();
		JsonPath jsonPath = new JsonPath(actualResponseBody);

		String actualResponseMessage = jsonPath.getString("message");
		softAssert.assertEquals(actualResponseMessage, expectedResponseMessage,
				"Update Response Message does not match!");
	}

	@Test(priority = 4)
	public void validateIfUpdated() {
		Response response =

				given().baseUri(baseuri).header("Content-Type", "application/json")
						.header("Authorization", "Beader kslmdfmskldfmsdn").queryParam("id", firstProductID).when()
						.get("/read_one.php").then().extract().response();

		String responseBody = response.getBody().asString();
		JsonPath jsonPath = new JsonPath(responseBody);

		String productID = jsonPath.getString("id");
		softAssert.assertEquals(productID, firstProductID);

		String productName = jsonPath.getString("name");
		softAssert.assertEquals(productName, updatePayloadMethod().get("name"));

		String productDescription = jsonPath.getString("description");
		softAssert.assertEquals(productDescription, updatePayloadMethod().get("description"));

		String productPrice = jsonPath.getString("price");
		softAssert.assertEquals(productPrice, updatePayloadMethod().get("price"));

		String productCategory_id = jsonPath.getString("category_id");
		softAssert.assertEquals(productCategory_id, updatePayloadMethod().get("category_id"));

		softAssert.assertAll();
	}

}
