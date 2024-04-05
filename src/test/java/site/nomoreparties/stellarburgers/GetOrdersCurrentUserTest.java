package site.nomoreparties.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static site.nomoreparties.stellarburgers.constants.ErrorMessage.REQUEST_WITHOUT_AUTH;
import static site.nomoreparties.stellarburgers.methods.OrderAPI.getOrdersAuthUserRequest;
import static site.nomoreparties.stellarburgers.methods.OrderAPI.getOrdersUserRequest;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GetOrdersCurrentUserTest extends BaseTest {
    @Test
    @DisplayName("Метод проверки получения заказов авторизованного пользователя")
    @Description("Проверка правильного кода ответа (200) и _______________")
    //можно сделать интереснее и добавить добавление заказов и проверку количества этих заказов в ответе
    public void checkUserCreate() {
        getAccessTokenAuthUser();

        Response response = getOrdersAuthUserRequest(accessToken);
        response.then()
                .assertThat()
                .statusCode(SC_OK);

        assertTrue("Пользователь не смог получить список заказов",
                response.path("success"));
    }

    @Test
    @DisplayName("Метод проверки получения заказов неавторизованного пользователя")
    @Description("Проверка правильного кода ответа (401) и соответствующего сообщения")
    public void checkUserCreateAlreadyRegistered() {
        Response response = getOrdersUserRequest();
        response.then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED);

        assertEquals(REQUEST_WITHOUT_AUTH, response.path("message"));
    }
}
