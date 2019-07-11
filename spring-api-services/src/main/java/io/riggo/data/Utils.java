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
public class Utils {

    public static Utils utils;

    public static Utils getInstance() {
        if (utils == null)
            utils = new Utils();

        return utils;
    }


    private Utils() {

    }


    public static boolean isNumeric(String strNum) {

        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    public static BigDecimal getBig(String num) {
        String n = num;
        if (Strings.isNullOrEmpty(num)) {
            n = "0.0";
        }
        BigDecimal d = new BigDecimal(n);
        return d.round(new MathContext(2, RoundingMode.HALF_UP));

    }

    public static Boolean getBool(String bool) {
        String n = bool;
        if (Strings.isNullOrEmpty(bool)) {
            n = "false";
        }
        Boolean val = new Boolean(n);
        return val;
    }


    public static java.sql.Date getDate(String dt) {

        String sDate1 = dt;
        if (Strings.isNullOrEmpty(sDate1))
            sDate1 = "2019-01-01";
        sDate1 = sDate1.replaceAll("\"", "");
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);
            java.sql.Date sqlDate = new java.sql.Date(date1.getTime());
            return sqlDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new java.sql.Date(new java.util.Date().getTime());
    }
}
