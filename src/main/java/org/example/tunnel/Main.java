package org.example.tunnel;


import org.example.tunnel.train.Train;
import org.example.tunnel.train.TrainParser;
import org.example.tunnel.tunnel.Tunnel;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        var trains = TrainParser.parse();

        var tunnels = List.of(
                new Tunnel(1, 2),
                new Tunnel(2, 3)
        );

        try (var executor = Executors.newFixedThreadPool(trains.size())) {
            trains.forEach((train -> executor.execute(() -> {
                var trainExitedFromTunnel = false;

                while (!trainExitedFromTunnel) {
                    for (var tunnel : tunnels) {
                        var result = tunnel.tryToAddTrain(train);

                        if (result.isEmpty()) {
                            try {
                                TimeUnit.MILLISECONDS.sleep(Train.TRAIN_TIME_IN_TUNNEL_MS / 2);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            continue;
                        }

                        train.goThroughTunnel(result.get());
                        tunnel.removeTrain(train);

                        trainExitedFromTunnel = true;
                        break;
                    }
                }
            })));
        }
    }
}