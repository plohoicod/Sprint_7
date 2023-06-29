import dto.OrderListResponseDto;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(JUnit4.class)
public class OrderListTest extends ScooterBaseTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = TestData.baseUrl;
    }
    @Test
    @DisplayName("Проверка успешного получения списка заказов")
    @Description("Проверка возврата статуса 200 и списка заказов")
    @Step("Отправка GET в /api/v1/orders")
    public void getOrderListSuccess() {
        Assert.assertNotNull(given().get("/api/v1/orders").then().statusCode(200).extract().as(OrderListResponseDto.class));
    }

    @Test
    @DisplayName("Проверка ошибки получения списка заказов")
    @Description("Проверка возврата статуса 404 и сообщения 'Курьер с идентификатором -1 не найден'")
    @Step("Отправка GET в /api/v1/orders?courierId=-1")
    public void getOrderListWithWrongCourierIdFailed() {
        given().get("/api/v1/orders?courierId=-1").then()
                .statusCode(404)
                .body("code", equalTo(404))
                .and()
                .body("message", equalTo("Курьер с идентификатором -1 не найден"));
    }
}
