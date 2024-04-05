package site.nomoreparties.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import site.nomoreparties.stellarburgers.requests.CreateUserRequest;

import static site.nomoreparties.stellarburgers.constants.ErrorMessage.DO_NOT_FILL_REQUIRE_FIELD;
import static site.nomoreparties.stellarburgers.constants.ErrorMessage.USER_ALREADY_EXISTS;
import static site.nomoreparties.stellarburgers.methods.UserAPI.deleteUserRequest;
import static site.nomoreparties.stellarburgers.methods.UserAPI.postUserCreateRequest;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CreateUserTest extends BaseTest {
    private final CreateUserRequest createUserRequest = userGenerator();
    public String accessToken;

    @Test
    @DisplayName("Метод проверки регистрации уникального пользователя")
    @Description("Проверка правильного кода ответа (200) и _______________")
    public void checkUserCreate() {
        Response response = postUserCreateRequest(createUserRequest);

        response.then()
                .assertThat()
                .statusCode(SC_OK);

        accessToken = response.path("accessToken").toString().substring(7);
        System.out.println(accessToken);

        assertTrue("Пользователь не смог зарегистрироваться",
                response.path("success"));
    }

    @Test
    @DisplayName("Метод проверки регистрации пользователя, который уже зарегистрирован")
    @Description("Проверка правильного кода ответа (403) и _______________")
    public void checkUserCreateAlreadyRegistered() {
        CreateUserRequest createUserRequestDouble = new CreateUserRequest(
                createUserRequest.getEmail(),
                createUserRequest.getPassword(),
                createUserRequest.getName()
        );

        postUserCreateRequest(createUserRequest);
        Response response = postUserCreateRequest(createUserRequestDouble);

        response.then()
                .assertThat()
                .statusCode(SC_FORBIDDEN);

        assertEquals(USER_ALREADY_EXISTS, response.path("message"));
    }

    @Test
    @DisplayName("Метод проверки регистрации пользователя при условии, что не заполненно одно из обязательных полей")
    @Description("Проверка правильного кода ответа (403) и _______________")
    public void checkUserCreateWithoutFilledRequiredField() {
        createUserRequest.setName(null);

        Response response = postUserCreateRequest(createUserRequest);

        response.then()
                .assertThat()
                .statusCode(SC_FORBIDDEN);

        assertEquals(DO_NOT_FILL_REQUIRE_FIELD, response.path("message"));
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
