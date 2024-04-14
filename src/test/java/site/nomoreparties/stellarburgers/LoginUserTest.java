package site.nomoreparties.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.requests.LoginUserRequest;

import static site.nomoreparties.stellarburgers.constants.ErrorMessage.*;
import static site.nomoreparties.stellarburgers.methods.UserAPI.*;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginUserTest extends BaseTest {
    private LoginUserRequest loginUserRequest;

    @Before
    public void userLoginGenerator() {
        loginUserRequest = new LoginUserRequest(
                createUserRequest.getEmail(),
                createUserRequest.getPassword()
        );
    }

    @Test
    @DisplayName("Метод проверки авторизации под существующим пользователем")
    @Description("Проверка правильного кода ответа (200) и соответствующего сообщения")
    public void checkLoginUserAlreadyRegistered() {
        getAccessTokenAuthUser();

        Response response = postUserLoginRequest(loginUserRequest);
        response.then()
                .assertThat()
                .statusCode(SC_OK);

        assertTrue("Пользователь не смог залогиниться",
                response.path("success"));
    }

    @Test
    @DisplayName("Метод проверки авторизации с неверным логином и паролем")
    @Description("Проверка правильного кода ответа (401) и соответствующего сообщения")
    public void checkLoginUserWithNonexistentProfile() {
        loginUserRequest.setPassword("check_test");
        loginUserRequest.setEmail("test@ya.ru");

        Response response = postUserLoginRequest(loginUserRequest);

        response.then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED);

        assertEquals(INCORRECT_DATA_EMAIL_OR_PASSWORD, response.path("message"));
    }
}
