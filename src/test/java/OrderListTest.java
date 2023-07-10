import dto.OrderListResponseDto;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(JUnit4.class)
public class OrderListTest {

    private final OrderClient orderClient = new OrderClient();

    @Before
    public void setUp() {
        RestAssured.baseURI = TestData.BASE_URL;
    }
    @Test
    @DisplayName("Проверка успешного получения списка заказов")
    @Description("Проверка возврата статуса 200 и списка заказов")
    public void getOrderListSuccess() {
        Assert.assertNotNull(orderClient.getOrderList().then().statusCode(HttpStatus.SC_OK).extract().as(OrderListResponseDto.class));
    }

    @Test
    @DisplayName("Проверка ошибки получения списка заказов")
    @Description("Проверка возврата статуса 404 и сообщения 'Курьер с идентификатором -1 не найден'")
    public void getOrderListWithWrongCourierIdFailed() {
        orderClient.getOrderListWithCourierId(-1).then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .and()
                .body("code", equalTo(404))
                .and()
                .body("message", equalTo("Курьер с идентификатором -1 не найден"));
    }
}
