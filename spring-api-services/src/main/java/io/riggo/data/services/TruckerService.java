package io.riggo.data.services;

import io.riggo.data.repositories.TruckerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TruckerService{

    @Autowired
    private TruckerRepository truckerRepository;

}
