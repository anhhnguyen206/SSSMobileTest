package com.example.anhhnguyen.myapplication.util;

import com.octo.android.robospice.persistence.exception.SpiceException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;

/**
 * Created by anhhnguyen on 7/14/2014.
 */
public class ExceptionUtils {
    public static String getErrorMessage(SpiceException spiceException){
        String response = "";
        String error = spiceException.getLocalizedMessage();
        if(spiceException.getCause() instanceof HttpStatusCodeException)
        {
            HttpStatusCodeException
                    exception = (HttpStatusCodeException)spiceException.getCause();

            response = exception.getResponseBodyAsString();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            error = jsonNode.findValue("message").getTextValue();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return error;
    }
}
