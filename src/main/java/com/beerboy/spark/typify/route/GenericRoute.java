package com.beerboy.spark.typify.route;

import spark.Request;
import spark.Response;

/**
 * @author manusant
 */
public interface GenericRoute extends SparkRoute{

    Object handleAndTransform(Request request, Response response);
}