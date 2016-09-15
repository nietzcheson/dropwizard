package com.m4sg.crm4marketingsunset.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

/**
 * Created by Juan on 11/19/2014.
 */
public final class LoggerJsonObject {

    public static void logObject(Object object, Logger logger) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String valueAsString = mapper.writeValueAsString(object);
        logger.info("{}: {}",object.getClass().getSimpleName(), valueAsString);
    }
}
