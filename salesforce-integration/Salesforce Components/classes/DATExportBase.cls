/**
 * Created by Viktor S. Yaremenko on 3/30/2019.
 */

public with sharing class DATExportBase {

    public static final String CONFIG_KEY = 'DATExportConfig';

    public final static List<String> csvHeaderFields = new List<String>{
            'Load ID / Pro Number', 'Actual Pick-up Date/Time', 'Actual Delivery Date/Time', 'Origin City',
            'Origin State', 'Origin Zip', 'Destination City', 'Destination State', 'Destination Zip',
            'Total Amount Paid by Broker', 'Total Amount Paid by Customer', 'Equipment Type (brief)',
            'Equipment Detail (full)', 'Mode', 'Practical Miles', 'Pieces or Pallets', 'Weight', 'Freight Class',
            'Cubic Feet', 'Linear Feet', 'Total Number of Pickups', 'Total Number of Drops', 'Linehaul Amount Paid',
            'Linehaul Amount Paid by Customer', 'Fuel Surcharge Amount Paid', 'Fuel Surcharge Amount Paid by Customer',
            'Load/Unload Amount Paid', 'Load/Unload Amount Paid by Customer', 'Stop Charges Paid',
            'Stop Charges Paid by Customer', 'Other Accessorial Amount Paid', 'Other Accessorial Amount Paid by Customer',
            'Name of Accessorial', 'Price Set on Spot Market Basis', 'Freight Description / Commodity', 'Service Level',
            'Delivery Appointment Mandatory', 'First Delivery Appointment Time', 'Customer Name or Code',
            'Consignee Name or Code', 'Freight Value', 'Cargo Insurance', 'Shipper Name or Code', 'Carrier Name or Code',
            'Truck Finder', 'Load Planner', 'SCAC Code', 'Location Name', 'Pick Up Address', 'Delivery  Address',
            'Pick Up Appointment Required', 'Pick Up Appointment Start Time', 'Pick Up Appointment End Time',
            'Order Book Date / Time', 'Shipment Exception Code', 'Carrier Type', 'Payment Terms', 'Pay Date',
            'Final Delivery Appointment Required', 'Delivery Appointment Start Time', 'Delivery Appointment End Time',
            'Hazmat', 'Units Type', 'Office Number'
    };
////////////////////////////////////////////////////////////////////////
    public final static List<String> csvHeaderMethods = new List<String>{
            'loadId', 'pickup_date', 'delivery_date', 'orig_city',
            'orig_state', 'orig_zip', 'dest_city', 'dest_state', 'dest_zip',
            'total_amount_broker', 'total_amount_customer', 'equipment_type',
            'equipment_detail', 'mode', 'distance', 'pices_palets', 'weight', 'freight_class',
            'cubic_feet', 'linear_feet', 'total_pickups', 'total_drops', 'linear_amount_paid',
            'linear_amount_paid_customer', 'fuel_amount_paid', 'fuel_amount_paid_customer',
            'load_unload_amount_paid', 'load_unload_amount_paid_customer', 'stop_charges_paid',
            'stop_charges_paid_customer', 'accessorial_amount_paid', 'accessorial_amount_paid_customer',
            'accessorial_name', 'market_basis', 'freight_description', 'service_level',
            'delivery_appointment_mandatory', 'first_delivery_appointment_time', 'customer_name',
            'consignee_name', 'freight_value', 'cargo_insurance', 'shipper_name', 'carrier_name',
            'truck_finder', 'load_planner', 'scac_code', 'location_name', 'pick_up_address', 'delivery_address',
            'pick_up_appointment_required', 'pick_up_appointment_start_time', 'pick_up_appointment_end_time',
            'order_book_datetime', 'shipment_exception_code', 'carrier_type', 'payment_terms', 'pay_date',
            'final_delivery_appointment_required', 'delivery_appointment_start_time', 'delivery_appointment_end_time',
            'haz_mat', 'units_type', 'office_number'
    };

    /******************** ******************* *********************** */
    public static String generateReport(Date startDate, Date endDate) { //return Document.Id
        Time zeroTime = Time.newInstance(0, 0, 0, 0);
        Time lastTime = Time.newInstance(23, 59, 59, 0);
        Datetime rangeEnd = (endDate == null)
                ? Datetime.newInstanceGmt(Date.today(), lastTime).addDays(-1) : Datetime.newInstanceGmt(endDate, lastTime);

        Datetime rangeStart = (startDate == null)
                ? Datetime.newInstanceGmt(Date.today(), zeroTime).addDays(-7) : Datetime.newInstanceGmt(startDate, zeroTime);

        //File Mask riggoh_export_4_dat_from_[YYYY_MM_DD_HH_MM_SS]UTC_TO[YYYY_MM_DD_HH_MM_SS]_UTC.CSV
/*        String fileName = 'riggoh_export_4_dat_from_['
                + rangeStart.formatGmt('yyyy-MMM-dd-HH-mm-ss')
                + ']UTC_TO['
                + rangeEnd.formatGmt('yyyy-MMM-dd-HH-mm-ss') + ']_UTC.CSV';*/
        String fileName = 'rates-233137-362295.csv';
        List<Folder> idsToTest = [SELECT Id, Name FROM Folder WHERE DeveloperName = 'DATExportData' LIMIT 1];
        Id folderId;
        if (!idsToTest.isEmpty()) {
            folderId = idsToTest[0].Id;
        }

        List<String> reportData = new List<String>();
        reportData.add('233137-362295,RIGGO,elizabeth@riggo.io,Broker');
        String csvHeader = String.join(csvHeaderFields, ',');
        reportData.add(csvHeader);

        Map<Id, rtms__Load__c> cqs = new Map<Id, rtms__Load__c>([
                SELECT Id, (
                        SELECT Id,
                                rtms__Pricing__c,
                                rtms__Fuel_Surcharge__c,
                                rtms__Net_Line_Haul__c
                        FROM rtms__Customer_Quotes1__r
                        WHERE rtms__Status__c = 'Accepted'
                )
                FROM rtms__Load__c
                WHERE
                rig_Completed_Date__c >= :rangeStart.dateGmt()
                AND rig_Completed_Date__c <= :rangeEnd.dateGmt()
        ]) ;

        Map<Id, rtms__Load__c> loadStops = new Map<Id, rtms__Load__c>([
                SELECT Id, (
                        SELECT Id,
                                rtms__Is_Dropoff__c,
                                rtms__Is_Pickup__c
                        FROM rtms__Stops__r
                )
                FROM rtms__Load__c
                WHERE
                rig_Completed_Date__c >= :rangeStart.dateGmt()
                AND rig_Completed_Date__c <= :rangeEnd.dateGmt()
        ]) ;

        Map<Id, rtms__Load__c> lineItems = new Map<Id, rtms__Load__c>([
                SELECT Id, (
                        SELECT Id,
                                rtms__NMFC_Class__c
                        FROM rtms__Line_Items__r
                )
                FROM rtms__Load__c
                WHERE
                rig_Completed_Date__c >= :rangeStart.dateGmt()
                AND rig_Completed_Date__c <= :rangeEnd.dateGmt()
        ]) ;

        for (rtms__Load__c obj : [
                SELECT Id,
                        Name,
                        rtms__First_Stop__r.rtms__Expected_Date__c,
                        rtms__Last_Stop__r.rtms__Expected_Date__c,
                        rtms__First_Stop__r.rtms__Location2__r.ShippingCity,
                        rtms__First_Stop__r.rtms__Location2__r.ShippingStateCode,
                        rtms__First_Stop__r.rtms__Location2__r.ShippingPostalCode,
                        rtms__First_Stop__r.rtms__Appointment_Required__c,
                        rtms__First_Stop__r.rtms__Appointment_Time__c,
                        rtms__Last_Stop__r.rtms__Location2__r.ShippingCity,
                        rtms__Last_Stop__r.rtms__Location2__r.ShippingPostalCode,
                        rtms__Last_Stop__r.rtms__Location2__r.ShippingStateCode,
                        rtms__Last_Stop__r.rtms__Appointment_Required__c,
                        rtms__Carrier_Quote_Total__c,
                        rtms__Equipment_Type__r.rtms__Category__c,
                        rtms__Equipment_Type__r.Name,
                        rtms__Mode__r.Name,
                        rtms__Mode__r.rtms__LTL__c,
                        rtms__Distance_Miles__c,
                        rtms__Total_Weight__c,
                        rtms__Linear_Feet__c,
                        rtms__Volume_Units__c,
                        rtms__Service_Level__c,
                        rtms__Customer__c,
                        rtms__Insurance_Amount__c,
                        rtms__Carrier__c,
                        rtms__Carrier__r.Name,
                        rtms__Order_Date__c,
                        rtms__Payment_Terms__c,
                        rtms__Hazardous_Materials__c,
                        rtms__Customer_Quote_Total__c, (
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
                ORDER BY Name
        ]) {
            LoadWrapper lw = new LoadWrapper(obj);
            lw.addCustomerQuote(cqs.get(obj.Id));
            lw.addLineItems(lineItems.get(obj.Id));
            lw.addLoadStops(loadStops.get(obj.Id));
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
    public static void sendToFtpAsync(Id docId, String folderId) {
        sendToFtp(docId, folderId);
    }

    public static void sendToFtp(Id docId, String folderId) {
        if (String.isNotBlank(docId)) {
            List<Document> reports = [SELECT Id, Name, Type, contentType, Body FROM Document WHERE Id = :docId LIMIT 1];
            if (!reports.isEmpty()) {
                Document report = reports[0];
                String oneDriveUrl = 'callout:DAT_GoogleDrive/?uploadType=multipart';// + report.Name media
                String boundary = '----------riggo' + DocId;
                Map<String,Object> reqJSON = new Map<String, Object>{
                        'name' => report.Name,
                        'parents' => new List<String>{folderId},
                        'originalFilename' => report.Name,
                        'mimeType' => report.contentType
                };

                List<String> reqBody = new List<String>();
                reqBody.add('--' + boundary);
                reqBody.add('Content-Type: application/json');
                reqBody.add('');
                reqBody.add(JSON.serialize(reqJSON));
                reqBody.add('--' + boundary);
                reqBody.add('Content-Type: ' + report.contentType);
                reqBody.add('Content-Transfer-Encoding: base64');
                reqBody.add('');
                reqBody.add(EncodingUtil.base64Encode(report.Body));
                reqBody.add('--' + boundary + '--');

                String body = String.join(reqBody, '\r\n');

                Http http = new Http();
                httpRequest req = new httpRequest();
                req.setMethod('POST');

                req.setEndpoint(oneDriveUrl);
                req.setHeader('Content-Type', 'multipart/mixed; boundary="'+boundary+'"');
                req.setHeader('Content-length', String.valueOf(body.length()));
                req.setBody(body);

                req.setHeader('Accept', 'application/json');
                httpResponse res = new httpResponse();
                res = http.send(req);

                integer statusCode = res.getStatusCode();
                string resBody = res.getBody();
                if (statusCode > 400) {
                    throw new DATExportBase.DATExportBaseException('Google Drive failed!\n Status Code:' + statusCode + '\nBody:\n' + resBody);
                }
            }

        }
    }


    public class DATExportBaseException extends Exception {
    }

    public class LoadWrapper {

        public rtms__Load__c load {
            get;
            set;
        }

        //'Load ID / Pro Number' = Id
        public String loadId {
            get {
                if (loadId == null) {
                    loadId = this.load.Name;
                }
                return loadId;
            }
            set;
        }

        //'Actual Pick-up Date/Time' = rtms__First_Stop__r.rtms__Expected_Date__c
        public Date pickup_date {
            get {
                if (pickup_date == null) {
                    pickup_date = this.load.rtms__First_Stop__r.rtms__Expected_Date__c;
                }
                return pickup_date;
            }
            set;
        }

        //'Actual Delivery Date/Time' = rtms__Last_Stop__r.rtms__Expected_Date__c
        public Date delivery_date {
            get {
                if (delivery_date == null) {
                    delivery_date = this.load.rtms__Last_Stop__r.rtms__Expected_Date__c;
                }
                return delivery_date;
            }
            set;
        }

        //'Origin City' = rtms__First_Stop__r.rtms__Location2__r.ShippingCity
        //'Origin State' = rtms__First_Stop__r.rtms__Location2__r.ShippingStateCode
        //'Origin Zip' = rtms__First_Stop__r.rtms__Location2__r.ShippingPostalCode
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

        //'Destination City' = rtms__Last_Stop__r.rtms__Location2__r.ShippingCity
        //'Destination State' = rtms__Last_Stop__r.rtms__Location2__r.ShippingStateCode
        //'Destination Zip' = rtms__Last_Stop__r.rtms__Location2__r.ShippingPostalCode
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

        //'Total Amount Paid by Broker' = rtms__Carrier_Quote_Total__c
        public Decimal total_amount_broker {
            get {
                if (total_amount_broker == null) {
                    total_amount_broker = load.rtms__Carrier_Quote_Total__c;
                }
                return total_amount_broker;
            }
            set;
        }

        //'Total Amount Paid by Customer' = rtms__Customer_Quote_Total__c
        public Decimal total_amount_customer {
            get {
                if (total_amount_customer == null) {
                    total_amount_customer = this.load.rtms__Customer_Quote_Total__c;
                }
                return total_amount_customer;
            }
            set;
        }

        //'Equipment Type (brief)' = rtms__Equipment_Type__r.rtms__Category__c
        public String equipment_type {
            get {
                if (equipment_type == null) {
                    equipment_type = load.rtms__Equipment_Type__r.rtms__Category__c;
                }
                return equipment_type;
            }
            set;
        }

        //'Equipment Detail (full)' = rtms__Equipment_Type__r.Name
        public String equipment_detail {
            get {
                if (equipment_detail == null) {
                    equipment_detail = load.rtms__Equipment_Type__r.Name;
                }
                return equipment_detail;
            }
            set;
        }

        /*'Mode' rtms__Mode__r.Name rtms__Mode__r.rtms__LTL__c - checkbox
        Load Mode Name and LTL - TL/LTL problem is that Name is text field - we use Name only*/
        public String mode {
            get {
                if (mode == null) {
                    mode = this.load.rtms__Mode__r.Name;
                }
                return mode;
            }
            set;
        }

        //'Practical Miles' = rtms__Distance_Miles__c
        public Decimal distance {
            get {
                if (distance == null) {
                    distance = load.rtms__Distance_Miles__c;
                }
                return distance;
            }
            set;
        }

        //'Pieces or Pallets' ignore
        public String pices_palets {
            get;
            set;
        }


        //'Weight' = rtms__Total_Weight__c LTL Required
        public Decimal weight {
            get {
                if (weight == null) {
                    weight = load.rtms__Total_Weight__c;
                }
                return weight;
            }
            set;
        }

        //'Freight Class' = rtms__Line_Items__r.rtms__NMFC_Class__c  fill in addLineItems
        public String freight_class {
            get;
            set;
        }

        //'Cubic Feet' = ignore
        public String cubic_feet {
            get;
            set;
        }

        //'Linear Feet' = rtms__Linear_Feet__c
        public Decimal linear_feet {
            get {
                if (linear_feet == null) {
                    linear_feet = load.rtms__Linear_Feet__c;
                }
                return linear_feet;
            }
            set;
        }

        //'Total Number of Pickups' fill in addLoadStops
        public Integer total_pickups {
            get;
            set;
        }

        //'Total Number of Drops' fill in addLoadStops
        public Integer total_drops {
            get;
            set;
        }

        //'Linehaul Amount Paid' = rtms__Carrier_Quotes__r.rtms__Line_Haul__c - fill in constructor
        public Decimal linear_amount_paid {
            get;
            set;
        }

        //'Linehaul Amount Paid by Customer' rtms__CustomerQuote__r.rtms__Net_Line_Haul__c - fill in addCustomerQuote
        public Decimal linear_amount_paid_customer {
            get;
            set;
        }


        //'Fuel Surcharge Amount Paid' = rtms__Carrier_Quotes__r.rtms__Fuel_Surcharge__c - fill in constructor
        public Decimal fuel_amount_paid {
            get;
            set;
        }

        //'Fuel Surcharge Amount Paid by Customer' = rtms__CustomerQuote__r.rtms__Fuel_Surcharge__c - fill in addCustomerQuote
        public Decimal fuel_amount_paid_customer {
            get;
            set;
        }

        //'Load/Unload Amount Paid' ignore
        public String load_unload_amount_paid {
            get;
            set;
        }

        //'Load/Unload Amount Paid by Customer' ignore
        public String load_unload_amount_paid_customer {
            get;
            set;
        }

        //'Stop Charges Paid' ignore
        public String stop_charges_paid {
            get;
            set;
        }

        //'Stop Charges Paid by Customer' ignore
        public String stop_charges_paid_customer {
            get;
            set;
        }

        //'Other Accessorial Amount Paid' = rtms__Carrier_Quotes__r.rtms__Accessorial_Charges__c fill in constructor
        public Decimal accessorial_amount_paid {
            get;
            set;
        }

        //'Other Accessorial Amount Paid by Customer' = rtms__CustomerQuote__r.rtms__Accessorial_Charges__c - fill in addCustomerQuote
        public String accessorial_amount_paid_customer {
            get;
            set;
        }

        //'Name of Accessorial' ignore
        public String accessorial_name {
            get;
            set;
        }

        //'Price Set on Spot Market Basis' = rtms__Customer_Quotes__r.rtms__Pricing__c - - fill in constructor
        public String market_basis {
            get;
            set;
        }


        //'Freight Description / Commodity' ignore
        public String freight_description {
            get;
            set;
        }

        //'Service Level' = rtms__Service_Level__c
        public String service_level {
            get {
                if (service_level == null) {
                    service_level = this.load.rtms__Service_Level__c;
                }
                return service_level;
            }
            set;
        }

        //'Delivery Appointment Mandatory' = rtms__First_Stop__r.rtms__Appointment_Required__c
        public String delivery_appointment_mandatory {
            get {
                if (delivery_appointment_mandatory == null) {
                    delivery_appointment_mandatory = this.load.rtms__First_Stop__r.rtms__Appointment_Required__c ?
                            'Yes' : 'No';
                }
                return delivery_appointment_mandatory;
            }
            set;
        }

        //'First Delivery Appointment Time' = rtms__First_Stop__r.rtms__Appointment_Time__c
        //Format may be 15:30 - 16:30 without date
        public String first_delivery_appointment_time {
            get {
                if (first_delivery_appointment_time == null) {
                    first_delivery_appointment_time = this.load.rtms__First_Stop__r.rtms__Appointment_Time__c;
                }
                return first_delivery_appointment_time;
            }
            set;
        }

        //'Customer Name or Code' = rtms__Customer__c
        public String customer_name {
            get {
                if (customer_name == null) {
                    customer_name = this.load.rtms__Customer__c;
                }
                return customer_name;
            }
            set;
        }

        //'Consignee Name or Code' ignore
        public String consignee_name {
            get;
            set;
        }

        //'Freight Value' ignore
        public String freight_value {
            get;
            set;
        }

        //'Cargo Insurance' = rtms__Insurance_Amount__c
        public Decimal cargo_insurance {
            get {
                if (cargo_insurance == null) {
                    cargo_insurance = this.load.rtms__Insurance_Amount__c;
                }
                return cargo_insurance;
            }
            set;
        }

        //'Shipper Name or Code' ignore
        public String shipper_name {
            get;
            set;
        }

        //'Carrier Name or Code' = rtms__Carrier__c
        public String carrier_name {
            get {
                if (carrier_name == null) {
                    carrier_name = this.load.rtms__Carrier__r.Name;
                }
                return carrier_name;
            }
            set;
        }

        //'Truck Finder' ignore
        public String truck_finder {
            get;
            set;
        }

        //'Load Planner' ignore
        public String load_planner {
            get;
            set;
        }

        //'SCAC Code' - optional, was not used in chaina
        public String scac_code {
            get;
            set;
        }

        //'Location Name' ignore
        public String location_name {
            get;
            set;
        }

        //'Pick Up Address' ignore
        public String pick_up_address {
            get;
            set;
        }

        //'Delivery Address' ignore
        public String delivery_address {
            get;
            set;
        }

        //'Pick Up Appointment Required' ignore
        public String pick_up_appointment_required {
            get;
            set;
        }

        //'Pick Up Appointment Start Time' ignore
        public String pick_up_appointment_start_time {
            get;
            set;
        }

        //'Pick Up Appointment End Time' ignore
        public String pick_up_appointment_end_time {
            get;
            set;
        }

        //'Order Book Date / Time' = rtms__Order_Date__c
        public Date order_book_datetime {
            get {
                if (order_book_datetime == null) {
                    order_book_datetime = this.load.rtms__Order_Date__c;
                }
                return order_book_datetime;
            }
            set;
        }

        //'Shipment Exception Code' ignore
        public String shipment_exception_code {
            get;
            set;
        }

        //'Carrier Type' ignore
        public String carrier_type {
            get;
            set;
        }

        //'Payment Terms' = rtms__Payment_Terms__c
        public String payment_terms {
            get {
                if (payment_terms == null) {
                    payment_terms = this.load.rtms__Payment_Terms__c;
                }
                return payment_terms;
            }
            set;
        }

        //'Pay Date' ignore
        public String pay_date {
            get;
            set;
        }

        //'Final Delivery Appointment Required' = rtms__Last_Stop__r.rtms__Appointment_Required__c
        public String final_delivery_appointment_required {
            get {
                if (final_delivery_appointment_required == null) {
                    final_delivery_appointment_required = this.load.rtms__Last_Stop__r.rtms__Appointment_Required__c ?
                            'Yes' : 'No';
                }
                return final_delivery_appointment_required;
            }
            set;
        }

        //'Delivery Appointment Start Time' ignore
        public String delivery_appointment_start_time {
            get;
            set;
        }

        //'Delivery Appointment End Time' ignore
        public String delivery_appointment_end_time {
            get;
            set;
        }

        //'Hazmat' = rtms__Hazardous_Materials__c
        public String haz_mat {
            get {
                if (haz_mat == null) {
                    haz_mat = load.rtms__Hazardous_Materials__c ? 'Yes' : 'No';
                }
                return haz_mat;
            }
            set;
        }

        //'Units Type' = rtms__Volume_Units__c
        public String units_type {
            get {
                if (units_type == null) {
                    units_type = load.rtms__Volume_Units__c;
                }
                return units_type;
            }
            set;
        }

        //'Office Number' ignore
        public String office_number {
            get;
            set;
        }

/////////////////////////////////////////////////

        public LoadWrapper(rtms__Load__c load) {
            this.load = load;
            setCarrierQuoteFields();
        }

        private void setCarrierQuoteFields() {
            if (this.load.rtms__Carrier_Quotes__r != null && this.load.rtms__Carrier_Quotes__r.size() > 0) {
                this.linear_amount_paid = this.load.rtms__Carrier_Quotes__r[0].rtms__Line_Haul__c;
                this.fuel_amount_paid = this.load.rtms__Carrier_Quotes__r[0].rtms__Fuel_Surcharge__c;
                this.accessorial_amount_paid = this.load.rtms__Carrier_Quotes__r[0].rtms__Accessorial_Charges__c;
            }
        }

        public void addCustomerQuote(rtms__Load__c obj) {
// Flag to show whether this load is a spot-market freight movement as opposed to a recurring contract load. TRUE = Spot Market; FALSE = Contract Freight
            if (obj.rtms__Customer_Quotes1__r != null && obj.rtms__Customer_Quotes1__r.size() > 0) {
                String pricing = obj.rtms__Customer_Quotes1__r[0].rtms__Pricing__c;
                if (String.isNotBlank(pricing)) {
                    this.market_basis = pricing.equalsIgnoreCase('contract') ? 'Yes' : 'No';
                }
                this.fuel_amount_paid_customer = obj.rtms__Customer_Quotes1__r[0].rtms__Fuel_Surcharge__c;
                this.linear_amount_paid_customer = obj.rtms__Customer_Quotes1__r[0].rtms__Net_Line_Haul__c;
            }
        }

        public void addLineItems(rtms__Load__c obj) {
            if (obj.rtms__Line_Items__r != null && obj.rtms__Line_Items__r.size() > 0) {
                this.freight_class = obj.rtms__Line_Items__r[0].rtms__NMFC_Class__c;
            }
        }

        public void addLoadStops(rtms__Load__c obj) {
            this.total_pickups = 0;
            this.total_drops = 0;
            for (rtms__Stop__c s : obj.rtms__Stops__r) {
                if (s.rtms__Is_Dropoff__c) {
                    this.total_drops += 1;
                }
                if (s.rtms__Is_Pickup__c) {
                    this.total_pickups += 1;
                }
            }
        }

        public String getAsCSV() {
            String result = '';
            String s = JSON.serialize(this);
            Map<String, Object> obj = (Map<String, Object>) JSON.deserializeUntyped(s);
            for (String field : csvHeaderMethods) {
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