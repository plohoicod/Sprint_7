import dto.CreateOrderResponseDto;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
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
public class CreateOrderTest extends ScooterBaseTest {

    private final List<String> color;

    private Integer orderNumber;

    public CreateOrderTest(List<String> color) {
        this.color = color;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = TestData.baseUrl;
    }

    @After
    @Step("Отправка PUT в /api/v1/orders/cancel?track=")
    public void cleanData() {
        given().put("/api/v1/orders/cancel?track="  + orderNumber);
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {Collections.emptyList()},
                {List.of("\"BLACK\"")},
                {List.of("\"GREY\"")},
                {Arrays.asList("\"BLACK", "GREY\"")}
        };
    }

    @Test
    @DisplayName("Проверка успешного созадния заказа")
    @Description("Проверка возврата статуса 201 и track")
    @Step("Отправка POST в /api/v1/orders")
    public void createOrderSuccess() {
        orderNumber = given()
                .header("Content-type", "application/json")
                .and()
                .body("{\n" +
                        "    \"firstName\": \"Naruto\",\n" +
                        "    \"lastName\": \"Uchiha\",\n" +
                        "    \"address\": \"Konoha, 142 apt.\",\n" +
                        "    \"metroStation\": 4,\n" +
                        "    \"phone\": \"+7 800 355 35 35\",\n" +
                        "    \"rentTime\": 5,\n" +
                        "    \"deliveryDate\": \"2020-06-06\",\n" +
                        "    \"comment\": \"Saske, come back to Konoha\",\n" +
                        "    \"color\": "+ color.toString() +
                        "}")
                .when()
                .post("/api/v1/orders")
                .then()
                .statusCode(201)
                .and()
                .body("track", notNullValue())
                .extract().as(CreateOrderResponseDto.class).getTrack();
    }
}
