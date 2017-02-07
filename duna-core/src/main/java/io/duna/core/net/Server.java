package io.duna.core.net;

import java.util.concurrent.Future;

public interface Server {

    Future<Void> listen();
}
