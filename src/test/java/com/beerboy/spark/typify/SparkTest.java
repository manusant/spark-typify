package com.beerboy.spark.typify;

import com.beerboy.spark.typify.annotation.AsXml;
import com.beerboy.spark.typify.route.TypedRoute;
import com.beerboy.spark.typify.routeX.Card;
import com.beerboy.spark.typify.spec.ContentType;
import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Service;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.HTML;
import static io.restassured.http.ContentType.JSON;

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
                    @Override
                    @AsXml
                    public Card handle(Card body, Request request, Response response) {
                        System.out.println("EXEC CardRoute");
                        response.type(ContentType.APPLICATION_JSON.getValue());
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

   /** @Test
    public void test2() {

        spark.get("/", new Route() {
            public @AsXml Object handle(Request request, Response response) {
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
                .contentType(HTML)
                .statusCode(200);
    }*/
}