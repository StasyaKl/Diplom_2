package site.nomoreparties.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import site.nomoreparties.stellarburgers.requests.CreateOrderRequest;
import site.nomoreparties.stellarburgers.response.Datas;
import site.nomoreparties.stellarburgers.response.GetIngredientResponse;

import java.util.ArrayList;
import java.util.List;

import static site.nomoreparties.stellarburgers.constants.ErrorMessage.ORDER_WITHOUT_INGREDIENTS;
import static site.nomoreparties.stellarburgers.methods.OrderAPI.*;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CreateOrderTest extends BaseTest {
    @Test
    @DisplayName("Метод проверки создания заказа авторизованного пользователя с ингридиентами")
    @Description("Проверка правильного кода ответа (200) и success: true")
    public void checkCreateOrderAuthUser() {
        getAccessTokenAuthUser();
        List<String> idsOfIngredients = getListOfIngredientsIds(3);
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(idsOfIngredients);

        Response response = postCreateOrderAuthUserRequest(accessToken, createOrderRequest);
        response.then()
                .assertThat()
                .statusCode(SC_OK);

        assertTrue("Авторизованный пользователь не смог сделать заказ",
                response.path("success"));
    }

    @Test
    @DisplayName("Метод проверки создания заказа неавторизованного пользователя с ингредиентами")
    @Description("Проверка правильного кода ответа (200) и success: true")
    public void checkCreateOrder() {
        List<String> idsOfIngredients = getListOfIngredientsIds(2);
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(idsOfIngredients);

        Response response = postCreateOrderUserRequest(createOrderRequest);
        response.then()
                .assertThat()
                .statusCode(SC_OK);

        assertTrue("Неавторизованный пользователь не смог сделать заказ",
                response.path("success"));
    }

    @Test
    @DisplayName("Метод проверки создания заказа неавторизованного пользователя без ингредиентов")
    @Description("Проверка правильного кода ответа (400) и соответствующего сообщения")
    public void checkCreateOrderWithoutIngredients() {
        List<String> ids = getListOfIngredientsIds(0);
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(ids);

        Response response = postCreateOrderUserRequest(createOrderRequest);
        response.then()
                .assertThat()
                .statusCode(SC_BAD_REQUEST);

        assertEquals(ORDER_WITHOUT_INGREDIENTS, response.path("message"));
    }

    @Test
    @DisplayName("Метод проверки создания заказа неавторизованного пользователя с неверным хешем ингредиентов")
    @Description("Проверка правильного кода ответа (500)")
    public void checkCreateOrderWithWrongHash() {
        List<String> ids = new ArrayList<>();
        ids.add("61c0c5a71d1f82001bdaaa6fwerg");
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(ids);

        Response response = postCreateOrderUserRequest(createOrderRequest);
        response.then()
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    public List<String> getListOfIngredientsIds(int N) {
        GetIngredientResponse getIngredient = getIngredientsRequest();

        Assert.assertNotNull("Не удалось десериализовать информацию о пользователе", getIngredient);

        List<Datas> datas = getIngredient.getData();
        List<String> ids = new ArrayList<>();

        for (int i = 0; i < N && i < datas.size(); i++) {
            Datas data = datas.get(i);
            ids.add(data.get_id());
        }

        return ids;
    }
}