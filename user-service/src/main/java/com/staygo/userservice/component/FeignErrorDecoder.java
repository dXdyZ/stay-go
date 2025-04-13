package com.staygo.userservice.component;

import com.staygo.userservice.exception.ErrorResponse;
import com.staygo.userservice.exception.FeignCustomServerErrorException;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.time.Instant;

public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        if (response.status() >= 500) {
            return new FeignCustomServerErrorException(new ErrorResponse(
                    Instant.now(),
                    response.status(),
                    "Server error"
            ));
        }
        return new Default().decode(s, response);
    }
}
