package site.nomoreparties.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import site.nomoreparties.stellarburgers.requests.CreateUserRequest;

import static site.nomoreparties.stellarburgers.constants.ErrorMessage.REQUEST_WITHOUT_AUTH;
import static site.nomoreparties.stellarburgers.methods.OrderAPI.getOrdersAuthUserRequest;
import static site.nomoreparties.stellarburgers.methods.OrderAPI.getOrdersUserRequest;
import static site.nomoreparties.stellarburgers.methods.UserAPI.*;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GetOrdersCurrentUserTest extends BaseTest {
    private final CreateUserRequest createUserRequest = userGenerator();
    private String accessToken;

    @Test
    @DisplayName("Метод проверки получения заказов авторизованного пользователя")
    @Description("Проверка правильного кода ответа (200) и _______________")
    //можно сделать интереснее и добавить добавление заказов и проверку количества этих заказов в ответе
    public void checkUserCreate() {
        Response response = postUserCreateRequest(createUserRequest);

        accessToken = response.path("accessToken").toString().substring(7);
        System.out.println(accessToken);

        Response response1 = getOrdersAuthUserRequest(accessToken);

        response1.then()
                .assertThat()
                .statusCode(SC_OK);

        assertTrue("Пользователь не смог получить список заказов",
                response.path("success"));
    }

    @Test
    @DisplayName("Метод проверки получения заказов неавторизованного пользователя")
    @Description("Проверка правильного кода ответа (401) и _______________")
    public void checkUserCreateAlreadyRegistered() {
        Response response = getOrdersUserRequest();

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
