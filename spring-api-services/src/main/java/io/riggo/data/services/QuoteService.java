package io.riggo.data.services;

import io.riggo.data.domain.Quote;
import io.riggo.data.exception.RiggoDataAccessException;
import io.riggo.data.repositories.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuoteService {

    @Autowired
    private QuoteRepository quoteRepository;


    public Quote save(Quote quote) {
        try {
            return quoteRepository.save(quote);
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }


    public Optional<Quote> findById(Integer id) {
        try {
            return quoteRepository.findById(id);
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }


    public Optional<Quote> findByExtSysId(String extSysId) {
        try {
            return quoteRepository.findByExtSysId(extSysId);
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }
}
