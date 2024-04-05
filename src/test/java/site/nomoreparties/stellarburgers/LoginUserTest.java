package site.nomoreparties.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.requests.CreateUserRequest;
import site.nomoreparties.stellarburgers.requests.LoginUserRequest;

import static site.nomoreparties.stellarburgers.constants.ErrorMessage.*;
import static site.nomoreparties.stellarburgers.methods.UserAPI.*;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginUserTest extends BaseTest {
    private final CreateUserRequest createUserRequest = userGenerator();
    private LoginUserRequest loginUserRequest;
    private String accessToken;

    @Before
    public void userLoginGenerator() {
        loginUserRequest = new LoginUserRequest(
                createUserRequest.getEmail(),
                createUserRequest.getPassword()
        );
    }

    @Test
    @DisplayName("Метод проверки авторизации под существующим пользователем")
    @Description("Проверка правильного кода ответа (200) и _______________")
    public void checkUserCreateAlreadyRegistered() {
        Response responseCreate = postUserCreateRequest(createUserRequest);
        Response responseLogin = postUserLoginRequest(loginUserRequest);

        responseLogin.then()
                .assertThat()
                .statusCode(SC_OK);

        accessToken = responseCreate.path("accessToken").toString().substring(7);
        System.out.println(accessToken);

        assertTrue("Пользователь не смог залогиниться",
                responseLogin.path("success"));
    }

    @Test
    @DisplayName("Метод проверки авторизации с неверным логином и паролем")
    @Description("Проверка правильного кода ответа (401) и _______________")
    public void checkUserCreateWithoutFilledRequiredField() {
        loginUserRequest.setPassword("check_test");

        Response response = postUserLoginRequest(loginUserRequest);

        response.then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED);

        assertEquals(INCORRECT_DATA_EMAIL_OR_PASSWORD, response.path("message"));
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
