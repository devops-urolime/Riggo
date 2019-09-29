package io.riggo.data.services;

import io.riggo.data.domain.QuoteLoad;
import io.riggo.data.exception.RiggoDataAccessException;
import io.riggo.data.repositories.QuoteLoadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class QuoteLoadService {

    @Autowired
    private QuoteLoadRepository quoteLoadRepository;

    public Optional<List<QuoteLoad>> findQuotesBySite(
            Integer siteId, List<Integer> quoteStatusList, List<Integer> loadStatusList,
            LocalDateTime expectedDeliveryDateStart, LocalDateTime expectedDeliveryDateEnd
    ){
        try {
            return quoteLoadRepository.findQuotesBySite(siteId, quoteStatusList, loadStatusList, expectedDeliveryDateStart, expectedDeliveryDateEnd);
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }


    public Optional<List<QuoteLoad>> findQuotesBySiteUser(
            Integer siteId,  String email, List<Integer> quoteStatusList,
            List<Integer> loadStatusList, LocalDateTime expectedDeliveryDateStart,
            LocalDateTime expectedDeliveryDateEnd
    ){
        try {
            return quoteLoadRepository.findQuotesBySiteUser(siteId, email, quoteStatusList, loadStatusList, expectedDeliveryDateStart, expectedDeliveryDateEnd);
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }
}
