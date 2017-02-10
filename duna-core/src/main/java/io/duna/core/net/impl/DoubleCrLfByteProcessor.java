package io.duna.core.net.impl;

import io.netty.util.ByteProcessor;

public class DoubleCrLfByteProcessor implements ByteProcessor {
    private byte[] latestBytes = new byte[4];
    private int position = 0;

    @Override
    public boolean process(byte value) throws Exception {
        latestBytes[0] = latestBytes[1];
        latestBytes[1] = latestBytes[2];
        latestBytes[2] = latestBytes[3];
        latestBytes[3] = value;

        return latestBytes[0] == 10 &&
            latestBytes[1] == 13 &&
            latestBytes[2] == 10 &&
            latestBytes[3] == 13;
    }
}
