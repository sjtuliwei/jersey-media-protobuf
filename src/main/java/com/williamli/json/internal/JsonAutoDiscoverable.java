package com.williamli.json.internal;

import javax.ws.rs.core.FeatureContext;

import org.glassfish.jersey.internal.spi.AutoDiscoverable;

import com.williamli.json.JsonFeature;

public class JsonAutoDiscoverable implements AutoDiscoverable {

    @Override
    public void configure(FeatureContext context) {
        if (!context.getConfiguration().isRegistered(JsonFeature.class)) {
            context.register(JsonFeature.class);
        }
    }
}
