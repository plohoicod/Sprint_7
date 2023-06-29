import dto.CreateCourierRequestDto;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.CoreMatchers.equalTo;


@RunWith(JUnit4.class)
public class CreateCourierTest extends ScooterBaseTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = TestData.baseUrl;
    }

    @After
    public void cleanData() {
        deleteCourier(TestData.login1, TestData.password1);
    }

    @Test
    @DisplayName("Проверка успешного создания курьера")
    @Description("Проверка возврата статуса 201 и 'ok'")
    public void createCourierSuccess() {
        createCourier(createCourierDto).then().assertThat()
                .body("ok", equalTo(true))
                .and().statusCode(201);
    }

    @Test
    @DisplayName("Проверка ошибки при создании одинаковых курьеров")
    @Description("Проверка невозможности создания курьеров с одинаковыми данными")
    public void createEqualCouriersFail() {

        createCourier(createCourierDto);
        createCourier(createCourierDto).then().assertThat()
                .body("code", equalTo(409))
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and().statusCode(409);
    }

    @Test
    @DisplayName("Проверка ошибки при создании курьеров с одинаковыми логинами")
    @Description("Проверка возврата статуса 409 и сообщения 'Этот логин уже используется. Попробуйте другой.'")
    public void createCourierWithEqualLoginsFail() {
        createCourier(createCourierDto);
        CreateCourierRequestDto createDto2 = new CreateCourierRequestDto();
        createDto2.setFirstName(TestData.name2);
        createDto2.setLogin(TestData.login1);
        createDto2.setPassword(TestData.password2);

        createCourier(createDto2).then().assertThat()
                .body("code", equalTo(409))
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and().statusCode(409);
    }

    @Test
    @DisplayName("Проверка ошибки при создании курьера без пароля")
    @Description("Проверка возврата статуса 400 и сообщения 'Недостаточно данных для создания учетной записи'")
    public void createCourierWithoutPasswordFail() {
        CreateCourierRequestDto createDto = new CreateCourierRequestDto();
        createDto.setFirstName(TestData.name1);
        createDto.setLogin(TestData.login1);
        createDto.setPassword(null);

        createCourier(createDto).then().assertThat()
                .body("code", equalTo(400))
                .body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and().statusCode(400);
    }


    @Test
    @DisplayName("Проверка ошибки при создании курьера без логина")
    @Description("Проверка возврата статуса 400 и сообщения 'Недостаточно данных для создания учетной записи'")
    public void createCourierWithoutLoginFail() {
        CreateCourierRequestDto createDto = new CreateCourierRequestDto();
        createDto.setFirstName(TestData.name1);
        createDto.setLogin(null);
        createDto.setPassword(TestData.password1);

        createCourier(createDto).then().assertThat()
                .body("code", equalTo(400))
                .body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and().statusCode(400);
    }

    @Test
    @DisplayName("Проверка ошибки при создании курьера без логина и пароля")
    @Description("Проверка возврата статуса 400 и сообщения 'Недостаточно данных для создания учетной записи'")
    public void createCourierWithoutLoginAndPasswordFail() {
        CreateCourierRequestDto createDto = new CreateCourierRequestDto();
        createDto.setFirstName(TestData.name1);
        createDto.setLogin(null);
        createDto.setPassword(null);

        createCourier(createDto).then().assertThat()
                .body("code", equalTo(400))
                .body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and().statusCode(400);
    }
}
