package io.riggo.data.services;

import io.riggo.data.domain.Invoice;
import io.riggo.data.exception.RiggoDataAccessException;
import io.riggo.data.repositories.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;


    public Invoice save(Invoice invoice) {
        try {
            return invoiceRepository.save(invoice);
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }


    public Optional<Invoice> findById(Integer id) {
        try {
            return invoiceRepository.findById(id);
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }


    public Optional<Invoice> findByExtSysId(String extSysId) {
        try {
            return invoiceRepository.findByExtSysId(extSysId);
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }
}
