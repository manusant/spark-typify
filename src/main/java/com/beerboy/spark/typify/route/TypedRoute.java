package com.beerboy.spark.typify.route;

import com.beerboy.spark.typify.provider.TypifyProvider;
import spark.Request;
import spark.Response;
import spark.Route;

import java.lang.reflect.ParameterizedType;

/**
 * @author manusant
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class TypedRoute<T, R> implements Route {

    public abstract R handle(T body, Request request, Response response);

    @Override
    public Object handle(Request request, Response response) {

        Class<T> typeOfT = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];

        T requestObject = TypifyProvider.gson().fromJson(request.body(), typeOfT);

        // Set content type on response to application/json
        R result = handle(requestObject, request, response);
        if (result != null) {
            return TypifyProvider.gson().toJson(result);
        }
        return null;
    }
}