package io.riggo.data.repositories;

import io.riggo.data.domain.Invoice;
import io.riggo.data.domain.InvoiceLoad;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceLoadRepository extends CrudRepository<InvoiceLoad, Integer> {

    @Query("SELECT NEW io.riggo.data.domain.InvoiceLoad(i.id as id," +
            "i.extSysId as extSysId, i.loadId as loadId," +
            "i.quoteDate as quoteDate, i.status as status," +
            "i.comments as comments, i.netFreightCharges as netFreightCharges," +
            "i.fuelSurcharge as fuelSurcharge, i.accessorialCharges as accessorialCharges," +
            "i.transportationTotal as transportationTotal, i.customerQuoteTotal as customerQuoteTotal," +
            "l.distanceMiles as distanceMiles) " +
            "FROM io.riggo.data.domain.Invoice i " +
            "JOIN io.riggo.data.domain.Load l ON l.id = i.loadId " +
            "JOIN io.riggo.data.domain.Shipper s ON s.id = l.shipperId " +
            "JOIN io.riggo.data.domain.Site site ON l.siteId = site.id " +
            "WHERE i.status IN :invoiceStatusList AND site.id = :siteId " +
            "AND l.loadStatus IN :loadStatusList " +
            "AND l.expectedDeliveryDate >= :expectedDeliveryDateStart AND l.expectedDeliveryDate <= :expectedDeliveryDateEnd " +
            "GROUP BY i.id")
    Optional<List<InvoiceLoad>> findInvoicesBySite(
            @Param("siteId") Integer siteId,
            @Param("invoiceStatusList") List<Integer> invoiceStatusList,
            @Param("loadStatusList") List<Integer> loadStatusList,
            @Param("expectedDeliveryDateStart") LocalDate expectedDeliveryDateStart,
            @Param("expectedDeliveryDateEnd") LocalDate expectedDeliveryDateEnd
    );

    @Query("SELECT NEW io.riggo.data.domain.InvoiceLoad(i.id as id," +
            "i.extSysId as extSysId, i.loadId as loadId," +
            "i.quoteDate as quoteDate, i.status as status," +
            "i.comments as comments, i.netFreightCharges as netFreightCharges," +
            "i.fuelSurcharge as fuelSurcharge, i.accessorialCharges as accessorialCharges," +
            "i.transportationTotal as transportationTotal, i.customerQuoteTotal as customerQuoteTotal," +
            "l.distanceMiles as distanceMiles) " +
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
    Optional<List<InvoiceLoad>> findInvoicesBySiteUser(
            @Param("siteId") Integer siteId,
            @Param("email") String email,
            @Param("invoiceStatusList") List<Integer> invoiceStatusList,
            @Param("loadStatusList") List<Integer> loadStatusList,
            @Param("expectedDeliveryDateStart") LocalDate expectedDeliveryDateStart,
            @Param("expectedDeliveryDateEnd") LocalDate expectedDeliveryDateEnd
    );
}