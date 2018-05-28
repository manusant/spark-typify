package com.beerboy.spark.typify.route;

import spark.Request;
import spark.Response;

/**
 * @author manusant
 */
@FunctionalInterface
public interface TypedRoute<T, R> extends SparkRoute{

    R handleAndTransform(T body, Request request, Response response);
}