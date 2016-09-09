package com.knightRider.typeahead.http;


import com.knightRider.typeahead.TypeaheadException;
import com.knightRider.typeahead.common.service.AbstractLifecycleService;
import com.knightRider.typeahead.common.util.ExceptionHelper;
import com.knightRider.typeahead.common.util.Size;
import org.elasticsearch.monitor.jvm.JvmInfo;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.channel.socket.oio.OioServerSocketChannelFactory;
import org.jboss.netty.handler.timeout.ReadTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/*
 * _____________________________________________________________________________________________
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * _____________________________________________________________________________________________
 */

public class HttpServerTransport extends AbstractLifecycleService<HttpServerTransport> {

    private static Logger LOGGER = LoggerFactory.getLogger(HttpServerTransport.class);

    private final int workerCount;

    private final boolean blockingServer;
    private final boolean compression;
    private final int port;
    private final String bindHost;
    private final Boolean tcpNoDelay;
    private final Boolean tcpKeepAlive;
    private final Boolean reuseAddress;
    private final ReceiveBufferSizePredictorFactory receiveBufferSizePredictorFactory;
    private final int maxContentLength;
    private final int maxChunkSize;
    private final int maxHeaderSize;
    private final int maxInitialLineLength;
    private final int tcpSendBufferSize;
    private final int tcpReceiveBufferSize;
    private final int compressionLevel;
    private final Boolean resetCookies;
    private final Integer maxCumulationBufferCapacity;
    private final Integer maxCompositeBufferComponents;

    private final boolean adminEnabled;

    private volatile Channel serverChannel;
    private volatile ServerBootstrap serverBootstrap;
    private volatile HttpServerAdapter httpServerAdapter;

    @Inject
    public HttpServerTransport() {

       // super(settings);

        // TODO: Document each of this properties and its significance in Netty transport

        this.maxContentLength =  (int) Size.megabytes(100).toBytes();
        this.maxChunkSize = (int) Size.kilobytes(8).toBytes();
        this.maxHeaderSize = (int) Size.kilobytes(8).toBytes();
        this.maxInitialLineLength =  (int) Size.kilobytes(4).toBytes();
        this.resetCookies =  false;
        this.maxCumulationBufferCapacity =  null;
        this.maxCompositeBufferComponents = -1;
        this.workerCount = Math.min(32, Runtime.getRuntime().availableProcessors()) * 2;
        this.blockingServer =  false;
        this.port =  8989;
        this.bindHost =  "0.0.0.0";
        this.tcpKeepAlive =  true;
        this.tcpNoDelay =  true;
        this.reuseAddress = true; // Note: This is linux friendly.
        this.tcpSendBufferSize =  (int) Size.kilobytes(16).toBytes();
        this.tcpReceiveBufferSize = (int) Size.kilobytes(16).toBytes();
        this.receiveBufferSizePredictorFactory = predictBufferSizeFactory();
        this.compression = false;
        this.compressionLevel = 6;

        //TODO: The admin auth is not yet implemented.
        this.adminEnabled =  false;
    }

    private ReceiveBufferSizePredictorFactory predictBufferSizeFactory() {

        long defaultReceiverPredictor = 512 * 1024;

        if (JvmInfo.jvmInfo().getMem().getDirectMemoryMax().bytes() > 0) {
            long l = (long) ((0.3 * JvmInfo.jvmInfo().getMem().getDirectMemoryMax().bytes()) / workerCount);
            defaultReceiverPredictor = Math.min(defaultReceiverPredictor, Math.max(l, 64 * 1024));
        }

        long receivePredictorMin =  defaultReceiverPredictor;
        long receivePredictorMax = defaultReceiverPredictor;

        if (receivePredictorMin == receivePredictorMax) {
            return new FixedReceiveBufferSizePredictorFactory((int) receivePredictorMax);
        } else {
            return new AdaptiveReceiveBufferSizePredictorFactory((int) receivePredictorMin, (int) receivePredictorMin, (int) receivePredictorMax);
        }

    }

    public void httpAdapter(HttpServerAdapter adapter) {
        this.httpServerAdapter = adapter;
    }

