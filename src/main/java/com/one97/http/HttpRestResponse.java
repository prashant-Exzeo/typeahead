package com.one97.http;

import com.one97.SpockException;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;

/*
 * ---------------------------------------------------------------------------------------------
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * ---------------------------------------------------------------------------------------------
 */

/**
 * Used for preparing {@link org.jboss.netty.handler.codec.http.HttpResponse} object. The class
 * follows flyweight pattern approach in that it wraps underlying response construction methods
 * such as {@link JsonGenerator} methods for easy access.
 *
 */
public class HttpRestResponse extends BaseHttpResponse {

    public static final String TEXT_CONTENT_TYPE = "text/plain; charset=UTF-8";
    public static final String JSON_CONTENT_TYPE = "application/json; charset=UTF-8";
    private static JsonFactory factory;
    private static PrettyPrinter prettyPrinter;

    static {
        factory = new JsonFactory();
        factory.enable(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT);

        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        pp.indentArraysWith(new DefaultPrettyPrinter.Lf2SpacesIndenter());
        pp.indentObjectsWith(new DefaultPrettyPrinter.Lf2SpacesIndenter());
        pp.spacesInObjectEntries(false);

    }

    private final HttpResponseStatus status;
    private final String contentType;

    private JsonGenerator generator;

    public HttpRestResponse(HttpResponseStatus status) {
        this(status, TEXT_CONTENT_TYPE);
    }

    public HttpRestResponse(HttpResponseStatus status, String contentType) {
        this.status = status;
        this.contentType = contentType;
        this.addHeader("Access-Control-Allow-Origin", "*");
        this.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
        this.addHeader("Access-Control-Allow-Credentials", "true");
        this.addHeader("Access-Control-Allow-Methods", "GET, POST");
        this.addHeader("Access-Control-Max-Age", "1209600");

        try {
            this.generator = factory.createGenerator(new ChannelBufferOutputStream(this.buffer), JsonEncoding.UTF8);
        } catch (Exception e) {
            throw new SpockException("IO Exception happened during initialization of JSONGenerator");
        }
    }

