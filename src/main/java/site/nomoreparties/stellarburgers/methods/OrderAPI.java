package site.nomoreparties.stellarburgers.methods;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import site.nomoreparties.stellarburgers.requests.CreateOrderRequest;
import site.nomoreparties.stellarburgers.response.GetIngredientResponse;

import static site.nomoreparties.stellarburgers.constants.Config.GET_INGREDIENTS;
import static site.nomoreparties.stellarburgers.constants.Config.GET_ORDERS_FOR_USER;
import static site.nomoreparties.stellarburgers.constants.Config.POST_CREATE_ORDER;
import static io.restassured.RestAssured.given;

public class OrderAPI {
    @Step("Отправка GET-запроса. Получить заказы авторизованного пользователя")
    public static Response getOrdersAuthUserRequest(String accessToken) {
        return given()
                .auth().oauth2(accessToken)
                .get(GET_ORDERS_FOR_USER);
    }
    @Step("Отправка GET-запроса. Получить заказы неавторизованного пользователя")
    public static Response getOrdersUserRequest() {
        return given()
                .get(GET_ORDERS_FOR_USER);
    }
    @Step("Отправка GET-запроса. Получить данные об ингредиентах")
    public static GetIngredientResponse getIngredientsRequest() {
        return given()
                .header("Content-type", "application/json")
                .get(GET_INGREDIENTS)
                .body().as(GetIngredientResponse.class);
    }
    @Step("Отправка POST-запроса. Создание заказа авторизованного пользователя")
    public static Response postCreateOrderAuthUserRequest(String accessToken, CreateOrderRequest createOrderRequest) {
        return given()
                .contentType(ContentType.JSON)
                .auth().oauth2(accessToken)
                .and()
                .body(createOrderRequest)
                .post(POST_CREATE_ORDER);
    }
    @Step("Отправка POST-запроса. Создание заказа неавторизованного пользователя")
    public static Response postCreateOrderUserRequest(CreateOrderRequest createOrderRequest) {
        return given()
                .contentType(ContentType.JSON)
                .and()
                .body(createOrderRequest)
                .post(POST_CREATE_ORDER);
    }
}
