package site.nomoreparties.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Test;
import site.nomoreparties.stellarburgers.requests.CreateUserRequest;

import static site.nomoreparties.stellarburgers.constants.ErrorMessage.REQUEST_WITHOUT_AUTH;
import static site.nomoreparties.stellarburgers.methods.UserAPI.*;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChangeUserDataTest extends BaseTest {
    private final CreateUserRequest createUserRequest = userGenerator();
    private String accessToken;

    @Test
    @DisplayName("Метод проверки изменения всех полей авторизованного пользователя")
    @Description("Проверка правильного кода ответа (200) и _______________")
    public void checkUserCreate() {
        Response response = postUserCreateRequest(createUserRequest);

        accessToken = response.path("accessToken").toString().substring(7);
        System.out.println(accessToken);

        createUserRequest.setEmail(RandomStringUtils.randomAlphabetic(10) + "@mail.ru");
        createUserRequest.setPassword(RandomStringUtils.randomAlphabetic(10));
        createUserRequest.setName(RandomStringUtils.randomAlphabetic(10));

        Response response1 = patchChangeAuthUserDataRequest(accessToken, createUserRequest);

        response1.then()
                .assertThat()
                .statusCode(SC_OK);

        assertTrue("Пользователь не смог изменить данные",
                response.path("success"));
    }

    @Test
    @DisplayName("Метод проверки изменения всех полей неавторизованного пользователя")
    @Description("Проверка правильного кода ответа (401) и _______________")
    public void checkUserCreateAlreadyRegistered() {
        Response response = patchChangeUserDataRequest(createUserRequest);

        response.then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED);

        assertEquals(REQUEST_WITHOUT_AUTH, response.path("message"));
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
