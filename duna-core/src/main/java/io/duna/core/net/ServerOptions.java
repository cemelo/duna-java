package io.duna.core.net;

import io.duna.core.net.ssl.SslOptions;

public class ServerOptions {

    private String host;
    private int port;
    private int threadPoolSize;
    private boolean reuseAddress;
    private boolean enableNetworkLogging;
    private int channelBacklog;
    private int maxFrameSize;
    private int idleTimeout;
    private SslOptions ssl;

    public ServerOptions() {
    }

    public String getHost() {
        return host;
    }

    public ServerOptions setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    public ServerOptions setPort(int port) {
        this.port = port;
        return this;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public ServerOptions setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
        return this;
    }

    public boolean isReuseAddress() {
        return reuseAddress;
    }

    public ServerOptions setReuseAddress(boolean reuseAddress) {
        this.reuseAddress = reuseAddress;
        return this;
    }

    public boolean isEnableNetworkLogging() {
        return enableNetworkLogging;
    }

    public ServerOptions setEnableNetworkLogging(boolean enableNetworkLogging) {
        this.enableNetworkLogging = enableNetworkLogging;
        return this;
    }

    public int getChannelBacklog() {
        return channelBacklog;
    }

    public ServerOptions setChannelBacklog(int channelBacklog) {
        this.channelBacklog = channelBacklog;
        return this;
    }

    public int getMaxFrameSize() {
        return maxFrameSize;
    }

    public ServerOptions setMaxFrameSize(int maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
        return this;
    }

    public int getIdleTimeout() {
        return idleTimeout;
    }

    public ServerOptions setIdleTimeout(int idleTimeout) {
        this.idleTimeout = idleTimeout;
        return this;
    }

    public SslOptions getSsl() {
        return ssl;
    }

    public ServerOptions setSsl(SslOptions ssl) {
        this.ssl = ssl;
        return this;
    }
}
