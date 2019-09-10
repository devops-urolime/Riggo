package io.riggo.data.repositories;

import io.riggo.data.domain.InvoiceLoad;
import io.riggo.data.domain.InvoiceStatus;
import io.riggo.data.domain.LoadSubStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class InvoiceLoadRepositoryTest {

    @Autowired
    private InvoiceLoadRepository invoiceLoadRepository;

    @Test
    public void findInvoicesBySite(){
        //given
        Integer siteId = 100;
        List<Integer> invoiceStatusList = Arrays.asList(new Integer[]{InvoiceStatus.ACCEPTED.getColVal()});
        List<Integer> loadStatusList = Arrays.asList(new Integer[]{LoadSubStatus.DOCUMENTS_RECEIVED.getColVal(), LoadSubStatus.PENDING_DOCUMENTS.getColVal(), LoadSubStatus.INVOICED.getColVal()});
        LocalDateTime expectedDeliveryDateStart = LocalDateTime.of(2019, Month.JULY, 1, 1, 2);
        LocalDateTime expectedDeliveryDateEnd = LocalDateTime.of(2019, Month.AUGUST, 31, 23, 59, 59, 999999999);

        //when
        Optional<List<InvoiceLoad>> invoiceList = invoiceLoadRepository.findInvoicesBySite(siteId, invoiceStatusList, loadStatusList, expectedDeliveryDateStart, expectedDeliveryDateEnd);

        assertTrue(invoiceList.isPresent());
        assertTrue(invoiceList.get().size() == 1);
    }

    @Test
    public void findInvoicesBySiteUser(){
        //given
        Integer siteId = 100;
        String email = "email@riggo.io";
        List<Integer> invoiceStatusList = Arrays.asList(new Integer[]{InvoiceStatus.ACCEPTED.getColVal()});
        List<Integer> loadStatusList = Arrays.asList(new Integer[]{LoadSubStatus.DOCUMENTS_RECEIVED.getColVal(), LoadSubStatus.PENDING_DOCUMENTS.getColVal(), LoadSubStatus.INVOICED.getColVal()});
        LocalDateTime expectedDeliveryDateStart = LocalDateTime.of(2019, Month.JULY, 1, 1, 2);
        LocalDateTime expectedDeliveryDateEnd = LocalDateTime.of(2019, Month.AUGUST, 31, 23, 59, 59, 999999999);

        //when
        Optional<List<InvoiceLoad>> invoiceList = invoiceLoadRepository.findInvoicesBySiteUser(siteId, email, invoiceStatusList, loadStatusList, expectedDeliveryDateStart, expectedDeliveryDateEnd);

        assertTrue(invoiceList.isPresent());
        assertTrue(invoiceList.get().size() == 1);
        assertTrue(invoiceList.get().get(0).getDistanceMiles().doubleValue() == 12.12);
    }
}
