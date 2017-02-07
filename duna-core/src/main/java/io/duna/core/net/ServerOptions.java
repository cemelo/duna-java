package io.duna.core.net;

import io.duna.core.net.ssl.SslOptions;

public class ServerOptions {

    private String host;
    private int port;
    private int threadPoolSize;
    private boolean reuseAddress;
    private boolean enableNetworkLogging;
    private int channelBacklog;
    private int idleTimeout;
    private SslOptions ssl;

    public ServerOptions() {
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    public boolean isReuseAddress() {
        return reuseAddress;
    }

    public void setReuseAddress(boolean reuseAddress) {
        this.reuseAddress = reuseAddress;
    }

    public boolean isEnableNetworkLogging() {
        return enableNetworkLogging;
    }

    public void setEnableNetworkLogging(boolean enableNetworkLogging) {
        this.enableNetworkLogging = enableNetworkLogging;
    }

    public int getChannelBacklog() {
        return channelBacklog;
    }

    public void setChannelBacklog(int channelBacklog) {
        this.channelBacklog = channelBacklog;
    }

    public int getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(int idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    public SslOptions getSsl() {
        return ssl;
    }

    public void setSsl(SslOptions ssl) {
        this.ssl = ssl;
    }
}
