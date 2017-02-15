package io.duna.core.eventbus;

import java.net.InetAddress;
import java.util.Set;

public interface EndpointDirectory {

    InetAddress getSingleEndpoint(String eventName);

    Set<InetAddress> getAllEndpoints(String eventName);

}
