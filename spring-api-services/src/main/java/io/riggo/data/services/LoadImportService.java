package io.riggo.data.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import io.riggo.data.Utils;
import io.riggo.data.domain.*;
import io.riggo.data.exception.LoadObjectConfilictExeception;
import io.riggo.data.exception.RiggoDataAccessException;
import io.riggo.data.repositories.LoadRepository;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.Map;


/**
 * This is a @Service that talks to multiple models as the Load object is composed of multiple models.
 */
@Service
@Configurable
public class LoadImportService {

    @Autowired
    ShipperService shipperService;

    @Autowired
    EquipmentTypeService equipmentTypeService;

    @Autowired
    CarrierService carrierService;

    @Autowired
    AddressService addressService;

    @Autowired
    LoadStopService loadStopService;

    @Autowired
    LocationService locationService;

    @Autowired
    TruckerService truckerService;


    @Autowired
    LoadService loadService;

    ObjectMapper objectMapper;

    @Autowired
    private LoadRepository loadRepository;

    /**
     * The core process of saving happens here
     *
     * @param logger       logger for logging - will move to aws
     * @param objectMapper - mapper object so it gets reused
     * @param all          - All json keys we need
     * @param key          - the pirmary object id
     * @param isUpadate    - create , or update/patch
     * @return Response to request based on success or failure
     * @throws LoadObjectConfilictExeception
     * @throws RiggoDataAccessException
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)

    public ResponseEntity importLoad(Logger logger, ObjectMapper objectMapper,
                                     Map<String, Object> all,
                                     String key, boolean isUpadate) throws LoadObjectConfilictExeception, RiggoDataAccessException {
        this.objectMapper = objectMapper;
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        if (key == null) {

            ResponseEntity<String> re = new ResponseEntity<String>(
                    new JSONObject().put("message", "Ensure your id is properly specifed")
                            .toString()
                    , HttpStatus.BAD_REQUEST);
            return re;
        }
        boolean valChk;
        key = key.replaceAll("\"", "");//Clean up junk from mapper.
        valChk = chkLoadEntityExists(loadService, key);
        if (valChk) {
            return getLoadExistsResposne();
        }

        Load rl = new Load();
        rl.setExtSysId(key);
        String objStr = (String) all.get("shipper");
        if (!Strings.isNullOrEmpty(key)) {
            Shipper shp = saveSaveShipper(objStr);
            if (shp != null && shp.getExtSysId() != "null")
                rl.setShipperByShipperId(shp);
        }

        objStr = (String) all.get("carrier~carrier");
        Carrier carrier;
        if (!Strings.isNullOrEmpty(objStr)) {
            carrier = saveCarrier(objStr);
            if (carrier != null && (!Strings.isNullOrEmpty(carrier.getExtSysId())))
                rl.setCarrier(carrier.getId().intValue());
        }

        objStr = (String) all.get("driver");
        Trucker trk;
        if (!Strings.isNullOrEmpty(objStr)) {
            trk = saveTrucker(objStr);
            if (trk != null && (!Strings.isNullOrEmpty(trk.getExtSysId()))) {
                rl.setDriver(trk.getId().intValue());
            }
        }
        objStr = (String) all.get("load~equipment_type"); // convert to enum - figure out how you can have nested enum
        objStr = objStr.replaceAll(":\"Van\"", ":1");
        objStr = objStr.replaceAll(":\"FlatBed\"", ":2");
        objStr = objStr.replaceAll(":\"Reefer\"", ":3");

        EquipmentType et; // for null check
        if (objStr != null) {
            et = saveEquipment(objStr);
            if (et != null && (!Strings.isNullOrEmpty(et.getExtSysId()))) {
                rl.setEquipmentTypeId(et.getType());
            }
        }


        rl.setLocationBasedSvcsReq((Boolean) all.get("location_based_svcs_req"));// Mitul todo
        rl.setTransportMode((String) all.get("transport_mode"));
        rl.setPostedRate(Utils.getBig((String) all.get("posted_rate")));
        rl.setPostedCurrency(new Short("1"));
        rl.setInsuranceAmt(Utils.getBig((String) all.get("insurance_amt")));
        rl.setInsurnaceCurrency(new Short("1"));
        rl.setTotalWeight(Utils.getBig((String) all.get("total_weight")));
        rl.setWeightUom(new Short("1"));//LBS
        rl.setLoadStatus(new Integer("1"));//TODO:// find out status.
        rl.setTeamReq(Utils.getBool((String) all.get("team_req")));
        rl.setFoodGradeTrailerReq(Utils.getBool((String) all.get("food_grade_trailer_req")));
        rl.setTempControlReq(Utils.getBool((String) all.get("temp_control_req")));
        rl.setName((String) all.get("name"));
        rl.setInvoiceTotal(Utils.getBig((String) all.get("invoice_total")));
        rl.setCarrierQuoteTotal(Utils.getBig((String) all.get("carrier_quote_total")));
        rl.setCarrierInvoiceTotal(Utils.getBig((String) all.get("carrier_invoice_total")));
        rl.setCustomerQuoteTotal(Utils.getBig((String) all.get("customer_quote_total")));
        rl.setCustomerTransportTotal(Utils.getBig((String) all.get("customer_transport_total")));
        rl.setDeliveryStatus((String) all.get("delivery_status"));
        rl.setDistanceKilometers(Utils.getBig((String) all.get("distance_kilometers")));
        rl.setHazMat(Utils.getBool((String) all.get("haz_mat")));
        rl.setLoadStatusReq(Utils.getBool((String) all.get("load_status_req")));
        rl.setMarginInvoiced(Utils.getBig((String) all.get("margin_invoiced")));
        rl.setMarginPiad(Utils.getBig((String) all.get("margin_piad")));
        rl.setMarginPctInvoiced(Utils.getBig((String) all.get("margin_pct_invoiced")));
        rl.setModeName(((String) all.get("mode_name")).replaceAll("\"", ""));
        rl.setOnTimeDeliveryCounter((String) all.get("on_time_delivery_counter"));
        rl.setOrderDate(Utils.getDate((String) all.get("order_date")));
        rl.setExpectedDeliveryDate(Utils.getDate((String) all.get("expected_delivery_date")));
        rl.setExpectedShipDate(Utils.getDate((String) all.get("expected_ship_date")));
        rl.setSalesStatus((String) all.get("sales_status"));
        rl.setSalesScheduleStatus((String) all.get("sales_schedule_status"));
        rl.setLoadShippingStatus((String) all.get("load_shipping_status"));
        rl.setSiteUrl((String) all.get("site_url"));
        rl.setPickupDevlieryNumber((String) all.get("pickup_devliery_number"));
        rl.setStopReferenceNumber((String) all.get("stop_reference_number"));
        rl.setLoadUrl((String) all.get("load_url"));
        rl.setExtSysTenantId("0");


        rl = loadService.save(rl);

        key = (String) all.get("$load_stop~1");

        if (key != null)
            saveStop(key, 1);

        key = (String) all.get("$load_stop~2");
        if (key != null)
            saveStop(key, 2);


        return new ResponseEntity<String>(new JSONObject()
                .put("message", "Saved")
                .put("id", rl.encode(rl.getId().toString(), 0)).toString(),
                HttpStatus.CREATED);

    }

    /**
     * Check if object is empty
     *
     * @param object object to check
     * @return status of check.
     */
    boolean objectHasNullExtId(Object object) {
        try {
            Field field = object.getClass().getDeclaredField("extSysId");
            field.setAccessible(true);
            Object value = field.get(object);
            if (value instanceof String) {
                String tmp = (String) value;
                if ((!Strings.isNullOrEmpty(tmp)) && (!tmp.equals("null"))) {
                    return false;
                }
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * Save an shipper
     *
     * @param shpStr shipper data from parsing
     * @return Shipper (customer) or null on failure
     */
    Shipper saveSaveShipper(String shpStr) {


        String ext;
        Shipper shp = null;
        try {
            shp = null;
            Shipper shpParse = (Shipper) getObject(shpStr, Shipper.class);

            if (shpParse != null && !objectHasNullExtId(shpParse)) {
                ext = shpParse.getExtSysId();

                Shipper shpchk = (Shipper) checkEntityExists(shipperService, ext);
                shp = shpchk != null ? shpchk : shpParse;


                if (shpchk != null) {
                    shp.setName(shpParse.getName());

                }
                if (!Strings.isNullOrEmpty(shp.getExtSysId()))
                    shp = shipperService.save(shp);
                else
                    return null;
            } else {
                return null;
            }
        } catch (RiggoDataAccessException e) {
            e.printStackTrace();
        }


        return shp;
    }

    /**
     * Save an equipment tupe
     * @param etStr equipment data from parsing
     * @return EquipmentType (vehicle) or null on failure
     */
    EquipmentType saveEquipment(String etStr) {

        EquipmentType eqp = null;

        try {
            EquipmentType etParse = (EquipmentType) getObject(etStr, EquipmentType.class);


            String key = "";
            if (etParse != null && !objectHasNullExtId(etParse)) {
                key = etParse.getExtSysId();

            } else {
                return null;
            }

            EquipmentType et2 = (EquipmentType) checkEntityExists(equipmentTypeService, key);

            eqp = et2 != null ? et2 : etParse;

            if (et2 != null) {//update
                eqp.setName(etParse.getName());
                eqp.setType(etParse.getType());
            }
            // final save
            if (!Strings.isNullOrEmpty(eqp.getExtSysId()))
                eqp = equipmentTypeService.save(eqp);
            else
                return null;
        } catch (RiggoDataAccessException e) {
            eqp = null;
            e.printStackTrace();
        }
        return eqp;
    }

    /**
     * Save the carrier
     * @param carStr incoming data for carrier
     * @return Carrier object from parsing or null on failure
     */
    Carrier saveCarrier(String carStr) {

        Carrier car = null;
        try {
            Carrier carParse = (Carrier) getObject(carStr, Carrier.class);
            String key = "";
            if (carParse != null && !objectHasNullExtId(carParse))
                key = carParse.getExtSysId();
            else
                return null;


            Carrier c2 = (Carrier) checkEntityExists(carrierService, key);

            car = c2 != null ? c2 : carParse;

            if (car.getId() != null) {//create
                car.setName(carParse.getName());
                car.setMcNumber(carParse.getMcNumber());
                //car.setExtSysId("0");// empty
            }
            if (!Strings.isNullOrEmpty(car.getExtSysId()))
                car = carrierService.save(car);
            else
                return null;

        } catch (NullPointerException e) {
            car = null;
            e.printStackTrace();
        }
        return car;

    }

    /**
     * This methods the maste control that splits data in to stops locations and address
     * @param stopStr the sql from json
     * @param number the stopnumber
     * @return saved stop or null on failure
     */
    LoadStop saveStop(String stopStr, int number) {


        Address addr = saveAddress(stopStr);

        saveLocation(stopStr, addr);

        LoadStop ls = null;
        try {
            LoadStop loadStop = (LoadStop) getObject(stopStr, LoadStop.class);
            String key = "";
            if (loadStop != null && !objectHasNullExtId(loadStop))
                key = loadStop.getExtSysId();
            else
                return null;

            LoadStop l2 = (LoadStop) checkEntityExists(loadStopService, key);

            ls = l2 != null ? l2 : loadStop;

            ls.setStopNumber(Integer.valueOf(number));
            ls.setType(Integer.valueOf(number));//for now - until we have grater clarity

            if (!Strings.isNullOrEmpty(ls.getExtSysId()))
                ls = loadStopService.save(ls);

            else
                return null;

        } catch (NullPointerException e) {
            ls = null;
            e.printStackTrace();
        }
        return ls;

    }

    Location saveLocation(String addrStr, Address addr) {

        Location loc = null;
        try {
            Location locParse = (Location) getObject(addrStr, Location.class);
            String key = "";
            if (locParse != null && !objectHasNullExtId(locParse))
                key = locParse.getExtSysId();
            else
                return null;

            Location l2 = (Location) checkEntityExists(locationService, key);

            loc = l2 != null ? l2 : locParse;

            if (loc.getId() != null) {//update
                loc.setAddressId(addr.getId());
                loc.setName(locParse.getName());
            }
            if (!Strings.isNullOrEmpty(loc.getExtSysId()))
                loc = locationService.save(loc);
            else
                return null;

        } catch (NullPointerException e) {
            e.printStackTrace();
            loc = null;
        }
        return loc;

    }

    Address saveAddress(String addrStr) {

        Address address = null;
        try {
            Address addrParse = (Address) getObject(addrStr, Address.class);
            String key = "";
            if (addrParse != null && !objectHasNullExtId(addrParse))
                key = addrParse.getExtSysId();
            else
                return null;

            Address c2 = (Address) checkEntityExists(addressService, key);

            address = c2 != null ? c2 : addrParse;

            if (c2 != null) {//update
                address.setExtSysId(addrParse.getExtSysId());
                address.setAddress2(addrParse.getAddress2());
                address.setCity(addrParse.getCity());
                address.setState(addrParse.getState());
                address.setCountry(addrParse.getCountry());
                address.setPostalCode(addrParse.getPostalCode());
            }
            address = addressService.save(address);

        } catch (NullPointerException e) {
            e.printStackTrace();
            address = null;
        }
        return address;

    }


    Trucker saveTrucker(String truckerStr) {
        Trucker trucker = null;
        try {
            Trucker tParse = (Trucker) getObject(truckerStr, Trucker.class);
            String key;
            if (tParse != null && !objectHasNullExtId(tParse)) {
                key = tParse.getExtSysId();

                Trucker t2 = (Trucker) checkEntityExists(truckerService, key);

                trucker = t2 != null ? t2 : tParse;


                if (t2 != null) {//update
                    trucker.setExtSysId(tParse.getExtSysId());
                    trucker.setLastName(tParse.getLastName());
                    trucker.setFirstName(tParse.getFirstName());

                    if (Strings.isNullOrEmpty(trucker.getLastName())) {
                        trucker.setLastName("Not specified");
                    }

                }

            } else {
                return null;
            }

            trucker = truckerService.save(trucker);
        } catch (NullPointerException e) {
            e.printStackTrace();
            trucker = null;
        }
        return trucker;
    }


    private Object getObject(String jsonStr, Class target) {
        Object o;

        try {
            o = objectMapper.readValue(jsonStr, target);


        } catch (Exception e) {
            return null;
        }
        return o;
    }


    private Object checkEntityExists(RiggoService rs, String checkKey) {
        Object chk = null;
        if ((!Strings.isNullOrEmpty(checkKey)) && checkKey.equals("null"))
            return null;
        if (checkKey != null) {

            if (Utils.isNumeric(checkKey)) {
                Long id = Long.parseLong(checkKey);
                chk = rs.findById(id);
                return chk;
            } else {
                chk = rs.findByExtSysId(checkKey);
                return chk;


            }
        }
        return chk;
    }

    private boolean chkLoadEntityExists(LoadService rs, String checkKey) {
        if (checkKey != null) {
            if (Utils.isNumeric(checkKey)) {
                Long id = Long.parseLong(checkKey);
                if (rs.findById(id) != null) {
                    return true;

                }
            } else {
                String extid = checkKey;

                try {
                    if (rs.findByExtSysId(extid) != null) {
                        return true;

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


    private ResponseEntity getLoadExistsResposne() {
        return new ResponseEntity<>(new JSONObject().put("message", "Load can not exist when trying to create")
                .toString(),
                HttpStatus.CONFLICT);
    }


}
