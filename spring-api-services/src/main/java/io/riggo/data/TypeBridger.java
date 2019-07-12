package io.riggo.data;

import com.google.common.base.Strings;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class is used to provide utility methods
 * Dev Note : For now type conversion more might come up and then we can split it
 */
public class TypeBridger {


    String[] loadStatus = {"Completed", "Assigned", "Cancelled", "Declined", "In Transit", "Loading", "Unloading",
            "Unloaded", "Delivered", "Expired", "Quotes Received", "Quotes Requested", "Tendered", "Unassigned"};


    private static TypeBridger typeBridger;

    /**
     * Ensure single Instance alone synchronized  to ensure thread safety
     *
     * @return a singleton for the bridger
     */
    public static TypeBridger getInstance() {
        if (typeBridger == null)
            typeBridger = new TypeBridger();

        return typeBridger;
    }


    private TypeBridger() {

    }


    public synchronized boolean isNumeric(String strNum) {

        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    public Integer getLoadStatusIdByName(String status) {
        for (int i = 0; i < loadStatus.length; i++) {
            if (status.equals(loadStatus[i])) {
                return i;
            }

        }
        return -1;
    }

    public synchronized BigDecimal getBig(String num) {
        String n = num;
        if (Strings.isNullOrEmpty(num)) {
            n = "0.0";
        }
        BigDecimal d = new BigDecimal(n);
        return d.round(new MathContext(2, RoundingMode.HALF_UP));

    }

    public synchronized Boolean getBool(Object bool) {

        String n = bool;
        if (Strings.isNullOrEmpty(bool)) {
            n = "false";
        }
        return Boolean.valueOf(n);

    }

    public synchronized String cleanQotes(String str) {
        return str.replaceAll("\"", "");

    }

    public synchronized java.sql.Date getDate(String dt) {

        String sDate1 = dt;
        if (Strings.isNullOrEmpty(sDate1))
            sDate1 = "2019-01-01";
        sDate1 = sDate1.replaceAll("\"", "");
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);
            return new java.sql.Date(date1.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new java.sql.Date(new java.util.Date().getTime());
    }
}
