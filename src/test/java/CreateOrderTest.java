import dto.CreateOrderResponseDto;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    private final List<String> color;

    private Integer orderNumber;

    private final OrderClient orderClient = new OrderClient();

    public CreateOrderTest(List<String> color) {
        this.color = color;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = TestData.BASE_URL;
    }

    @After
    public void cleanData() {
        orderClient.deleteOrder(orderNumber);
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {List.of()},
                {List.of("\"BLACK\"")},
                {List.of("\"GREY\"")},
                {List.of("\"BLACK", "GREY\"")}
        };
    }

    @Test
    @DisplayName("Проверка успешного созадния заказа")
    @Description("Проверка возврата статуса 201 и track")
    public void createOrderSuccess() {
        orderNumber = orderClient.createOrder(color)
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .and()
                .body("track", notNullValue())
                .extract().as(CreateOrderResponseDto.class).getTrack();
    }
}
