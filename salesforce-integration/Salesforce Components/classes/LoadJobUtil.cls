/*
* Copyright 2014-2017 Revenova, LLC. All rights reserved.    
*/
public with sharing class LoadJobUtil {

    public static SObject getCompleteObject(Schema.SObjectType sot, Id theId) {    
        String tableName = sot.getDescribe().getName();
        String query = ' select ' + String.join(getColumnNamesForTable(sot), ',') + ' from ' + tableName + ' where Id = ' + quote(theId);                       
        return Database.query(query);            
    }

    public static List<String> getColumnNamesForTable(Schema.SObjectType sot) {    
        List<String> cols = new List<String>();
        for(Schema.SObjectField sof : sot.getDescribe().fields.getMap().values()) {
            cols.add(sof.getDescribe().getName());
        }
        return cols;
    }   




    public static String quote(String str) {
        return str == null ? str : '\'' + str + '\'';
    }

    private static String q(String s) {
        return quote(s);
    }

    public static Boolean isValidEmail(String s) {
        Boolean returnValue = false;
        if ( s != null ) {
            s = s.trim();
            Pattern p = Pattern.compile( '([a-zA-Z0-9_\\-\\.]+)@(((\\[a-z]{1,3}\\.[a-z]{1,3}\\.[a-z]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3}))');
            Matcher m = p.matcher(s);
            if  (m.matches() ) {
                returnValue = true;
            }
        }   
        return returnValue;
    }

 
    public static Boolean isDecimal(String s){
        Boolean returnValue = false;
        if ( s != null ) {
            s = s.trim();
            s = s.remove('.');
            s = s.remove('-');
            s = s.remove('+');
            returnValue = s.isNumeric();
        }
        return returnValue;             
    }


    public static DateTime toDT(Date d) {
        if (d != null ) {
            return datetime.newInstance(d.year(),d.month(),d.day());
        }
        else {
            return null;
        }       
    }


    public static string getFirstName(String s) {
        // really basic.  if simple two part name, split on space then return first part, otherwise bail and send back the whole thing
        if ( s != null ) {
            s = s.trim();
            List<String> nameList = s.split(' ');
            if (nameList.size() == 2) {
                return nameList[0];
            }
        }           
        return s;
    }
     
    public static string getLastName(String s) {
        // as above only for last name
        if ( s != null ) {
            s = s.trim();
            List<String> nameList = s.split(' ');
            if (nameList.size() == 2) {
                return nameList[1];
            }
        }           
        return s;       
    }


    public static string formatHours(String s) {
    // input hours example 0800-1700  ----   want 08:30-16:30
        if ( s == null ) {
            return s;
        }
        s = s.trim();
        List<String> hourParts = s.split('-');
        if ( hourParts.size() != 2 ) {
            return '';
        }
        if ( hourParts[0].Length() != 4  ||  hourParts[1].Length() != 4 ) {
            return '';
        }
        if ( !hourParts[0].isNumeric() ||  !hourParts[1].isNumeric() ) {
            return '';
        }
        
        String hh1 = hourParts[0].left(2);
        String mm1 = hourParts[0].right(2);
        String hh2 = hourParts[1].left(2);
        String mm2 = hourParts[1].right(2);     
        
        if ( hh1 > '24'  ||  hh2 > '24' ||  mm1 > '60'  ||  mm2 > '60' ){       // range check
            return '';
        }   
        
        if ( hh1 == '24'  &&  mm1 != '00' ) {        // don't go beyond 24 hours
            return '';
        }
        if ( hh2 == '24'  &&  mm2 != '00' ) {
            return '';
        }

        
        if ( hh1 + mm1   >=  hh2 + mm2  ){   // Start has to be greater than end
            return '';
        }
        
        return hh1 + ':' + mm1 + '-' + hh2 + ':' + mm2; 
            
        
    }
    
}