package io.riggo.data.services;

import io.riggo.data.domain.InvoiceLoad;
import io.riggo.data.exception.RiggoDataAccessException;
import io.riggo.data.repositories.InvoiceLoadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceLoadService {

    @Autowired
    private InvoiceLoadRepository invoiceLoadRepository;

    public Optional<List<InvoiceLoad>> findInvoicesBySite(
            Integer siteId, List<Integer> invoiceStatusList, List<Integer> loadStatusList,
            LocalDateTime expectedDeliveryDateStart, LocalDateTime expectedDeliveryDateEnd
    ){
        try {
            return invoiceLoadRepository.findInvoicesBySite(siteId, invoiceStatusList, loadStatusList, expectedDeliveryDateStart, expectedDeliveryDateEnd);
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }


    public Optional<List<InvoiceLoad>> findInvoicesBySiteUser(
            Integer siteId,  String email, List<Integer> invoiceStatusList,
            List<Integer> loadStatusList, LocalDateTime expectedDeliveryDateStart,
            LocalDateTime expectedDeliveryDateEnd
    ){
        try {
            return invoiceLoadRepository.findInvoicesBySiteUser(siteId, email, invoiceStatusList, loadStatusList, expectedDeliveryDateStart, expectedDeliveryDateEnd);
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }
}
