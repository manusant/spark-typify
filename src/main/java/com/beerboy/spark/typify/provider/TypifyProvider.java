package com.beerboy.spark.typify.provider;

import com.beerboy.spark.typify.spec.IgnoreSpec;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.function.Supplier;

/**
 * @author manusant
 */
public class TypifyProvider {

    private static Gson GSON;

    private static Gson create(IgnoreSpec ignoreSpec) {
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

    public static void setUp(Supplier<IgnoreSpec> ignoreSupplier) {
        create(ignoreSupplier.get());
    }

    public static void setUp(IgnoreSpec ignore) {
        create(ignore);
    }

    public static Gson gson() {
        if (GSON == null) {
            // Create with default configs
            create(null);
        }
        return GSON;
    }
}
