/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.bbstone.pisces.client;

import com.bbstone.pisces.client.base.ClientCmdRegister;
import com.bbstone.pisces.config.Config;
import com.bbstone.pisces.util.ConstUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.util.CharsetUtil;

import java.io.File;

/**
 * Sends one message when a connection is open and echoes back any received
 * data to the server.  Simply put, the echo client initiates the ping-pong
 * traffic between the echo client and server by sending the first message to
 * the server.
 */
public final class FileClient {
    /*
        static final boolean SSL = System.getProperty("ssl") != null;
        static final String HOST = System.getProperty("host", "127.0.0.1");
        static final int PORT = Integer.parseInt(System.getProperty("port", "8080"));
        static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));
        */
    static final boolean SSL = Config.sslEnabled;
    static final String HOST = Config.host;
    static final int PORT = Config.port;

    public static void main(String[] args) throws Exception {
        ClientCmdRegister.init();
//        ClientCache.cleanAll();
        // Configure SSL.
        final SslContext sslCtx;
        if (SSL) {
//            SelfSignedCertificate ssc = new SelfSignedCertificate();
//            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();

            File certChainFile = Config.clientSSLCertChainFile;
            File keyFile = Config.clientSSLKeyFile;
            File rootFile = Config.clientSSLRootFile;
            sslCtx = SslContextBuilder.forClient().keyManager(certChainFile, keyFile).trustManager(rootFile).build();
        } else {
            sslCtx = null;
        }
        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {

                            ChannelPipeline p = ch.pipeline();
                            if (sslCtx != null) {
                                p.addLast(sslCtx.newHandler(ch.alloc()));
                            }
                            // outbound(BFileReq)
                            p.addLast(new ProtobufEncoder());

                            // --- inbound
                            // if os not support zero-copy/sslEnabled, used this, must be the first inbound handler
                            p.addLast(new ChunkedReadHandler());

                            // ----- decode and handle (BFileRsp + FileRegion) data stream
                            ByteBuf delimiter = Unpooled.copiedBuffer(ConstUtil.delimiter.getBytes(CharsetUtil.UTF_8));
                            // inbound frameLen = chunkSize[default: 8192] + BFileRsp header)
//                            p.addLast(new DelimiterBasedFrameDecoder(10240, delimiter));
                            p.addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, delimiter));
                            p.addLast(new FileClientHandler());
                        }
                    });


            // Start the client.
            ChannelFuture f = b.connect(HOST, PORT).sync();

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully();
        }
    }
}
