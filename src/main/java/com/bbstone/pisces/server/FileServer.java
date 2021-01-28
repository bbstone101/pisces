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
package com.bbstone.pisces.server;

import com.bbstone.pisces.config.Config;
import com.bbstone.pisces.proto.BFileMsg;
import com.bbstone.pisces.server.base.CmdRegister;
import com.bbstone.pisces.util.ConstUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.CharsetUtil;

import java.io.File;

/**
 * Server that accept the path of a file an echo back its content.
 */
public final class FileServer {
    /*

        static final boolean SSL = System.getProperty("ssl") != null;
        // Use the same default port with the telnet example so that we can use the telnet client example to access it.
        static final int PORT = Integer.parseInt(System.getProperty("port", SSL ? "8992" : "8080"));
    */
    static final boolean SSL = Config.sslEnabled;
    static final int PORT = Config.port;

    public static void main(String[] args) throws Exception {
        CmdRegister.init();

        // Configure SSL.
        final SslContext sslCtx;
        if (SSL) {
//            SelfSignedCertificate ssc = new SelfSignedCertificate();
//            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();

            File certChainFile = Config.serverSSLCertChainFile;
            File keyFile = Config.serverSSLKeyFile;
            File rootFile = Config.serverSSLRootFile;
            sslCtx = SslContextBuilder.forServer(certChainFile, keyFile).trustManager(rootFile).clientAuth(ClientAuth.REQUIRE).build();
        } else {
            sslCtx = null;
        }
        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {

                            ChannelPipeline p = ch.pipeline();
                            if (sslCtx != null) {
                                p.addLast(sslCtx.newHandler(ch.alloc()));
                            }
                            // outbound (default ByteBuf)
                            // no encoder, direct send ByteBuf
                            // if os not support zero-copy, used ChunkedWriteHandler
                            p.addLast(new ChunkedWriteHandler());

                            // inbound(decode by the delimiter, then forward to protobuf decoder, last forward to handler)
                            ByteBuf delimiter = Unpooled.copiedBuffer(ConstUtil.delimiter.getBytes(CharsetUtil.UTF_8));
                            p.addLast(new DelimiterBasedFrameDecoder(8192, delimiter)); // frameLen = BFileReq bytes
                            p.addLast(new ProtobufDecoder(BFileMsg.BFileReq.getDefaultInstance()));
                            p.addLast(new FileServerHandler());
                        }
                    });

            // Start the server.
            ChannelFuture f = b.bind(PORT).sync();

            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
