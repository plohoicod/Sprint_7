import dto.CreateCourierRequestDto;
import dto.LoginCourierRequestDto;
import dto.LoginCourierResponseDto;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;


public class ScooterBaseTest {

    public static final CreateCourierRequestDto createCourierDto = new CreateCourierRequestDto(TestData.login1, TestData.password1, TestData.name1);


    @Step("Отправка POST в /api/v1/courier")
    protected Response createCourier(CreateCourierRequestDto dto) {
        return
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(dto)
                        .when()
                        .post("/api/v1/courier");

    }

    @Step("Отправка DELETE в /api/v1/courier/:id")
    protected Response deleteCourier(Integer id) {
        return given()
                .delete("/api/v1/courier/" + id);
    }

    @Step("Отправка POST в /api/v1/courier/login")
    protected Response loginCourier(LoginCourierRequestDto dto) {
        return
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(dto)
                        .when()
                        .post("/api/v1/courier/login");
    }

    protected Response loginCourier(String login, String password) {
        LoginCourierRequestDto loginDto = new LoginCourierRequestDto();
        loginDto.setLogin(login);
        loginDto.setPassword(password);
        return loginCourier(loginDto);
    }

    protected void deleteCourier(String login, String password) {
        LoginCourierRequestDto loginDto = new LoginCourierRequestDto();
        loginDto.setLogin(login);
        loginDto.setPassword(password);


        LoginCourierResponseDto loginCourierResponseDto = loginCourier(loginDto).as(LoginCourierResponseDto.class);
        deleteCourier(loginCourierResponseDto.getId());
    }
}