    private void internalStart() {


        if (blockingServer) {

            serverBootstrap = new ServerBootstrap(new OioServerSocketChannelFactory(
                    Executors.newCachedThreadPool(),
                    Executors.newCachedThreadPool()
            ));

        } else {
            serverBootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
                    Executors.newCachedThreadPool(),
                    Executors.newCachedThreadPool(),
                    workerCount
            ));
        }

        serverBootstrap.setPipelineFactory(new com.knightRider.typeahead.http.DefaultChannelPipeline(this));

        if (tcpNoDelay != null) {
            serverBootstrap.setOption("child.tcpNoDelay", tcpNoDelay);
        }

        if (tcpKeepAlive != null) {
            serverBootstrap.setOption("child.keepAlive", tcpKeepAlive);
        }
        if (tcpSendBufferSize > 0) {
            serverBootstrap.setOption("child.sendBufferSize", tcpSendBufferSize);
        }
        if (tcpReceiveBufferSize > 0) {
            serverBootstrap.setOption("child.receiveBufferSize", tcpReceiveBufferSize);
        }

        serverBootstrap.setOption("receiveBufferSizePredictorFactory", receiveBufferSizePredictorFactory);
        serverBootstrap.setOption("child.receiveBufferSizePredictorFactory", receiveBufferSizePredictorFactory);

        if (reuseAddress != null) {
            serverBootstrap.setOption("reuseAddress", reuseAddress);
            serverBootstrap.setOption("child.reuseAddress", reuseAddress);
        }

        serverChannel = serverBootstrap.bind(new InetSocketAddress(bindHost, port));

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Http transport initialized and bounded to {}:{}", bindHost, port);
        }
    }

    @Override
    protected void doStart() throws TypeaheadException {
        internalStart();
    }

    private void internalStop() {

        if (serverChannel != null) {
            serverChannel.close().awaitUninterruptibly();
            serverChannel = null;
        }

        if (serverBootstrap != null) {
            serverBootstrap.releaseExternalResources();
            serverBootstrap = null;
        }
    }

    @Override
    protected void doStop() throws TypeaheadException {
        internalStop();
    }

    @Override
    protected void doClose() throws TypeaheadException {

    }

    void dispatchRequest(BaseRequest request, BaseChannel channel) {
        httpServerAdapter.dispatchRequest(request, channel);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {

        if (e.getCause() instanceof ReadTimeoutException) {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("Connection timeout {}", ctx.getChannel().getRemoteAddress());
            }
            ctx.getChannel().close();
        } else {
            if (!ExceptionHelper.isCloseConnectionException(e.getCause())) {

                // TODO: Exception handling during internal server error has to be well thought.
                //
                // <strong>CAUTION: Don't go to production before fixing this problem</strong>
                //
                // Here comes the big to do item, the cost of trying to run our own http stack.
                // So, in case of bad programming, such as an error happened during parsing the incoming request.
                // For example, try posting alphabets in request param where number is expected.
                // The type parsing error would reflect back caller through below code.
                //
                // Currently, the way it works is -- the connection is just closed and no information is sent to
                // caller in case of error. She is left out in middle of forest.
                //
                // Need to find out such example that how response is prepared in case of exception happens in server.
                // Do we tell that to caller? Or just blindly close the connection.
                //
                // Below code writes a server error empty response.
                //
                // HttpResponse httpResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1,
                //                                      HttpResponseStatus.INTERNAL_SERVER_ERROR);
                // ctx.getChannel().write(httpResponse);
                ctx.getChannel().close();

                LOGGER.warn(String.format("Caught exception while handling client http traffic due to internal error " +
                                "closing connection from channel '%s'", ctx.getChannel()),
                        e.getCause()
                );

            } else {

                // Continuation of above notes.
                //
                //
                // If the exception is not from client side, above block takes care of that.
                // then the exception should carry some status code from the handler level.
                // or action implementation level.
                //
                // This means, that let's say, when in the implementation for an Action (the rest handler)
                // one can throw TypeaheadException to tell here that what happened was purely
                // a bad programming error or some parsing/validation error. And, the exception class
                // can designed in such way that some caller specific error message can be passed in.

                ctx.getChannel().close();

                LOGGER.warn(String.format("!Caught exception while handling client http traffic, " +
                                "closing connection from channel '%s'", ctx.getChannel()),
                        e.getCause()
                );

            }
        }


    }

    public int maxContentLength() {
        return maxContentLength;
    }

    public boolean compressionEnabled() {
        return compression;
    }

    public int port() {
        return port;
    }

    public String host() {
        return bindHost;
    }

    public int maxChunkSize() {
        return maxChunkSize;
    }

    public int maxHeaderSize() {
        return maxHeaderSize;
    }

    public int maxInitialLineLength() {
        return maxInitialLineLength;
    }

    public int compressionLevel() {
        return compressionLevel;
    }

    public Integer maxCumulationBufferCapacity() {
        return maxCumulationBufferCapacity;
    }

    public Integer maxCompositeBufferComponents() {
        return maxCompositeBufferComponents;
    }

//    public TypeaheadSettings settings() {
//        return settings;
//    }

    public boolean adminEnabled() {
        return adminEnabled;
    }
}
