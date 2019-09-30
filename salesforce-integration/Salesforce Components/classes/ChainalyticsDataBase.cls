/**
 * Created by Viktor S. Yaremenko on 11/10/2018.
 */

public with sharing class ChainalyticsDataBase {

    public static final String CONFIG_KEY = 'ChainalyticsDataConfig';

    public final static List<String> csvHeaderFields = new List<String>{
            'ship_date', 'orig_city', 'orig_state', 'orig_zip', 'origin_country',
            'dest_city', 'dest_state', 'dest_zip', 'dest_country', 'linehaul_cost', 'fuel_charge', 'acc_cost', 'distance',
            'mode', 'temp_control_type', 'equip_size', 'service_level', 'contract_type', 'route_type', 'num_stopoffs',
            'haz_mat', 'shipment_id', 'weight', 'sc1', 'sc2', 'sc3', 'sc4', 'sc5', 'load_type', 'unload_type', 'equip_size_req',
            'movement_type', 'delivery_date', 'scac', 'total_ship_cost', 'customer', 'origin_id', 'destination_id'
    };

    public static String generateReport(Date startDate, Date endDate) { //return Document.Id
        Time zeroTime = Time.newInstance(0, 0, 0, 0);
        Time lastTime = Time.newInstance(23, 59, 59, 0);
        Datetime rangeEnd = (endDate == null)
                ? Datetime.newInstanceGmt(Date.today(), lastTime).addDays(-1) : Datetime.newInstanceGmt(endDate, lastTime);

        Datetime rangeStart = (startDate == null)
                ? Datetime.newInstanceGmt(Date.today(), zeroTime).addDays(-7) : Datetime.newInstanceGmt(startDate, zeroTime);

        //File Mask riggoh_export_4_chainalytics_from_[YYYY_MM_DD_HH_MM_SS]UTC_TO[YYYY_MM_DD_HH_MM_SS]_UTC.CSV
        String fileName = 'riggoh_export_4_chainalytics_from_['
                + rangeStart.formatGmt('yyyy-MMM-dd-HH-mm-ss')
                + ']UTC_TO['
                + rangeEnd.formatGmt('yyyy-MMM-dd-HH-mm-ss') + ']_UTC.CSV';
        List<Folder> idsToTest = [SELECT Id, Name FROM Folder WHERE DeveloperName = 'ChainalyticsData' LIMIT 1];
        Id folderId;
        if (!idsToTest.isEmpty()) {
            folderId = idsToTest[0].Id;
        }

        List<String> reportData = new List<String>();
        String csvHeader = String.join(csvHeaderFields, ',')
                .replace('sc1', 'Spotlight Category 1')
                .replace('sc2', 'Spotlight Category 2')
                .replace('sc3', 'Spotlight Category 3')
                .replace('sc4', 'Spotlight Category 4')
                .replace('sc5', 'Spotlight Category 5');
        reportData.add(csvHeader);

        Map<Id, rtms__Load__c> cqs = new Map<Id, rtms__Load__c>([
                SELECT Id, (
                        SELECT Id,
                                rtms__Pricing__c
                        FROM rtms__Customer_Quotes1__r
                        WHERE rtms__Status__c = 'Accepted'
                )
                FROM rtms__Load__c
                WHERE
                    rig_Completed_Date__c >= :rangeStart.dateGmt()
                    AND rig_Completed_Date__c <= :rangeEnd.dateGmt()
        ]) ;

        for (rtms__Load__c obj : [
                SELECT Id, rig_Completed_Date__c,
                        rtms__Last_Stop__r.rtms__Expected_Date__c,
                        rtms__Last_Stop__r.rtms__Arrival_Date__c,
/*                        rtms__First_Stop__r.rtms__City__c,
                        rtms__First_Stop__r.rtms__State_Province__c,
                        rtms__First_Stop__r.rtms__Postal_Code__c,
                        rtms__First_Stop__r.rtms__Country__c,
*/
                        rtms__First_Stop__r.rtms__Location2__c,
                        rtms__First_Stop__r.rtms__Location2__r.ShippingCity,
                        rtms__First_Stop__r.rtms__Location2__r.ShippingCountryCode,
                        rtms__First_Stop__r.rtms__Location2__r.ShippingPostalCode,
                        rtms__First_Stop__r.rtms__Location2__r.ShippingStateCode,
/*                        rtms__Last_Stop__r.rtms__City__c,
                        rtms__Last_Stop__r.rtms__State_Province__c,
                        rtms__Last_Stop__r.rtms__Postal_Code__c,
                        rtms__Last_Stop__r.rtms__Country__c,
*/
                        rtms__Last_Stop__r.rtms__Location2__c,
                        rtms__Last_Stop__r.rtms__Location2__r.ShippingCity,
                        rtms__Last_Stop__r.rtms__Location2__r.ShippingCountryCode,
                        rtms__Last_Stop__r.rtms__Location2__r.ShippingPostalCode,
                        rtms__Last_Stop__r.rtms__Location2__r.ShippingStateCode,

                        rtms__Customer__c,
                        rig_Num_of_Stops__c,
                        rtms__Distance_Miles__c,
                        rtms__Equipment_Type__r.rtms__Category__c,
                        rtms__Equipment_Type__r.rig_Equipment_Size__c,
                        rtms__Temperature_Controlled__c,
                        riggoh_Required_Temperature__c,
                        rtms__Service_Level__r.Name,
                        rtms__Hazardous_Materials__c,
                        rtms__Total_Weight__c, (
                        SELECT Id,
                                rtms__Line_Haul__c,
                                rtms__Fuel_Surcharge__c,
                                rtms__Accessorial_Charges__c,
                                rtms__Carrier_Total__c
                        FROM rtms__Carrier_Quotes__r
                        WHERE rtms__Accepted__c = true
                )
                FROM rtms__Load__c
                WHERE
                    rig_Completed_Date__c >= :rangeStart.dateGmt()
                    AND rig_Completed_Date__c <= :rangeEnd.dateGmt()
        ]) {//rig_Completed_Date__c
            LoadWrapper lw = new LoadWrapper(obj);
            lw.addCustomerQuote(cqs.get(obj.Id));
            reportData.add(lw.getAsCSV());
        }

        Document report = new Document();
        report.Name = fileName;
        report.Type = 'csv';
        report.contentType = 'text/csv';
        report.Body = Blob.valueOf(String.join(reportData, '\r\n'));//EncodingUtil.base64Encode(Blob.valueOf(
        if (folderId != null) {
            report.FolderId = folderId;
        }
        insert report;

        return report.Id;
    }

    @future(callout=true)
    public static void sendToOneDriveAsync(Id docId) {
        sendToOneDrive(docId);
    }

    public static void sendToOneDrive(Id docId) {
        if (String.isNotBlank(docId)) {
            List<Document> reports = [SELECT Id, Name, Type, contentType, Body FROM Document WHERE Id = :docId LIMIT 1];
            if (!reports.isEmpty()) {
                Document report = reports[0];
                String oneDriveUrl = 'callout:Chainalytics_OneDrive/' + report.Name + ':/content';
                Http http = new Http();
                httpRequest req = new httpRequest();
                req.setMethod('PUT');

                req.setEndpoint(oneDriveUrl);
                req.setHeader('Content-Type', report.contentType);
                req.setHeader('Accept', 'application/json');
                req.setBodyAsBlob(report.Body);
                httpResponse res = new httpResponse();
                res = http.send(req);

                integer statusCode = res.getStatusCode();
                string resBody = res.getBody();
                if (statusCode > 400) {
                    throw new ChainalyticsDataBase.ChainalyticsDataBaseException('OneDrive failed!\n Status Code:' + statusCode + '\nBody:\n' + resBody);
                }
            }
        }
    }

    public class ChainalyticsDataBaseException extends Exception {
    }

    public class LoadWrapper {

        public rtms__Load__c load {
            get;
            set;
        }

        public Date ship_date {
            get {
                if (ship_date == null) {
                    ship_date = this.load.rtms__Last_Stop__r.rtms__Expected_Date__c;
                }
                return ship_date;
            }
            set;
        }

        public String orig_city {
            get {
                if (orig_city == null) {
//orig_city = load.rtms__First_Stop__r.rtms__City__c;
                    orig_city = load.rtms__First_Stop__r.rtms__Location2__r.ShippingCity;
                }
                return orig_city;
            }
            set;
        }

        public String orig_state {
            get {
                if (orig_state == null) {
//orig_state = load.rtms__First_Stop__r.rtms__State_Province__c;
                    orig_state = load.rtms__First_Stop__r.rtms__Location2__r.ShippingStateCode;

                }
                return orig_state;
            }
            set;
        }

        public String orig_zip {
            get {
                if (orig_zip == null) {
//orig_zip = load.rtms__First_Stop__r.rtms__Postal_Code__c;
                    orig_zip = load.rtms__First_Stop__r.rtms__Location2__r.ShippingPostalCode;
                }
                return orig_zip;
            }
            set;
        }

        public String origin_country {
            get {
                if (origin_country == null) {
//origin_country = load.rtms__First_Stop__r.rtms__Country__c;
                    origin_country = load.rtms__First_Stop__r.rtms__Location2__r.ShippingCountryCode;
                }
                return origin_country;
            }
            set;
        }

        public String dest_city {
            get {
                if (dest_city == null) {
//dest_city = load.rtms__Last_Stop__r.rtms__City__c;
                    dest_city = load.rtms__Last_Stop__r.rtms__Location2__r.ShippingCity;
                }
                return dest_city;
            }
            set;
        }

        public String dest_state {
            get {
                if (dest_state == null) {
//dest_state = load.rtms__Last_Stop__r.rtms__State_Province__c;
                    dest_state = load.rtms__Last_Stop__r.rtms__Location2__r.ShippingStateCode;
                }
                return dest_state;
            }
            set;
        }

        public String dest_zip {
            get {
                if (dest_zip == null) {
//dest_zip = load.rtms__Last_Stop__r.rtms__Postal_Code__c;
                    dest_zip = load.rtms__Last_Stop__r.rtms__Location2__r.ShippingPostalCode;
                }
                return dest_zip;
            }
            set;
        }

        public String dest_country {
            get {
                if (dest_country == null) {
//dest_country = load.rtms__Last_Stop__r.rtms__Country__c;
                    dest_country = load.rtms__Last_Stop__r.rtms__Location2__r.ShippingCountryCode;
                }
                return dest_country;
            }
            set;
        }

        public Decimal linehaul_cost {
            get;
            set;
        }

        public Decimal fuel_charge {
            get;
            set;
        }

        public Decimal acc_cost {
            get;
            set;
        }

        public Decimal distance {
            get {
                if (distance == null) {
                    distance = load.rtms__Distance_Miles__c;
                }
                return distance;
            }
            set;
        }

        public String mode {
            get;
            set;
        }

        public String temp_control_type {
            get {
                if (temp_control_type == null) {
                    temp_control_type = this.load.rtms__Temperature_Controlled__c
                            ? String.valueOf(this.load.riggoh_Required_Temperature__c) : 'N';
                }
                return temp_control_type;
            }
            set;
        }

        public Decimal equip_size {
            get;
            set;
        }

        public String service_level {
            get {
                if (service_level == null) {
                    service_level = 'R'; // this field always empty this.load.rtms__Service_Level__c;

                }
                return service_level;
            }
            set;
        }

        public String contract_type {
            get;
            set;
        }

        public String route_type {
            get {
                if (route_type == null) {
                    route_type = this.num_stopoffs == 0 ? 'DM' : 'MS';
                }
                return route_type;
            }
            set;
        }// stopoffs > 2 = MS 2 = DM

        public Integer num_stopoffs {
            get {
                if (num_stopoffs == null) {
                    num_stopoffs = Integer.valueOf(this.load.rig_Num_of_Stops__c) - 2;
                }
                return num_stopoffs;
            }
            set;
        }// load.Stops.count - 2

        public String haz_mat {
            get {
                if (haz_mat == null) {
                    haz_mat = load.rtms__Hazardous_Materials__c ? 'Y' : 'N';
                }
                return haz_mat;
            }
            set;
        }

        public String shipment_id {
            get {
                if (shipment_id == null) {
                    shipment_id = load.Id;
                }
                return shipment_id;
            }
            set;
        }

        public Decimal weight {
            get {
                if (weight == null) {
                    weight = load.rtms__Total_Weight__c;
                }
                return weight;
            }
            set;
        }

        public String sc1 {
            get;
            set;
        } //Spotlight Category 1,
        public String sc2 {
            get;
            set;
        } //Spotlight Category 2,
        public String sc3 {
            get;
            set;
        } //Spotlight Category 3,
        public String sc4 {
            get;
            set;
        } //Spotlight Category 4,
        public String sc5 {
            get;
            set;
        } //Spotlight Category 5,

        public String load_type {
            get {
                return 'LL';
            }
            set;
        }

        public String unload_type {
            get {
                return 'LL';
            }
            set;
        }

        public String equip_size_req {
            get {
                return 'N';
            }
            set;
        }

        public String movement_type {
            get;
            set;
        }

        public String scac {
            get;
            set;
        }

        public Decimal total_ship_cost {
            get;
            set;
        }

        public Date delivery_date {
            get {
                if (delivery_date == null) {
                    delivery_date = this.load.rtms__Last_Stop__r.rtms__Arrival_Date__c != null ?
                            this.load.rtms__Last_Stop__r.rtms__Arrival_Date__c :
                            this.load.rtms__Last_Stop__r.rtms__Expected_Date__c;
                }
                return delivery_date;
            }
            set;
        }

        public String customer {
            get {
                if (customer == null) {
                    customer = this.load.rtms__Customer__c;
                }
                return customer;
            }
            set;
        }

        public String origin_id {
            get {
                if (origin_id == null) {
                    origin_id = this.load.rtms__First_Stop__r.rtms__Location2__c;
                }
                return origin_id;
            }
            set;
        }

        public String destination_id {
            get {
                if (destination_id == null) {
                    destination_id = this.load.rtms__Last_Stop__r.rtms__Location2__c;
                }
                return destination_id;
            }
            set;
        }

        public LoadWrapper(rtms__Load__c load) {
            this.load = load;
            setCarrierQuoteFields();
            setEquipmentTypyFields();
        }

        private void setCarrierQuoteFields() {
            if (this.load.rtms__Carrier_Quotes__r != null && this.load.rtms__Carrier_Quotes__r.size() > 0) {
                this.linehaul_cost = this.load.rtms__Carrier_Quotes__r[0].rtms__Line_Haul__c;
                this.fuel_charge = this.load.rtms__Carrier_Quotes__r[0].rtms__Fuel_Surcharge__c;
                this.acc_cost = this.load.rtms__Carrier_Quotes__r[0].rtms__Accessorial_Charges__c;
                this.total_ship_cost = this.load.rtms__Carrier_Quotes__r[0].rtms__Carrier_Total__c;
            }
        }

        private void setEquipmentTypyFields() {
            if (this.load.rtms__Equipment_Type__r != null) {
                String cat = this.load.rtms__Equipment_Type__r.rtms__Category__c;
                if (cat == 'Van') {
                    this.mode = 'DV';
                } else if (cat == 'Flatbed') {
                    this.mode = 'FB';
                } else if (cat == 'Reefer') {
                    this.mode = 'TC';
                } else {
                    this.mode = ' IC';
                }
                this.equip_size = this.load.rtms__Equipment_Type__r.rig_Equipment_Size__c;
            }
        }

        public void addCustomerQuote(rtms__Load__c obj) {
//If 'spot' please pass SM if 'contract' please pass CC
            if (obj.rtms__Customer_Quotes1__r != null && obj.rtms__Customer_Quotes1__r.size() > 0) {
                String pricing = obj.rtms__Customer_Quotes1__r[0].rtms__Pricing__c;
                if (String.isNotBlank(pricing)) {
                    this.contract_type = pricing.equalsIgnoreCase('contract') ? 'CC' : 'SM';
                }
            }
        }

        public String getAsCSV() {
            String result = '';
            String s = JSON.serialize(this);
            Map<String, Object> obj = (Map<String, Object>) JSON.deserializeUntyped(s);
            for (String field : csvHeaderFields) {
                if (obj.containsKey(field)) {
                    String o = String.valueOf(obj.get(field));
                    if (o != null) {
                        result += o.escapeCsv() + ',';
                    } else {
                        result += ',';
                    }
                } else {
                    result += ',';
                }
            }
            return result.removeEnd(',');
        }

    }

}