package com.beerboy.spark.typify;

import com.beerboy.spark.typify.annotation.Json;
import com.beerboy.spark.typify.annotation.Xml;
import com.beerboy.spark.typify.model.Card;
import com.beerboy.spark.typify.route.Route;
import com.beerboy.spark.typify.route.TypedRoute;
import com.beerboy.spark.typify.spec.ContentType;
import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Request;
import spark.Response;
import spark.Service;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.http.ContentType.XML;

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

        spark.post("/xx", new TypedRoute<Card, Card>() {
                    @Json
                    public Card onRequest(Card body, Request request, Response response) {
                        System.out.println("EXEC CardRoute");
                        return body;
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
                .post("/xx")
                .then()
                .contentType(JSON)
                .statusCode(200);
    }

    @Test
    public void test2() {

        spark.get("/", new Route() {
            @Xml
            public Object onRequest(Request request, Response response) {
                Card card = new Card();
                card.setName("CARD X");
                card.setType(1);
                return Arrays.asList(card);
            }
        });

        given()
                .when()
                .get("/")
                .then()
                .contentType(XML)
                .statusCode(500);
    }
}