package io.duna.core.net.impl;

import io.duna.core.net.Server;
import io.duna.core.net.ServerOptions;
import io.duna.core.net.codec.EnvelopeCodec;
import io.duna.core.net.codec.PrefixCodec;
import io.duna.core.net.ssl.SslContextFactory;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.Future;

public abstract class AbstractServer implements Server {

    private ServerOptions options;

    private ChannelFuture channelFuture;

    public AbstractServer() {
        this(ConfigFactory.load());
    }

    public AbstractServer(Config config) {
        this.options = ConfigBeanFactory.create(config.getConfig(configKey()),
            ServerOptions.class);
    }

    protected abstract String configKey();

    protected abstract void registerHandlers(ChannelPipeline pipeline);

    @Override
    public Future<Void> listen() {
        if (this.channelFuture != null)
            throw new IllegalStateException("Listen already called.");

        EventLoopGroup acceptorGroup = new NioEventLoopGroup(1);
        EventLoopGroup handlerGroup = new NioEventLoopGroup();

        final SslContext sslContext = SslContextFactory.create(options.getSsl());

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap
            .group(acceptorGroup, handlerGroup)
            .channel(NioServerSocketChannel.class)
            .option(ChannelOption.SO_BACKLOG, options.getChannelBacklog())
            .option(ChannelOption.SO_REUSEADDR, options.isReuseAddress())
            .childHandler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();

                    if (options.getSsl().isEnabled() && sslContext != null) {
                        pipeline.addLast("ssl", sslContext.newHandler(ch.alloc()));
                    } else if (options.getSsl().isEnabled() && sslContext == null) {
                        throw new IllegalStateException(
                            "SSL context is null. Can't create a secure channel.");
                    }

                    if (options.isEnableNetworkLogging()) {
                        pipeline.addLast("logging", new LoggingHandler(LogLevel.DEBUG));
                    }

                    if (options.getIdleTimeout() > 0) {
                        pipeline.addLast("idle",
                            new IdleStateHandler(0, 0,
                                options.getIdleTimeout()));
                    }

                    pipeline.addLast("duna-prefix", new PrefixCodec());

                    pipeline.addLast("length-prepender", new LengthFieldPrepender(4));
                    pipeline.addLast("length-decoder",
                        new LengthFieldBasedFrameDecoder(options.getMaxFrameSize(),
                            0, 4));
                    pipeline.addLast(new EnvelopeCodec());

                    registerHandlers(pipeline);
                }
            });

        return this.channelFuture = bootstrap.bind(options.getHost(), options.getPort());
    }
}
