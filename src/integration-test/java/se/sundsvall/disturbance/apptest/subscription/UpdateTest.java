package se.sundsvall.disturbance.apptest.subscription;

import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.OK;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import se.sundsvall.disturbance.Application;

/**
 * Update subscription application tests
 *
 * @see src/test/resources/db/scripts/testdata.sql for data setup.
 */
@WireMockAppTestSuite(files = "classpath:/subscription/UpdateTest/", classes = Application.class)
@Sql(scripts = {
	"/db/scripts/truncate.sql",
	"/db/scripts/testdata-it.sql"
})
class UpdateTest extends AbstractAppTest {

	@Test
	void test1_updateSubscriptionAddOutOutSettings() throws IOException {

		setupCall()
			.withServicePath("/subscriptions/1")
			.withHttpMethod(PUT)
			.withRequest("request.json")
			.withExpectedResponseStatus(OK)
			.withExpectedResponse("response.json")
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test2_updateSubscriptionRemoveOutOutSettings() throws IOException {

		setupCall()
			.withServicePath("/subscriptions/1")
			.withHttpMethod(PUT)
			.withRequest("request.json")
			.withExpectedResponseStatus(OK)
			.withExpectedResponse("response.json")
			.sendRequestAndVerifyResponse();
	}
}
