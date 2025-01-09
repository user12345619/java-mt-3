package org.example.tunnel.train;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.tunnel.City;
import org.example.tunnel.tunnel.TunnelInfo;

import java.util.concurrent.TimeUnit;

public record Train(int id, City destination) {
    public static final int TRAIN_TIME_IN_TUNNEL_MS = 500;

    private static final Logger logger = LogManager.getLogger();

    public void goThroughTunnel(TunnelInfo tunnelInfo) {
        logger.info("Train#{} is currently running in Tunnel#{} ({})", id, tunnelInfo.id(), tunnelInfo.load());

        try {
            TimeUnit.MILLISECONDS.sleep(TRAIN_TIME_IN_TUNNEL_MS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
