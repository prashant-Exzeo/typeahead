package com.one97.http;

import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;

import java.util.Map;

/*
 * ---------------------------------------------------------------------------------------------
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * ---------------------------------------------------------------------------------------------
 */

public abstract class BaseRequest {
    public abstract HttpVersion getProtocolVersion();

    public abstract HttpRequest request();

    public enum Method {
        GET, POST, DELETE, OPTIONS, HEAD, PUT
    }

    public abstract Method method();

    public abstract String uri();

    public abstract String path();

    public abstract boolean hasContent();

    public abstract Iterable<Map.Entry<String, String>> headers();

    public abstract boolean hasParam(String key);

    public abstract String header(String name);

    public abstract String param(String name);

    public abstract String param(String name, String defaultValue);

    public abstract Map<String, String> params();

    public float paramAsFloat(String key, float defaultValue) {
        String sValue = param(key);
        if (sValue == null) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(sValue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Failed to parse float parameter [" + key + "] with value [" + sValue + "]", e);
        }
    }

    public int paramAsInt(String key, int defaultValue) {
        String sValue = param(key);
        if (sValue == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(sValue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Failed to parse int parameter [" + key + "] with value [" + sValue + "]", e);
        }
    }

    public long paramAsLong(String key, long defaultValue) {
        String sValue = param(key);
        if (sValue == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(sValue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Failed to parse int parameter [" + key + "] with value [" + sValue + "]", e);
        }
    }

}
