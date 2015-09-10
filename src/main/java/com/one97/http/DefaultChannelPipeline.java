package com.one97.http;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

/*
 * ---------------------------------------------------------------------------------------------
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * ---------------------------------------------------------------------------------------------
 */

public class DefaultChannelPipeline implements ChannelPipelineFactory {

    private final HttpServerTransport httpServerTransport;
    private final HttpRequestHandler httpRequestHandler;

    public DefaultChannelPipeline(HttpServerTransport httpServerTransport) {
        this.httpServerTransport = httpServerTransport;
        this.httpRequestHandler = new HttpRequestHandler(this.httpServerTransport);

    }

    @Override
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = Channels.pipeline();

        HttpRequestDecoder httpRequestDecoder = new HttpRequestDecoder(
                httpServerTransport.maxInitialLineLength(),
                httpServerTransport.maxHeaderSize(),
                httpServerTransport.maxChunkSize()
        );


        if (httpServerTransport.maxCumulationBufferCapacity() != null) {
            httpRequestDecoder.setMaxCumulationBufferCapacity(httpServerTransport.maxCumulationBufferCapacity());
        } else {
            httpRequestDecoder.setMaxCumulationBufferCapacity(Integer.MAX_VALUE);
        }

        HttpChunkAggregator httpChunkAggregator = new HttpChunkAggregator(httpServerTransport.maxContentLength());

        if (httpServerTransport.maxCompositeBufferComponents() != -1) {
            httpChunkAggregator.setMaxCumulationBufferComponents(httpServerTransport.maxCompositeBufferComponents());
        }

        pipeline.addLast("decoder", httpRequestDecoder);
        pipeline.addLast("aggregator", httpChunkAggregator);
        pipeline.addLast("encoder", new HttpResponseEncoder());

        if (httpServerTransport.compressionEnabled()) {
            pipeline.addLast("encoder_compress", new HttpContentCompressor(httpServerTransport.compressionLevel()));
        }

        if (httpServerTransport.adminEnabled()) {
            pipeline.addLast("adminArea", new HttpBasicAuthHandler(this.httpServerTransport));
        }

        pipeline.addLast("handler", httpRequestHandler);

        return pipeline;
    }
}
