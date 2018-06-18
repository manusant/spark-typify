package com.beerboy.spark.typify.route;

import com.beerboy.spark.typify.annotation.Json;
import com.beerboy.spark.typify.annotation.Xml;
import com.beerboy.spark.typify.provider.TypifyProvider;
import com.beerboy.spark.typify.spec.ContentType;
import spark.Request;
import spark.Response;

import java.lang.reflect.Method;

/**
 * @author manusant
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class Route implements spark.Route {

    public abstract Object onRequest(Request request, Response response);

    @Override
    public Object handle(Request request, Response response) {
        Object result = onRequest(request, response);
        try {
            if (result != null) {
                Method m = getClass().getMethod("onRequest", Request.class, Response.class);

                Json json = m.getAnnotation(Json.class);
                Xml xml = m.getAnnotation(Xml.class);

                if (json != null) {
                    response.type(ContentType.APPLICATION_JSON.getValue());
                    return TypifyProvider.gson().toJson(result);
                } else if (xml != null) {
                    response.type(ContentType.APPLICATION_XML.getValue());
                    throw new UnsupportedOperationException("XML mapping not supported yet");
                }
            }
        } catch (NoSuchMethodException | SecurityException e) {
            // DO NOTHING
        }
        return null;
    }
}