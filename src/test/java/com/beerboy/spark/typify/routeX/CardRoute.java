package com.beerboy.spark.typify.routeX;

import com.beerboy.spark.typify.route.TypedRoute;
import com.beerboy.spark.typify.spec.ContentType;
import spark.Request;
import spark.Response;

public class CardRoute extends TypedRoute<Card,Card> {
    @Override
    public Card handle(Card body, Request request, Response response) {
        System.out.println("EXEC CardRoute");
        response.type(ContentType.APPLICATION_JSON.getValue());
        return body;
    }
}
