package site.nomoreparties.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static site.nomoreparties.stellarburgers.constants.ErrorMessage.REQUEST_WITHOUT_AUTH;
import static site.nomoreparties.stellarburgers.methods.UserAPI.*;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChangeUserDataTest extends BaseTest {
    @Test
    @DisplayName("Метод проверки изменения всех полей авторизованного пользователя")
    @Description("Проверка правильного кода ответа (200) и соответствующего сообщения")
    public void checkChangeAllFieldsAuthUser() {
        getAccessTokenAuthUser();

        createUserRequest.setEmail(RandomStringUtils.randomAlphabetic(10) + "@mail.ru");
        createUserRequest.setPassword(RandomStringUtils.randomAlphabetic(10));
        createUserRequest.setName(RandomStringUtils.randomAlphabetic(10));

        Response response = patchChangeAuthUserDataRequest(accessToken, createUserRequest);
        response.then()
                .assertThat()
                .statusCode(SC_OK);

        assertTrue("Пользователь не смог изменить данные",
                response.path("success"));
    }

    @Test
    @DisplayName("Метод проверки изменения всех полей неавторизованного пользователя")
    @Description("Проверка правильного кода ответа (401) и соответствующего сообщения")
    public void checkChangeAllFieldsUser() {
        Response response = patchChangeUserDataRequest(createUserRequest);
        response.then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED);

        assertEquals(REQUEST_WITHOUT_AUTH, response.path("message"));
    }
}