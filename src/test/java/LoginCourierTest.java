import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(JUnit4.class)
public class LoginCourierTest extends ScooterBaseTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = TestData.baseUrl;
        createCourier(createCourierDto);
    }

    @After
    public void cleanData() {
        deleteCourier(TestData.login1, TestData.password1);
    }

    @Test
    @DisplayName("Проверка успешной авторизации курьера")
    @Description("Проверка возврата статуса 200 и id курьера")
    public void loginCourierSuccess() {
        loginCourier(TestData.login1, TestData.password1).then().assertThat()
                .body("id", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Проверка авторизации курьера c неверным паролем")
    @Description("Проверка возврата статуса 404 и сообщения 'Учетная запись не найдена'")
    public void loginCourierWrongPassFail() {
        loginCourier(TestData.login1, TestData.password2).then().assertThat()
                .body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }

    @Test
    @DisplayName("Проверка авторизации курьера c неверным логином")
    @Description("Проверка возврата статуса 404 и сообщения 'Учетная запись не найдена'")
    public void loginCourierWrongLoginFail() {
        loginCourier(TestData.login2, TestData.password1).then().assertThat()
                .body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }

    @Test
    @DisplayName("Проверка авторизации несуществующего курьера")
    @Description("Проверка возврата статуса 404 и сообщения 'Учетная запись не найдена'")
    public void loginCourierWrongLoginAndPassFail() {
        loginCourier(TestData.login2, TestData.password2).then().assertThat()
                .body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }

    @Test
    @DisplayName("Проверка авторизации курьера c пустым паролем")
    @Description("Проверка возврата статуса 400 и сообщения 'Недостаточно данных для входа'")
    public void loginCourierNullPassFail() {
        loginCourier(TestData.login1, null).then().assertThat()
                .body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Проверка авторизации курьера c пустым логином")
    @Description("Проверка возврата статуса 400 и сообщения 'Недостаточно данных для входа'")
    public void loginCourierNullLoginFail() {
        loginCourier(null, TestData.password1).then().assertThat()
                .body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Проверка авторизации курьера c пустым логином и паролем")
    @Description("Проверка возврата статуса 400 и сообщения 'Недостаточно данных для входа'")
    public void loginCourierNullLoginAndPasswordFail() {
        loginCourier(null, null).then().assertThat()
                .body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }


}
