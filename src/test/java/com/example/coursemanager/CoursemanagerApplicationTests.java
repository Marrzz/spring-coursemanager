package com.example.coursemanager;

import application.entity.Course;
import static com.google.common.truth.Truth.assertThat;
import com.google.gson.Gson;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;

@SpringBootTest
@SpringBootConfiguration
class CoursemanagerApplicationTests {

    String GET_ALL = "http://localhost:8080/courses/all/";
    String BASE = "http://localhost:8080/courses/";

    @Test
    void shouldReturnOk_WhenGetAll() {
        given()
                .when()
                .get(GET_ALL)
                .then()
                .statusCode(200);
    }

    @Test
    void shouldReturnOk_WhenAddingNewCourse(){
        Course course = new Course("Course1", "123", 5);

        Gson gson = new Gson();

        Response response = given()
                .when()
                .header("Content-Type", "application/json")
                .body(gson.toJson(course))
                .post(BASE + "add")
                .then()
                .extract().response();

        assertThat(response.getStatusCode()).isEqualTo(201);

    }

    @Test
    void ShouldReturnCourse_WhenIdIsGiven(){

        Course course = new Course("Course1", "123", 5);

        Gson gson = new Gson();

        int id = given()
                .when()
                .header("Content-Type", "application/json")
                .body(gson.toJson(course))
                .post(BASE + "add")
                .jsonPath()
                .get("id");

        JsonPath getCourse = given()
                .when()
                .get(BASE + id)
                .jsonPath();

        assertThat("Course1").isEqualTo(getCourse.get("name"));
        assertThat("123").isEqualTo(getCourse.get("code"));
        assertThat(5).isEqualTo(getCourse.get("grade"));
    }

    @Test
    void shouldDeleteCourse_WhenIdIsGiven(){

        Course course = new Course("Deleted", "1841284", 1);

        Gson gson = new Gson();

        int id = given()
                .when()
                .header("Content-Type", "application/json")
                .body(gson.toJson(course))
                .post(BASE + "add")
                .jsonPath()
                .get("id");

        given()
                .when()
                .delete(BASE + "delete/" + id)
                .then()
                .statusCode(200);

        ResponseBody body = given()
                .get(BASE + "all")
                        .getBody();

        assertThat(body.asString()).doesNotContain(gson.toJson(course));


    }

    @Test
    void ShouldReturnUpdatedCourse_WhenCourseIsGiven(){

        Course course = new Course("Old", "1841284", 1);



        Gson gson = new Gson();

        int id = given()
                .when()
                .header("Content-Type", "application/json")
                .body(gson.toJson(course))
                .post(BASE + "add")
                .jsonPath()
                .get("id");

        Course updatedCourse = new Course(id,"New", "1841284", 1);

        JsonPath response = given()
                .when()
                .header("Content-Type", "application/json")
                .body(gson.toJson(updatedCourse))
                .put(BASE + "update/").jsonPath();

        int responseId = response.get("id");
        String responseName = response.get("name");
        String responseCode = response.get("code");
        int responseGrade = response.get("grade");

        assertThat(responseId).isEqualTo(id);
        assertThat(responseName).isEqualTo(updatedCourse.getName());
        assertThat(responseCode).isEqualTo(updatedCourse.getCode());
        assertThat(responseGrade).isEqualTo(updatedCourse.getGrade());


    }

}
