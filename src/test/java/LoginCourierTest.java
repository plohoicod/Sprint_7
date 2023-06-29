import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(JUnit4.class)
public class LoginCourierTest {

    private final CourierClient courierClient = new CourierClient();

    @Before
    public void setUp() {
        RestAssured.baseURI = TestData.BASE_URL;
        courierClient.createCourier(courierClient.createCourierDto);
    }

    @After
    public void cleanData() {
        courierClient.deleteCourier(TestData.LOGIN_1, TestData.PASSWORD_1);
    }

    @Test
    @DisplayName("Проверка успешной авторизации курьера")
    @Description("Проверка возврата статуса 200 и id курьера")
    public void loginCourierSuccess() {
        courierClient.loginCourier(TestData.LOGIN_1, TestData.PASSWORD_1).then().assertThat()
                .body("id", notNullValue())
                .and()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Проверка авторизации курьера c неверным паролем")
    @Description("Проверка возврата статуса 404 и сообщения 'Учетная запись не найдена'")
    public void loginCourierWrongPassFail() {
        courierClient.loginCourier(TestData.LOGIN_1, TestData.PASSWORD_2).then().assertThat()
                .body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Проверка авторизации курьера c неверным логином")
    @Description("Проверка возврата статуса 404 и сообщения 'Учетная запись не найдена'")
    public void loginCourierWrongLoginFail() {
        courierClient.loginCourier(TestData.LOGIN_2, TestData.PASSWORD_1).then().assertThat()
                .body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Проверка авторизации несуществующего курьера")
    @Description("Проверка возврата статуса 404 и сообщения 'Учетная запись не найдена'")
    public void loginCourierWrongLoginAndPassFail() {
        courierClient.loginCourier(TestData.LOGIN_2, TestData.PASSWORD_2).then().assertThat()
                .body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Проверка авторизации курьера c пустым паролем")
    @Description("Проверка возврата статуса 400 и сообщения 'Недостаточно данных для входа'")
    public void loginCourierNullPassFail() {
        courierClient.loginCourier(TestData.LOGIN_1, null).then().assertThat()
                .body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Проверка авторизации курьера c пустым логином")
    @Description("Проверка возврата статуса 400 и сообщения 'Недостаточно данных для входа'")
    public void loginCourierNullLoginFail() {
        courierClient.loginCourier(null, TestData.PASSWORD_1).then().assertThat()
                .body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Проверка авторизации курьера c пустым логином и паролем")
    @Description("Проверка возврата статуса 400 и сообщения 'Недостаточно данных для входа'")
    public void loginCourierNullLoginAndPasswordFail() {
        courierClient.loginCourier(null, null).then().assertThat()
                .body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }


}
