package com.staygo.service.car;

import com.staygo.repository.transport_repo.TransportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarReservationService {
    private final TransportRepository transportRepository;

    @Autowired
    public CarReservationService(TransportRepository transportRepository) {
        this.transportRepository = transportRepository;
    }
}
