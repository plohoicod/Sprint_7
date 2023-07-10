import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderClient {

    static final String ORDERS_API = "/api/v1/orders";
    @Step("Отправка GET в /api/v1/orders")
    public Response getOrderList() {
        return given().get(ORDERS_API);
    }
    @Step("Отправка GET в /api/v1/orders?courierId={}")
    public Response getOrderListWithCourierId(Integer id) {
        return given().get(ORDERS_API + "?courierId=" + id);
    }

    @Step("Отправка PUT в /api/v1/orders/cancel?track={}")
    public void deleteOrder(Integer orderNumber) {
        given().put(ORDERS_API + "/cancel?track="  + orderNumber);
    }

    @Step("Отправка POST в /api/v1/orders")
    public Response createOrder(List<String> color) {
        return given()
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
                .post(ORDERS_API);
    }
}
