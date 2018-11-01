package com.thelads.service;

import com.thelads.model.Unicorn;
import com.thelads.model.UnicornCreationRequest;
import com.thelads.publisher.UnicornPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnicornService {

    @Autowired
    private UnicornPublisher unicornPublisher;

    public void createUnicorn(UnicornCreationRequest request) {
        if (request.getLatitude() == null || request.getLongitude() == null) {
            unicornPublisher.addUnicorn(new Unicorn(request.getName()));
        } else {
            unicornPublisher.addUnicorn(new Unicorn(request.getName(),
                request.getLatitude(), request.getLongitude()));
        }
    }
}
