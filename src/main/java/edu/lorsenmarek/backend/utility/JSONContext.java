package edu.lorsenmarek.backend.utility;

import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.io.Reader;

public class JSONContext {
    private static final ObjectMapper mapper = new ObjectMapper();
    public static JsonNode parseReader(Reader input) throws IOException {
        return mapper.readTree(input);
    }
}
