package com.one97.settings;

import com.one97.SpockException;
import com.one97.common.conf.ConfigurationSourceProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

import static com.google.common.base.Strings.isNullOrEmpty;

/*
 * ---------------------------------------------------------------------------------------------
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * ---------------------------------------------------------------------------------------------
 */

public class SpockSettingsBuilder implements SettingsBuilder {

    public ConfigurationSourceProvider getProvider() {
        return provider;
    }

    private final ConfigurationSourceProvider provider;
    private final YAMLFactory factory;

    private static Logger LOGGER = LoggerFactory.getLogger(SpockSettingsBuilder.class);

    public SpockSettingsBuilder(ConfigurationSourceProvider provider) {
        this.provider = provider;
        this.factory = new YAMLFactory();
        this.factory.enable(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT);
        this.factory.enable(JsonParser.Feature.ALLOW_COMMENTS);
        this.factory.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        this.factory.enable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
    }

    public SpockSettings build() {

        InputStream input = null;
        YAMLParser parser = null;
        SpockSettings settings = null;

        try {
            input = provider.open();
        } catch (IOException e) {
            throw new SpockException("Configuration initialization failed due to I/O Exception", e);
        }

        try {
            parser = factory.createParser(input);
        } catch (IOException e) {
            throw new SpockException("Parser initialization failed due to I/O Exception", e);
        }

        try {
            settings = new ImmutableSpockSettings(parseAndBuild(parser).build(), getClass().getClassLoader());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return settings;
    }


    private ImmutableMap.Builder<String, String> parseAndBuild(YAMLParser parser) throws IOException {

        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();


        String fieldName = null;
        String fieldValue = null;

        JsonToken token = parser.nextToken();

        do {

            if (token == null)
                break;

            if (token == JsonToken.START_ARRAY) {
                throw new SpockException(String.format("Configuration has array structure at line %s (\"%s\"). " +
                                "Configuration does not support arrays, " +
                                "everything has to be just key:value pairs. You may want to namespace your keys" +
                                "to keep an array structure to a key/value pair.",
                        parser.getCurrentLocation().getLineNr(), parser.getText()
                ));
            }

            if (token == JsonToken.FIELD_NAME) {
                fieldName = parser.getCurrentName();

                token = parser.nextToken();

                if (token != null)
                    fieldValue = parser.getText();
            }

            if (fieldName != null && isNullOrEmpty(fieldValue)) {
                LOGGER.warn("Field {} has no value, skipping from loading", fieldName);
                fieldName = null;
            }

            if (fieldName != null && fieldValue != null) {
                builder.put(fieldName, fieldValue);
                fieldName = fieldValue = null;
            }

        } while ((token = parser.nextToken()) != null);

        return builder;
    }

}
