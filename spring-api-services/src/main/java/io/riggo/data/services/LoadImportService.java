package io.riggo.data.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import io.riggo.data.TypeBridger;
import io.riggo.data.domain.*;
import io.riggo.data.exception.BadLoadJsonException;
import io.riggo.data.exception.LoadObjectConfilictExeception;
import io.riggo.data.exception.RiggoDataAccessException;
import io.riggo.data.repositories.LoadRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;


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
    TypeBridger typeBridger;

    private Logger logger;

    private final String message = "message";

    /**
     * The core process of saving a load happens here
     *
     * @param logger       logger for logging - will move to aws
     * @param objectMapper - mapper object so it gets reused
     * @param all          - All json keys we need
     * @param key          - the primary object id
     * @param action       - create (0), or update(1), patch(2)
     * @return Response to request based on success or failure
     * @throws LoadObjectConfilictExeception Conflict on create
     * @throws RiggoDataAccessException      Data related failures
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)

    public ResponseEntity importLoad(Logger logger, ObjectMapper objectMapper,
                                     Map<String, Object> all,
                                     String key, int action) throws LoadObjectConfilictExeception, RiggoDataAccessException {
        this.objectMapper = objectMapper;
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.logger = logger;
        typeBridger = TypeBridger.getInstance();


        if (key == null) {

            return new ResponseEntity<>(
                    new JSONObject().put(message, "Ensure your id is properly specified")
                            .toString()
                    , HttpStatus.BAD_REQUEST);

        }
        Optional<Load> ld;
        boolean valChk;
        key = key.replaceAll("\"", "");//Clean up junk from mapper.

        valChk = chkLoadEntityExists(loadService, key);
        if (valChk && action == 0) {
            return getLoadExistsResposne();
        }

        Load rl = null;

        if (action > 0 && key.startsWith("ey")) {// get by maketplace id
            RiggoBaseEntity re = new RiggoBaseEntity();
            String extid = re.encode(key, re.REVERSE);
            Long id = Long.parseLong(extid);
            ld = loadService.findById(id);
            if (ld.isPresent())
                rl = ld.get();
        } else if (action <= 0) {
            rl = new Load();//create
            rl.setExtSysId(key);
        } else {
            //update sans market place id
            ld = loadService.findByExtSysId(key);
            if (ld.isPresent())
                rl = ld.get();
        }

        if (key.equals(""))

            if (rl == null) {
                rl = new Load();

                rl.setExtSysId(key);
            }
        String objStr ;
        if (action < 2) {
            objStr = (String) all.get("shipper");
            if (!Strings.isNullOrEmpty(objStr)) {
                Shipper shp = saveSaveShipper(objStr);
                if (shp != null && !Strings.isNullOrEmpty(shp.getExtSysId()))
                    rl.setShipperByShipperId(shp.getId());
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
            // MITUL Yet to send this rl.setLocationBasedSvcsReq(typeBridger.getBool((String)all.get("location_based_svcs_req")));
            rl.setTransportMode(typeBridger.cleanQotes((String) all.get("transport_mode")));
            rl.setPostedRate(typeBridger.getBig((String) all.get("posted_rate")));
            rl.setPostedCurrency(Short.valueOf("1"));//USD - Will change when app is internationalized
            rl.setInsuranceAmt(typeBridger.getBig((String) all.get("insurance_amt")));
            rl.setInsurnaceCurrency(Short.valueOf("1"));
            rl.setTotalWeight(typeBridger.getBig((String) all.get("total_weight")));
            rl.setWeightUom(Short.valueOf("1"));//LBS - Will change when app is internationalized

            String loadStatus = typeBridger.cleanQotes((String) all.get("load_status"));
            Integer stat = typeBridger.getLoadStatusIdByName(loadStatus);
            if (action <= 1) {// not patch
                if (stat == -1) {

                    return new ResponseEntity<>(new JSONObject()
                            .put(message, "Incorrect Load status").toString(),

                            HttpStatus.BAD_REQUEST);
                }
            }


            rl.setLoadStatus(stat);
            rl.setTeamReq(typeBridger.getBool((String) all.get("team_req")));
            rl.setFoodGradeTrailerReq(typeBridger.getBool((String) all.get("food_grade_trailer_req")));
            rl.setTempControlReq(typeBridger.getBool((String) all.get("temp_control_req")));
            rl.setName(typeBridger.cleanQotes((String) all.get("name")));
            rl.setInvoiceTotal(typeBridger.getBig((String) all.get("invoice_total")));
            rl.setCarrierQuoteTotal(typeBridger.getBig((String) all.get("carrier_quote_total")));
            rl.setCarrierInvoiceTotal(typeBridger.getBig((String) all.get("carrier_invoice_total")));
            rl.setCustomerQuoteTotal(typeBridger.getBig((String) all.get("customer_quote_total")));
            rl.setCustomerTransportTotal(typeBridger.getBig((String) all.get("customer_transport_total")));
            rl.setDeliveryStatus(typeBridger.cleanQotes((String) all.get("delivery_status")));
            rl.setDistanceKilometers(typeBridger.getBig((String) all.get("distance_kilometers")));
            rl.setHazMat(typeBridger.getBool((String) all.get("haz_mat")));
            rl.setLoadStatusReq(typeBridger.getBool((String) all.get("load_status_req")));
            rl.setMarginInvoiced(typeBridger.getBig((String) all.get("margin_invoiced")));
            rl.setMarginPiad(typeBridger.getBig((String) all.get("margin_piad")));
            rl.setMarginPctInvoiced(typeBridger.getBig((String) all.get("margin_pct_invoiced")));
            rl.setModeName(typeBridger.cleanQotes((String) all.get("mode_name")));
            rl.setOnTimeDeliveryCounter((String) all.get("on_time_delivery_counter"));
            rl.setOrderDate(typeBridger.getDate((String) all.get("order_date")));
            rl.setExpectedDeliveryDate(typeBridger.getDate((String) all.get("expected_delivery_date")));
            rl.setExpectedShipDate(typeBridger.getDate((String) all.get("expected_ship_date")));
            rl.setSalesStatus(typeBridger.cleanQotes((String) all.get("sales_status")));
            rl.setSalesScheduleStatus((typeBridger.cleanQotes((String) all.get("sales_schedule_status"))));
            rl.setLoadShippingStatus(typeBridger.cleanQotes((String) all.get("load_shipping_status")));
            rl.setSiteUrl(typeBridger.cleanQotes((String) all.get("site_url")));
            rl.setPickupDevlieryNumber(typeBridger.cleanQotes((String) all.get("pickup_devliery_number")));
            rl.setStopReferenceNumber(typeBridger.cleanQotes((String) all.get("stop_reference_number")));
            rl.setLoadUrl(typeBridger.cleanQotes((String) all.get("load_url")));
            rl.setExtSysTenantId("0");


            rl = loadService.save(rl);
        }

        objStr = (String) all.get("$load_stop~1");

        if (objStr != null)
            saveStop(objStr, 1,rl);

        objStr = (String) all.get("$load_stop~2");
        if (objStr != null)
            saveStop(objStr, 2,rl);

        objStr = (String) all.get("stops");
        if (!Strings.isNullOrEmpty(objStr) && action == 2) {

            return handlePatch(objStr, rl);
        }

        return new ResponseEntity<>(new JSONObject()
                .put(message, "Saved")
                .put("id", rl.encode(rl.getId().toString(), 0)).toString(),
                HttpStatus.CREATED);

    }

    /**
     * Save Load stops
     *
     * @param objStr All stops
     * @return
     */
    private ResponseEntity handlePatch(String objStr, Load rl)  {

        JSONArray jsonArray = new JSONArray(objStr);
        if (jsonArray == null)
            return new ResponseEntity<>(new JSONObject()
                    .put(message, "Not Found")
                    .put("id", rl.encode(rl.getId().toString(), 0)).toString(),
                    HttpStatus.NOT_FOUND);


            LoadStop st;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObject = jsonArray.getJSONObject(i);
                if (jObject != null) {
                    logger.debug(jObject.toString());
                    st = saveStop(jObject.toString(), -1,rl);

                }
            }

        return new ResponseEntity<>(new JSONObject()
                .put(message, "Update successful")
                .put("id", rl.encode(rl.getId().toString(), 0)).toString(),
                HttpStatus.OK);
    }

    /**
     * Check if object is empty
     *
     * @param object object to check
     * @return status of check.
     */
    private boolean objectHasNullExtId(Object object) {
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
    private Shipper saveSaveShipper(String shpStr) {


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
            logger.error(Arrays.toString(e.getStackTrace()));
        }


        return shp;
    }

    /**
     * Save an equipment tupe
     *
     * @param etStr equipment data from parsing
     * @return EquipmentType (vehicle) or null on failure
     */
    private EquipmentType saveEquipment(String etStr) {

        EquipmentType eqp;

        try {
            EquipmentType etParse = (EquipmentType) getObject(etStr, EquipmentType.class);


            String key;
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
            logger.error(Arrays.toString(e.getStackTrace()));
        }
        return eqp;
    }

    /**
     * Save the carrier
     *
     * @param carStr incoming data for carrier
     * @return Carrier object from parsing or null on failure
     */
    private Carrier saveCarrier(String carStr) {

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
            logger.error(Arrays.toString(e.getStackTrace()));
        }
        return car;

    }

    /**
     * This methods the maste control that splits data in to stops locations and address
     *
     * @param stopStr the sql from json
     * @param number  the stopnumber
     * @return saved stop or null on failure
     */
    LoadStop saveStop(String stopStr, int number,Load ld) {


        Address addr = saveAddress(stopStr);


        LoadStop ls = null;
        try {
            Location loc = saveLocation(stopStr, addr);

            LoadStop loadStop = (LoadStop) getObject(stopStr, LoadStop.class);
            String key;
            if (loadStop != null && !objectHasNullExtId(loadStop))
                key = loadStop.getExtSysId();
            else
                return null;

            LoadStop l2 = (LoadStop) checkEntityExists(loadStopService, key);

            ls = l2 != null ? l2 : loadStop;


            if (number != -1)
                ls.setStopNumber(number);// when not marked a patch.

            ls.setLocationId(loc.getId());
            ls.setLoadId(ld.getId());

            ls.setType(ls.getStopNumber());//for now - until we have grater clarity

            if (!Strings.isNullOrEmpty(ls.getExtSysId()))
                ls = loadStopService.save(ls);

            else
                return null;

        } catch (NullPointerException e) {
            ls = null;
            logger.error(Arrays.toString(e.getStackTrace()));
        }
        return ls;

    }

    private Location saveLocation(String addrStr, Address addr) {

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
            logger.error(Arrays.toString(e.getStackTrace()));
            loc = null;
        }
        return loc;

    }

    private Address saveAddress(String addrStr) {

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
            logger.error(Arrays.toString(e.getStackTrace()));
            address = null;
        }
        return address;

    }


    private Trucker saveTrucker(String truckerStr) {
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
            logger.error(Arrays.toString(e.getStackTrace()));
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

            checkKey = checkKey.replaceAll("\"", "");
            if (typeBridger.isNumeric(checkKey)) {
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
        Optional<Load> ld;
        if (checkKey != null) {
            if (typeBridger.isNumeric(checkKey)) {
                Long id = Long.parseLong(checkKey);
                ld = rs.findById(id);
                if (ld.isPresent()) {
                    return true;

                }
            } else {

                String extid = checkKey;

                if (extid.contains("ey") && extid.contains(".")) {
                    RiggoBaseEntity re = new RiggoBaseEntity();
                    String mpid = re.encode(extid, re.REVERSE);
                    Long id = Long.parseLong(mpid); // Since we are using marketplace id itself

                    if (!rs.findById(id).isPresent())
                        return true;
                }

                try {
                    ld = rs.findByExtSysId(extid);
                    if (ld.isPresent()) {
                        return true;

                    }
                } catch (Exception e) {
                    logger.error(Arrays.toString(e.getStackTrace()));
                }
            }
        }
        return false;
    }


    private ResponseEntity getLoadExistsResposne() {
        return new ResponseEntity<>(new JSONObject().put(message, "Load can not exist when trying to create")
                .toString(),
                HttpStatus.CONFLICT);
    }


}
