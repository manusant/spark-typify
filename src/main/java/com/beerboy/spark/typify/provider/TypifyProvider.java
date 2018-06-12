package com.beerboy.spark.typify.provider;

import com.beerboy.spark.typify.spec.IgnoreSpec;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author manusant
 */
public class TypifyProvider {

    private static Gson GSON;

    public static Gson create() {
        return create(null);
    }

    public static Gson create(IgnoreSpec ignoreSpec) {
        GSON = new GsonBuilder()
                .setPrettyPrinting()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                        return ignoreSpec != null && (ignoreSpec.ignoreAnnotated(fieldAttributes) || ignoreSpec.ignored(fieldAttributes));
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> aClass) {
                        return false;
                    }
                })
                .create();
        return GSON;
    }

    public static Gson gson() {
        if (GSON == null) {
            // Create with default configs
            create();
        }
        return GSON;
    }
}
