/*
*  Copyright 2014-2017 Revenova, LLC. All rights reserved.  
*  IIF Builder 
*/  
public class QBBuilder1 extends QBBuilderBase{  

    public QBBuilder1() {
        FS_VENDOR_LIST    = 'VendorList';
        FS_CUSTOMER_LIST  = 'CustomerList';
        FS_INVOICE_HEADER = 'InvoiceTrans';   
        FS_INVOICE_LINE   = 'InvoiceSplit';
        FS_BILL_HEADER    = 'BillTrans';  
        FS_BILL_LINE      = 'BillSplit';
        WRITE_SINGLE_FILE = true; 
    }
    
    public override record doBalancing(Record rec) {
        rec.InvertAMOUNT();
        rec.InvertQNTY();
        return rec;
    }
  
    public override string bundleTransaction ( Record header, List<Record> lineItems) {
        Boolean ok = isTranactionBalanced(header, lineItems);
        String transactionBundle = '';
        
        // Header is a seperate record, then each line item, then the end trans
        transactionBundle += header.getRecordString() + RECORD_DELIMITER;
        for ( Record rec : lineItems ) {
            transactionBundle += rec.getRecordString() + RECORD_DELIMITER;
        }
        transactionBundle += 'ENDTRNS' + RECORD_DELIMITER;       

        return transactionBundle;    
    }

    public Static boolean isTranactionBalanced( Record header, List<Record> lineItems ) {
        if ( lineItems.size() < 1 ) {
            return false;
        }
        Decimal total = header.getAMOUNT();
        System.debug('Total:  ' + total );
        Decimal transactionTotal = header.getAMOUNT();
        for ( Record rec : lineItems ) {
            Decimal splitAmount = rec.getAMOUNT();       
            System.debug('Total:  ' + total + ' + '  + splitAmount  + ' = '  + ( total + splitAmount ) );
            total += splitAmount;
        }
        if ( total == 0 ) {
            System.debug('*************************************************************************************************OK-BALANCED');
            return true;
        }
        else {
            System.debug('**************************************************************************************************UNBALANCED');
            return false; 
        }
    }

    public override String constructVendorHeader() {
        return '!' + constructHeader(FS_VENDOR_LIST)    + RECORD_DELIMITER;
    }

    public override String constructCustomerHeader() {
        return '!' +  constructHeader(FS_CUSTOMER_LIST) + RECORD_DELIMITER;
    }

    public override String constructInvoiceHeader() {
        String colHeader = '';
        colHeader += '!' +  constructHeader(FS_INVOICE_HEADER) + RECORD_DELIMITER;
        colHeader += '!' +  constructHeader(FS_INVOICE_LINE)   + RECORD_DELIMITER;
        colHeader += '!' +  'ENDTRNS'                          + RECORD_DELIMITER;
        return colHeader;
    }   

     public override String constructBillHeader() {
        String colHeader = '';
        colHeader += '!' +  constructHeader(FS_BILL_HEADER) + RECORD_DELIMITER;
        colHeader += '!' +  constructHeader(FS_BILL_LINE)   + RECORD_DELIMITER;
        colHeader += '!' +  'ENDTRNS'                       + RECORD_DELIMITER;
        return colHeader;
    }   


}