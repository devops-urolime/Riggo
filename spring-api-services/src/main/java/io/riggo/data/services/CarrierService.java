package io.riggo.data.services;

import io.riggo.data.repositories.CarrierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarrierService {

    @Autowired
    private CarrierRepository carrierRepository;

}
