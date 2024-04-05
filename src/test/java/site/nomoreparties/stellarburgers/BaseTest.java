package site.nomoreparties.stellarburgers;

import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import site.nomoreparties.stellarburgers.requests.CreateUserRequest;

import static site.nomoreparties.stellarburgers.constants.Config.BASE_URI;

public class BaseTest {
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
}
