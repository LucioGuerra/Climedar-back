package com.climedar.patient_sv.configuration.feign;

import feign.FeignException;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, feign.Response response) {
        if (response.status() == 404) {
            return new FeignException.NotFound("Error 404", response.request(), null, null);
        }
        return new Default().decode(methodKey, response);
    }
}
