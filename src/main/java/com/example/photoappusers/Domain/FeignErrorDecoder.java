package com.example.photoappusers.Domain;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

// @Component
public class FeignErrorDecoder implements ErrorDecoder {
    /*
    Environment env;

    @Autowired
    public FeignErrorDecoder(Environment environment) {
        this.env = environment;
    }
    */

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 400:
                break;
            case 404:
                if(methodKey.contains("getAlbums")) {
                    return new ResponseStatusException(HttpStatus.valueOf(response.status()), "Users albums are not found. " + response.reason());
                }
                break;
            default:
                return new Exception(response.reason());
        }
        return null;
    }
}
