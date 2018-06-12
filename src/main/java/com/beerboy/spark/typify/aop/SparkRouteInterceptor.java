package com.beerboy.spark.typify.aop;

import com.beerboy.spark.typify.annotation.AsJson;
import com.beerboy.spark.typify.annotation.AsText;
import com.beerboy.spark.typify.annotation.AsXml;
import com.beerboy.spark.typify.provider.TypifyProvider;
import com.beerboy.spark.typify.spec.ContentType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.RouteImpl;

import java.lang.annotation.Annotation;

@Aspect
public class SparkRouteInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(SparkRouteInterceptor.class);

    @Around("execution(* *(..)) && within(com.beerboy.spark.typify.route..*)")
    public Object aroundInvoke(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        LOG.info(String.format("Intercepting Spark Route ===> %s", signature.getMethod().getName()));

        Object[] args = pjp.getArgs();

        Request request = null;
        Response response = null;

        try {
            Route route = (Route) pjp.getTarget();
            Annotation[] annotations = route.getClass().getDeclaredMethod("handle", Object.class, Request.class, Response.class).getDeclaredAnnotations();
            System.out.println("Annotations " + annotations[0].getClass());

        }catch (Exception e){
            e.printStackTrace();
        }

        if (args.length == 3) {
            Object typedArgument = args[0];
            request = (Request) args[1];
            response = (Response) args[2];
        } else {
            request = (Request) args[0];
            response = (Response) args[1];
        }

        LOG.debug(String.format("REQUEST BODY===> %s", request.body()));

        AsJson asJson = signature.getMethod().getAnnotation(AsJson.class);
        AsXml asXml = signature.getMethod().getAnnotation(AsXml.class);
        AsText asText = signature.getMethod().getAnnotation(AsText.class);

        if (asJson != null) {
            LOG.info("Response As JSON");
        } else if (asText != null) {
            LOG.info("Response As TXT");
        } else if (asXml != null) {
            LOG.info("Response As XML");
        } else {
            LOG.info("Response As TEXT");
        }

        Object result = pjp.proceed(); //Execute the method


        if (asJson != null) {
            LOG.debug("Response As JSON");
            response.type(ContentType.APPLICATION_JSON.getValue());
            return TypifyProvider.gson().toJson(result);
        } else if (asText != null) {
            LOG.debug("Response As TXT");
            response.type(ContentType.TEXT_PLAIN.getValue());
            return asJson.toString();
        } else if (asXml != null) {
            LOG.debug("Response As XML");
            response.type(ContentType.APPLICATION_XML.getValue());
            return asJson;
        }
        return result;
    }
}