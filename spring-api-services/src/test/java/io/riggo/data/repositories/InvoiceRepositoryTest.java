package io.riggo.data.repositories;

import io.riggo.data.domain.Invoice;
import io.riggo.data.domain.InvoiceStatus;
import io.riggo.data.domain.LoadSubStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class InvoiceRepositoryTest {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Test
    public void findByExtSysId() {
        //given

        //when
        Optional<Invoice> invoice = invoiceRepository.findByExtSysId("extSysId1");

        //then invoice isPresent and id = 1
        assertTrue( invoice.isPresent());
        assertTrue(invoice.get().getId() == 1);
    }

    @Test
    public void findInvoicesBySite(){
        //given
        Integer siteId = 100;
        List<Integer> invoiceStatusList = Arrays.asList(new Integer[]{InvoiceStatus.ACCEPTED.getColVal()});
        List<Integer> loadStatusList = Arrays.asList(new Integer[]{LoadSubStatus.DOCUMENTS_RECEIVED.getColVal(), LoadSubStatus.PENDING_DOCUMENTS.getColVal(), LoadSubStatus.INVOICED.getColVal()});
        LocalDate expectedDeliveryDateStart = LocalDate.of(2019, Month.JULY, 1);
        LocalDate expectedDeliveryDateEnd = LocalDate.of(2019, Month.AUGUST, 31);

        //when
        Optional<List<Invoice>> invoiceList = invoiceRepository.findInvoicesBySite(siteId, invoiceStatusList, loadStatusList, expectedDeliveryDateStart, expectedDeliveryDateEnd);

        //assertTrue(invoiceList.isPresent());
        //assertTrue(invoiceList.get().size() == 10);
    }


    @Test
    public void findInvoicesBySiteUser(){
        //given
        Integer siteId = 100;
        String email = "email@riggo.io";
        List<Integer> invoiceStatusList = Arrays.asList(new Integer[]{InvoiceStatus.ACCEPTED.getColVal()});
        List<Integer> loadStatusList = Arrays.asList(new Integer[]{LoadSubStatus.DOCUMENTS_RECEIVED.getColVal(), LoadSubStatus.PENDING_DOCUMENTS.getColVal(), LoadSubStatus.INVOICED.getColVal()});
        LocalDate expectedDeliveryDateStart = LocalDate.of(2019, Month.JULY, 1);
        LocalDate expectedDeliveryDateEnd = LocalDate.of(2019, Month.AUGUST, 31);

        //when
        Optional<List<Invoice>> invoiceList = invoiceRepository.findInvoicesBySiteUser(siteId, email, invoiceStatusList, loadStatusList, expectedDeliveryDateStart, expectedDeliveryDateEnd);

        //assertTrue(invoiceList.isPresent());
        //assertTrue(invoiceList.get().size() == 10);
    }
}
