import dto.CreateCourierRequestDto;
import dto.LoginCourierRequestDto;
import dto.LoginCourierResponseDto;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierClient {

    static final String COURIER_API = "/api/v1/courier";

    public final CreateCourierRequestDto createCourierDto = new CreateCourierRequestDto(TestData.LOGIN_1, TestData.PASSWORD_1, TestData.NAME_1);

    @Step("Отправка POST в /api/v1/courier")
    public Response createCourier(CreateCourierRequestDto dto) {
        return
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(dto)
                        .when()
                        .post(COURIER_API);

    }

    @Step("Отправка DELETE в /api/v1/courier/:id")
    public Response deleteCourier(Integer id) {
        return given()
                .delete(COURIER_API + "/" + id);
    }

    @Step("Отправка POST в /api/v1/courier/login")
    public Response loginCourier(LoginCourierRequestDto dto) {
        return
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(dto)
                        .when()
                        .post(COURIER_API + "/login");
    }

    public Response loginCourier(String login, String password) {
        LoginCourierRequestDto loginDto = new LoginCourierRequestDto();
        loginDto.setLogin(login);
        loginDto.setPassword(password);
        return loginCourier(loginDto);
    }

    public void deleteCourier(String login, String password) {
        LoginCourierRequestDto loginDto = new LoginCourierRequestDto();
        loginDto.setLogin(login);
        loginDto.setPassword(password);

        LoginCourierResponseDto loginCourierResponseDto = loginCourier(loginDto).as(LoginCourierResponseDto.class);
        deleteCourier(loginCourierResponseDto.getId());
    }
}
