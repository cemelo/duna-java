package io.duna.core.eventbus;

import java.net.InetAddress;

public interface NodeRegistry {

    InetAddress getNode(String event);
}
