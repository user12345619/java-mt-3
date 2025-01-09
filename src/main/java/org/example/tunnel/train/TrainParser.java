package org.example.tunnel.train;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.tunnel.City;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TrainParser {
    private static final String INPUT_FILENAME = "input.json";

    private record InputData(List<City> trains) {
    }

    public static List<Train> parse() {
        var idGenerator = TrainIdGenerator.INSTANCE;

        var objectMapper = new ObjectMapper();
        InputData inputData;

        try {
            inputData = objectMapper.readValue(new FileReader(INPUT_FILENAME), InputData.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return inputData.trains.stream().map((city) -> new Train(idGenerator.generateId(), city)).collect(Collectors.toList());
    }
}
