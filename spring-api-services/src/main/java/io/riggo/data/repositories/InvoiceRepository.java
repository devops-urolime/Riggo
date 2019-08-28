package io.riggo.data.repositories;

import io.riggo.data.domain.Invoice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends CrudRepository<Invoice, Integer> {

    @Query("select i from Invoice i where i.extSysId = :extSysId")
    Optional<Invoice> findByExtSysId(@Param("extSysId") String extSysId);

    @Query("SELECT i " +
            "FROM io.riggo.data.domain.Invoice i " +
            "JOIN io.riggo.data.domain.Load l ON l.id = i.loadId " +
            "JOIN io.riggo.data.domain.Shipper s ON s.id = l.shipperId " +
            "JOIN io.riggo.data.domain.Site site ON l.siteId = site.id " +
            "WHERE i.status IN :invoiceStatusList AND site.id = :siteId " +
            "AND l.loadStatus IN :loadStatusList " +
            "AND l.expectedDeliveryDate >= :expectedDeliveryDateStart AND l.expectedDeliveryDate <= :expectedDeliveryDateEnd " +
            "GROUP BY i.id")
    Optional<List<Invoice>> findInvoicesBySite(
            @Param("siteId") Integer siteId,
            @Param("invoiceStatusList") List<Integer> invoiceStatusList,
            @Param("loadStatusList") List<Integer> loadStatusList,
            @Param("expectedDeliveryDateStart") LocalDate expectedDeliveryDateStart,
            @Param("expectedDeliveryDateEnd") LocalDate expectedDeliveryDateEnd
    );

    @Query("SELECT i " +
            "FROM io.riggo.data.domain.Invoice i " +
            "JOIN io.riggo.data.domain.Load l ON l.id = i.loadId " +
            "JOIN io.riggo.data.domain.Shipper s ON s.id = l.shipperId " +
            "JOIN io.riggo.data.domain.Site site ON l.siteId = site.id " +
            "JOIN io.riggo.data.domain.SiteUser site_user ON site_user.siteId = site.id " +
            "JOIN io.riggo.data.domain.User u ON u.id = site_user.userId " +
            "JOIN io.riggo.data.domain.ShipperUser su ON s.id = su.shipperId AND su.userId = u.id " +
            "WHERE i.status IN :invoiceStatusList AND site.id = :siteId " +
            "AND u.email = :email AND l.loadStatus IN :loadStatusList " +
            "AND l.expectedDeliveryDate >= :expectedDeliveryDateStart AND l.expectedDeliveryDate <= :expectedDeliveryDateEnd")
    Optional<List<Invoice>> findInvoicesBySiteUser(
            @Param("siteId") Integer siteId,
            @Param("email") String email,
            @Param("invoiceStatusList") List<Integer> invoiceStatusList,
            @Param("loadStatusList") List<Integer> loadStatusList,
            @Param("expectedDeliveryDateStart") LocalDate expectedDeliveryDateStart,
            @Param("expectedDeliveryDateEnd") LocalDate expectedDeliveryDateEnd
    );
}