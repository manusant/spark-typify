package io.github.manusant.spark.typify;

import io.github.manusant.spark.typify.annotation.Json;
import io.github.manusant.spark.typify.annotation.Xml;
import io.github.manusant.spark.typify.model.Card;
import io.github.manusant.spark.typify.model.JsonIgnore;
import io.github.manusant.spark.typify.provider.TypifyProvider;
import io.github.manusant.spark.typify.route.Route;
import io.github.manusant.spark.typify.route.TypedRoute;
import io.github.manusant.spark.typify.spec.IgnoreSpec;
import io.github.manusant.spark.typify.model.RestResponse;
import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Request;
import spark.Response;
import spark.Service;

import java.util.Collections;

import static io.github.manusant.spark.typify.model.RestResponse.ok;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.http.ContentType.XML;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class SparkTest {

    static Service spark;

    @BeforeClass
    public static void beforeClass() {
        RestAssured.baseURI = "http://localhost:55555";
        RestAssured.basePath = "";

        spark = Service.ignite().port(55555);
    }

    @Test
    public void test1() {

        spark.post("/", new TypedRoute<Card, Card>() {
                    @Json
                    public Card onRequest(Card body, Request request, Response response) {
                        System.out.println("EXEC CardRoute");
                        return RestResponse.ok(response, body);
                    }
                }
        );

        Card card = new Card();
        card.setName("CARD X");
        card.setType(1);

        given()
                .contentType(JSON)
                .body(card)
                .when()
                .post("/")
                .then()
                .contentType(JSON)
                .statusCode(200);
    }

    @Test
    public void test2() {

        spark.get("/v0", new Route() {
            @Xml
            public Object onRequest(Request request, Response response) {
                Card card = new Card();
                card.setName("CARD X");
                card.setType(1);
                return RestResponse.ok(response, Collections.singletonList(card));
            }
        });

        given()
                .when()
                .get("/v0")
                .then()
                .contentType(XML)
                .statusCode(500);
    }

    @Test
    public void test3() {

        TypifyProvider.setUp(IgnoreSpec.newBuilder()
                .withIgnoreAnnotated(JsonIgnore.class)
                ::build);

        spark.get("/v1", new Route() {
            @Json
            public Object onRequest(Request request, Response response) {
                Card card = new Card();
                card.setName("CARD X");
                card.setSurName("BAJORAS");
                card.setType(1);
                return card;
            }
        });

        given()
                .when()
                .get("/v1")
                .then()
                .contentType(JSON)
                .statusCode(200)
                .assertThat()
                .body("name", equalTo("CARD X"))
                .body("type", equalTo(1))
                .body("surName", nullValue());
    }
}