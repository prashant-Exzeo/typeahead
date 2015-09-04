package com.one97.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

/**
 * Created by prashant on 4/9/15.
 */
@Component
public class DataMaper {

    public Object jsonProvider() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] json = objectMapper.writeValueAsBytes(new Object());
        return json.toString();
    }


}
