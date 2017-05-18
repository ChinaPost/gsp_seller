package com.framework.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ToStringBuilder {

    private static ObjectMapper mapper = new ObjectMapper();

    /**
     *  Convenient method for Class to implement toString method.
     *  You might want to implement your own toString method if require better performance.
     * @param object
     * @return json string
     */
    public static String stringOf(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("", e);
        }
    }
}
