package site.nomoreparties.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import site.nomoreparties.stellarburgers.requests.CreateOrderRequest;
import site.nomoreparties.stellarburgers.requests.CreateUserRequest;
import site.nomoreparties.stellarburgers.response.Datas;
import site.nomoreparties.stellarburgers.response.GetIngredientResponse;

import java.util.ArrayList;
import java.util.List;

import static site.nomoreparties.stellarburgers.constants.ErrorMessage.ORDER_WITHOUT_INGREDIENTS;
import static site.nomoreparties.stellarburgers.methods.OrderAPI.*;
import static site.nomoreparties.stellarburgers.methods.UserAPI.deleteUserRequest;
import static site.nomoreparties.stellarburgers.methods.UserAPI.postUserCreateRequest;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CreateOrderTest extends BaseTest {
    private final CreateUserRequest createUserRequest = userGenerator();
    private String accessToken;
    @Test
    @DisplayName("Метод проверки создания заказа авторизованного пользователя с ингридиентами")
    @Description("Проверка правильного кода ответа (200) и _______________")
    public void checkCreateOrderAuthUser() {
        Response response = postUserCreateRequest(createUserRequest);

        accessToken = response.path("accessToken").toString().substring(7);
        System.out.println(accessToken);

        List<String> ids = getListOfIngredientsIds(3);
        System.out.println(ids);

        CreateOrderRequest createOrderRequest = new CreateOrderRequest(ids);
        Response response1 = postCreateOrderAuthUserRequest(accessToken, createOrderRequest);

        response1.then()
                .assertThat()
                .statusCode(SC_OK);

        assertTrue("Неавторизованный пользователь не смог сделать заказ",
                response.path("success"));
    }

    @Test
    @DisplayName("Метод проверки создания заказа неавторизованного пользователя с ингредиентами")
    @Description("Проверка правильного кода ответа (200) и _______________")
    public void checkCreateOrder() {
        List<String> ids = getListOfIngredientsIds(2);
        System.out.println(ids);

        CreateOrderRequest createOrderRequest = new CreateOrderRequest(ids);
        Response response = postCreateOrderUserRequest(createOrderRequest);

        response.then()
                .assertThat()
                .statusCode(SC_OK);

        assertTrue("Неавторизованный пользователь не смог сделать заказ",
                response.path("success"));
    }

    @Test
    @DisplayName("Метод проверки создания заказа неавторизованного пользователя без ингредиентов")
    @Description("Проверка правильного кода ответа (400) и _______________")
    public void checkCreateOrderWithoutIngredients() {
        List<String> ids = getListOfIngredientsIds(0);
        System.out.println(ids);

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
        System.out.println(ids);

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

        // Получаем только первые N элементов списка datas
        for (int i = 0; i < N && i < datas.size(); i++) {
            Datas data = datas.get(i);
            ids.add(data.get_id());
            System.out.println("id: " + data.get_id());
        }

        return ids;
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
