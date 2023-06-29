import dto.CreateCourierRequestDto;
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


@RunWith(JUnit4.class)
public class CreateCourierTest {

    private final CourierClient courierClient = new CourierClient();

    @Before
    public void setUp() {
        RestAssured.baseURI = TestData.BASE_URL;
    }

    @After
    public void cleanData() {
        courierClient.deleteCourier(TestData.LOGIN_1, TestData.PASSWORD_1);
    }

    @Test
    @DisplayName("Проверка успешного создания курьера")
    @Description("Проверка возврата статуса 201 и 'ok'")
    public void createCourierSuccess() {
        courierClient.createCourier(courierClient.createCourierDto).then().assertThat()
                .body("ok", equalTo(true))
                .and().statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    @DisplayName("Проверка ошибки при создании одинаковых курьеров")
    @Description("Проверка невозможности создания курьеров с одинаковыми данными")
    public void createEqualCouriersFail() {

        courierClient.createCourier(courierClient.createCourierDto);
        courierClient.createCourier(courierClient.createCourierDto).then().assertThat()
                .body("code", equalTo(409))
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and().statusCode(HttpStatus.SC_CONFLICT);
    }

    @Test
    @DisplayName("Проверка ошибки при создании курьеров с одинаковыми логинами")
    @Description("Проверка возврата статуса 409 и сообщения 'Этот логин уже используется. Попробуйте другой.'")
    public void createCourierWithEqualLoginsFail() {
        courierClient.createCourier(courierClient.createCourierDto);
        CreateCourierRequestDto createDto2 = new CreateCourierRequestDto();
        createDto2.setFirstName(TestData.NAME_1);
        createDto2.setLogin(TestData.LOGIN_1);
        createDto2.setPassword(TestData.PASSWORD_2);

        courierClient.createCourier(createDto2).then().assertThat()
                .body("code", equalTo(409))
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and().statusCode(HttpStatus.SC_CONFLICT);
    }

    @Test
    @DisplayName("Проверка ошибки при создании курьера без пароля")
    @Description("Проверка возврата статуса 400 и сообщения 'Недостаточно данных для создания учетной записи'")
    public void createCourierWithoutPasswordFail() {
        CreateCourierRequestDto createDto = new CreateCourierRequestDto();
        createDto.setFirstName(TestData.NAME_1);
        createDto.setLogin(TestData.LOGIN_1);
        createDto.setPassword(null);

        courierClient.createCourier(createDto).then().assertThat()
                .body("code", equalTo(400))
                .body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and().statusCode(HttpStatus.SC_BAD_REQUEST);
    }


    @Test
    @DisplayName("Проверка ошибки при создании курьера без логина")
    @Description("Проверка возврата статуса 400 и сообщения 'Недостаточно данных для создания учетной записи'")
    public void createCourierWithoutLoginFail() {
        CreateCourierRequestDto createDto = new CreateCourierRequestDto();
        createDto.setFirstName(TestData.NAME_1);
        createDto.setLogin(null);
        createDto.setPassword(TestData.PASSWORD_1);

        courierClient.createCourier(createDto).then().assertThat()
                .body("code", equalTo(400))
                .body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Проверка ошибки при создании курьера без логина и пароля")
    @Description("Проверка возврата статуса 400 и сообщения 'Недостаточно данных для создания учетной записи'")
    public void createCourierWithoutLoginAndPasswordFail() {
        CreateCourierRequestDto createDto = new CreateCourierRequestDto();
        createDto.setFirstName(TestData.NAME_1);
        createDto.setLogin(null);
        createDto.setPassword(null);

        courierClient.createCourier(createDto).then().assertThat()
                .body("code", equalTo(400))
                .body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and().statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}
