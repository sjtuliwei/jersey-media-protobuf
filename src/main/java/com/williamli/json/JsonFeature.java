package com.williamli.json;

import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import com.williamli.json.internal.JsonProvider;

public class JsonFeature implements Feature {
    @Override
    public boolean configure(final FeatureContext context) {
        final Configuration config = context.getConfiguration();

        if (!config.isRegistered(JsonProvider.class)) {
            context.register(JsonProvider.class, MessageBodyReader.class, MessageBodyWriter.class);
        }
        return true;
    }

}