    public static HttpRestResponse internalServerErrorJsonResponse() {
        return new HttpRestResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR, JSON_CONTENT_TYPE);
    }

    public static HttpRestResponse OkJsonResponse() {
        return new HttpRestResponse(HttpResponseStatus.OK, JSON_CONTENT_TYPE);
    }

    public static HttpRestResponse OkJsonResponse(RequestContext context) {
        HttpRestResponse restResponse = OkJsonResponse();
        restResponse.prettify(context.prettify());
        return restResponse;
    }

    @Override
    public String contentType() {
        return contentType;
    }

    @Override
    public void prettify(boolean flag) {

        if (flag) {
            this.generator.setPrettyPrinter(prettyPrinter);
        }

        generator = generator.useDefaultPrettyPrinter();
    }

    @Override
    public HttpResponseStatus status() {
        return status;
    }

    public HttpRestResponse writeStartArray() {
        try {
            generator.writeStartArray();
        } catch (Exception e) {
            throw new SpockException("writeStartArray failed", e);
        }
        return this;
    }

    public HttpRestResponse writeEndArray() {
        try {
            generator.writeEndArray();
        } catch (Exception e) {
            throw new SpockException("writeEndArray failed", e);
        }

        return this;
    }

    public HttpRestResponse writeStartObject() {
        try {
            generator.writeStartObject();
        } catch (Exception e) {
            throw new SpockException("writeStartObject failed", e);
        }
        return this;
    }

    public HttpRestResponse writeEndObject() {
        try {
            generator.writeEndObject();
        } catch (Exception e) {
            throw new SpockException("writeEndObject failed", e);
        }
        return this;
    }

    public HttpRestResponse writeFieldName(String name) {
        try {
            generator.writeFieldName(name);
        } catch (Exception e) {
            throw new SpockException("writeFieldName failed", e);
        }
        return this;
    }

    public HttpRestResponse writeFieldName(SerializableString name) {
        try {
            generator.writeFieldName(name);
        } catch (Exception e) {
            throw new SpockException("writeFieldName failed", e);
        }
        return this;
    }

    public HttpRestResponse writeString(String text) {
        try {
            generator.writeString(text);
        } catch (Exception e) {
            throw new SpockException("writeString failed", e);
        }
        return this;
    }


    public HttpRestResponse writeString(char[] text, int offset, int len) {
        try {
            generator.writeString(text, offset, len);
        } catch (Exception e) {
            throw new SpockException("writeString failed", e);
        }
        return this;
    }


    public HttpRestResponse writeString(SerializableString text) {
        try {
            generator.writeString(text);
        } catch (Exception e) {
            throw new SpockException("writeString failed", e);
        }
        return this;
    }

    public HttpRestResponse writeRawUTF8String(byte[] text, int offset, int length) {
        try {

            generator.writeRawUTF8String(text, offset, length);
        } catch (Exception e) {
            throw new SpockException("writeRawUTF8String failed", e);
        }
        return this;
    }

    public HttpRestResponse writeUTF8String(byte[] text, int offset, int length) {
        try {

            generator.writeUTF8String(text, offset, length);
        } catch (Exception e) {
            throw new SpockException("writeUTF8String failed", e);
        }
        return this;
    }

    public HttpRestResponse writeRaw(String text) {
        try {

            generator.writeRaw(text);
        } catch (Exception e) {
            throw new SpockException("writeRaw failed", e);
        }
        return this;
    }


    public HttpRestResponse writeRaw(String text, int offset, int len) {
        try {

            generator.writeRaw(text, offset, len);
        } catch (Exception e) {
            throw new SpockException("writeRaw failed", e);
        }
        return this;
    }

    public HttpRestResponse writeRaw(char[] text, int offset, int len) {
        try {

            generator.writeRaw(text, offset, len);
        } catch (Exception e) {
            throw new SpockException("writeRaw failed", e);
        }
        return this;
    }

    public HttpRestResponse writeRaw(char c) {
        try {

            generator.writeRaw(c);
        } catch (Exception e) {
            throw new SpockException("writeRaw failed", e);
        }
        return this;
    }

    public HttpRestResponse writeRawValue(String text) {
        try {

            generator.writeRawValue(text);
        } catch (Exception e) {
            throw new SpockException("writeRawValue failed", e);
        }
        return this;
    }

    public HttpRestResponse writeRawValue(String text, int offset, int len) {
        try {

            generator.writeRawValue(text, offset, len);
        } catch (Exception e) {
            throw new SpockException("writeRawValue failed", e);
        }
        return this;
    }

    public HttpRestResponse writeRawValue(char[] text, int offset, int len) {
        try {

            generator.writeRawValue(text, offset, len);
        } catch (Exception e) {
            throw new SpockException("writeRawValue failed", e);
        }
        return this;
    }

    public HttpRestResponse writeBinary(Base64Variant b64variant, byte[] data, int offset, int len) {
        try {

            generator.writeBinary(b64variant, data, offset, len);
        } catch (Exception e) {
            throw new SpockException("writeBinary failed", e);
        }
        return this;
    }

    public HttpRestResponse writeBinary(Base64Variant b64variant, InputStream data, int dataLength) {
        try {

            generator.writeBinary(b64variant, data, dataLength);
        } catch (Exception e) {
            throw new SpockException("writeBinary failed", e);
        }
        return this;
    }

    public HttpRestResponse writeNumber(int v) {
        try {

            generator.writeNumber(v);
        } catch (Exception e) {
            throw new SpockException("writeNumber failed", e);
        }
        return this;
    }

    public HttpRestResponse writeNumber(long v) {
        try {

            generator.writeNumber(v);
        } catch (Exception e) {
            throw new SpockException("writeNumber failed", e);
        }
        return this;
    }

    public HttpRestResponse writeNumber(BigInteger v) {
        try {

            generator.writeNumber(v);
        } catch (Exception e) {
            throw new SpockException("writeNumber failed", e);
        }
        return this;
    }

    public HttpRestResponse writeNumber(double d) {
        try {

            generator.writeNumber(d);
        } catch (Exception e) {
            throw new SpockException("writeNumber failed", e);
        }
        return this;
    }

    public HttpRestResponse writeNumber(float f) {
        try {

            generator.writeNumber(f);
        } catch (Exception e) {
            throw new SpockException("writeNumber failed", e);
        }
        return this;
    }

    public HttpRestResponse writeNumber(BigDecimal dec) {
        try {
            generator.writeNumber(dec);
        } catch (Exception e) {
            throw new SpockException("writeNumber failed", e);
        }

        return this;
    }

    public HttpRestResponse writeNumber(String encodedValue) {

        try {
            generator.writeNumber(encodedValue);
        } catch (Exception e) {
            throw new SpockException("writeNumber failed", e);
        }

        return this;
    }

    public HttpRestResponse writeBoolean(boolean state) {

        try {
            generator.writeBoolean(state);
        } catch (Exception e) {
            throw new SpockException("writeBoolean failed", e);
        }

        return this;
    }

    public HttpRestResponse writeNull() {
        try {
            generator.writeNull();
        } catch (Exception e) {
            throw new SpockException("writeNull failed", e);
        }
        return this;
    }

    @Override
    public ChannelBuffer content() {

        try {
            if (generator != null && !generator.isClosed()) {
                generator.flush();
                generator.close();
            }
        } catch (Exception e) {
            throw new SpockException("channel buffer failed", e);
        }

        return super.content();
    }
}
