package org.example.tunnel.tunnel;

import java.util.Optional;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.example.tunnel.City;
import org.example.tunnel.train.Train;

public class Tunnel {
    private static final Logger logger = LogManager.getLogger();

    private final int id;
    private final int tunnelCapacity;

    private final ReentrantLock lock = new ReentrantLock();
    private final Semaphore trainsInside;
    private City currentDirection;

    public Tunnel(int id, int tunnelCapacity) {
        this.id = id;
        this.tunnelCapacity = tunnelCapacity;
        this.trainsInside = new Semaphore(tunnelCapacity);
        this.currentDirection = City.NONE;
    }

    public Optional<TunnelInfo> tryToAddTrain(Train train) {
        var trainDestination = train.destination();

        lock.lock();
        if (currentDirection == City.NONE || currentDirection == trainDestination) {
            currentDirection = trainDestination;

            logger.info("Train#{} want to go through Tunnel#{}; Dest={}", train.id(), id, trainDestination);

            if (!trainsInside.tryAcquire()) {
                logger.info("Train#{} can't go through Tunnel#{} because it is full", train.id(), id);

                lock.unlock();
                return Optional.empty();
            }

            var trainInsideCount = tunnelCapacity - trainsInside.availablePermits();

            lock.unlock();
            return Optional.of(new TunnelInfo(id, trainInsideCount));
        } else {
            lock.unlock();
            return Optional.empty();
        }
    }

    public void removeTrain(Train train) {
        lock.lock();

        var trainInsideCount = tunnelCapacity - trainsInside.availablePermits();
        logger.info("Train#{} exited from Tunnel#{}", train.id(), id);
        trainsInside.release();

        if (trainInsideCount == 1) {
            currentDirection = City.NONE;
        }

        lock.unlock();
    }
}
