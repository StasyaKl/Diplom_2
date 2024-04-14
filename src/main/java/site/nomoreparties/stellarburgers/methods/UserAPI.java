package site.nomoreparties.stellarburgers.methods;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import site.nomoreparties.stellarburgers.requests.CreateUserRequest;
import site.nomoreparties.stellarburgers.requests.LoginUserRequest;

import static site.nomoreparties.stellarburgers.constants.Config.*;
import static io.restassured.RestAssured.given;

public class UserAPI {
    @Step("Отправка POST-запроса. Регистрация пользователя")
    public static Response postUserCreateRequest(CreateUserRequest createUserRequest) {
        return given()
                .contentType(ContentType.JSON)
                .and()
                .body(createUserRequest)
                .post(POST_REGISTER_USER);
    }
    @Step("Отправка POST-запроса. Авторизация пользователя")
    public static Response postUserLoginRequest(LoginUserRequest loginUserRequest) {
        return given()
                .contentType(ContentType.JSON)
                .and()
                .body(loginUserRequest)
                .post(POST_LOGIN_USER);
    }

    @Step("Отправка DELETE-запроса. Удаление пользователя")
    public static Response deleteUserRequest(String accessToken) {
        return given()
                .auth().oauth2(accessToken)
                .delete(DELETE_USER);
    }

    @Step("Отправка PATCH-запроса. Изменение данных авторизованного пользователя")
    public static Response patchChangeAuthUserDataRequest(String accessToken, CreateUserRequest createUserRequest) {
        return given()
                .auth().oauth2(accessToken)
                .body(createUserRequest)
                .patch(PATCH_CHANGE_DATA_USER);
    }

    @Step("Отправка PATCH-запроса. Изменение данных неавторизованного пользователя")
    public static Response patchChangeUserDataRequest(CreateUserRequest createUserRequest) {
        return given()
                .body(createUserRequest)
                .patch(PATCH_CHANGE_DATA_USER);
    }
}
