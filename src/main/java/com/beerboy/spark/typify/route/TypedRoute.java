package com.beerboy.spark.typify.route;

import com.beerboy.spark.typify.annotation.Json;
import com.beerboy.spark.typify.annotation.Xml;
import com.beerboy.spark.typify.exception.ReflectionExceptions;
import com.beerboy.spark.typify.provider.TypifyProvider;
import com.beerboy.spark.typify.spec.ContentType;
import spark.Request;
import spark.Response;
import spark.Route;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

/**
 * @author manusant
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class TypedRoute<T, R> implements Route {

    public abstract R onRequest(T body, Request request, Response response);

    @Override
    public Object handle(Request request, Response response) {
        try {
            Class<T> typeOfT = (Class<T>) ((ParameterizedType) getClass()
                    .getGenericSuperclass())
                    .getActualTypeArguments()[0];

            T requestObject = TypifyProvider.gson().fromJson(request.body(), typeOfT);

            Method method = getClass().getMethod("onRequest", Object.class, Request.class, Response.class);

            Json json = method.getAnnotation(Json.class);
            Xml xml = method.getAnnotation(Xml.class);

            // Set content type on response to application/json
            R result = onRequest(requestObject, request, response);
            if (json != null) {
                response.type(ContentType.APPLICATION_JSON.getValue());
                return TypifyProvider.gson().toJson(result);
            } else if (xml != null) {
                response.type(ContentType.APPLICATION_XML.getValue());
                throw new UnsupportedOperationException("XML mapping not supported yet");
            }
        } catch (NoSuchMethodException | SecurityException e) {
            ReflectionExceptions.handleReflectionException(e);
        }
        return null;
    }
}