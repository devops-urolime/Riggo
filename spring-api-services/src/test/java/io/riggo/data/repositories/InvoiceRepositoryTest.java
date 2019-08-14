package io.riggo.data.repositories;

import io.riggo.data.domain.Invoice;
import io.riggo.data.domain.Menu;
import io.riggo.data.domain.MenuType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

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
}
