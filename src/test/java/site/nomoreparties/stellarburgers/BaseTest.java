package site.nomoreparties.stellarburgers;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import site.nomoreparties.stellarburgers.requests.CreateUserRequest;

import static org.junit.Assert.assertTrue;
import static site.nomoreparties.stellarburgers.constants.Config.BASE_URI;
import static site.nomoreparties.stellarburgers.methods.UserAPI.deleteUserRequest;
import static site.nomoreparties.stellarburgers.methods.UserAPI.postUserCreateRequest;

public class BaseTest {
    protected CreateUserRequest createUserRequest = userGenerator();
    protected String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }

    public CreateUserRequest userGenerator() {
        return
                new CreateUserRequest(
                        RandomStringUtils.randomAlphabetic(10) + "@mail.ru",
                        RandomStringUtils.randomAlphabetic(10),
                        RandomStringUtils.randomAlphabetic(10)
                );
    }

    public String getAccessTokenAuthUser() {
        Response response = postUserCreateRequest(createUserRequest);
        return accessToken = response.path("accessToken").toString().substring(7);
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            Response response = deleteUserRequest(accessToken);

            assertTrue("Пользователь не удален",
                    response.path("success"));
        }
    }
}