/*
* Copyright 2014-2017 Revenova, LLC. All rights reserved.   
*/  
public class QBBuilder2 extends QBBuilderBase{  

    public QBBuilder2() {
        FS_VENDOR_LIST    = 'VendorList';
        FS_CUSTOMER_LIST  = 'CustomerList';
        FS_INVOICE_HEADER = 'InvoiceHeader';   
        FS_INVOICE_LINE   = 'InvoiceLine';
        FS_BILL_HEADER    = 'BillHeader';  
        FS_BILL_LINE      = 'BillLine';
        WRITE_SINGLE_FILE = false; 
    }
    
    public override string bundleTransaction ( Record header, List<Record> lines) {
        // Header is NOT a seperate line.  Tacked on end of each line item
        String headerString = header.getRecordString();
        String invoiceString = '';
        for ( Record rec : lines ) {
            invoiceString += rec.getRecordString() + FIELD_DELIMITER + header.getRecordString() + RECORD_DELIMITER;
        }     
        return invoiceString;  
    }

    public override String constructVendorHeader() {
        return constructHeader(FS_VENDOR_LIST)  + RECORD_DELIMITER;
    }

    public override String constructCustomerHeader() {
        return constructHeader(FS_CUSTOMER_LIST) + RECORD_DELIMITER;
    }

    public override String constructInvoiceHeader() {
        return constructHeader(FS_INVOICE_LINE) + FIELD_DELIMITER + constructHeader(FS_INVOICE_HEADER) + RECORD_DELIMITER;
    }   

     public override String constructBillHeader() {
        return constructHeader(FS_BILL_LINE) + FIELD_DELIMITER + constructHeader(FS_BILL_HEADER) + RECORD_DELIMITER;
    }   






}