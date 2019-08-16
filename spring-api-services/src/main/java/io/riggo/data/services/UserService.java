package io.riggo.data.services;

import io.riggo.data.domain.User;
import io.riggo.data.exception.RiggoDataAccessException;
import io.riggo.data.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService  {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> findByEmailAndSiteId(String username, Integer siteId){
        try{
            return userRepository.findByEmailAndSiteId(username, siteId);
        }catch (Exception e){
            throw new RiggoDataAccessException(e);
        }
    }
}
