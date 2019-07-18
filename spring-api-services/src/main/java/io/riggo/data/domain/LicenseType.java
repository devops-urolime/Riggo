package io.riggo.data.domain;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("MagicNumber")
public enum LicenseType {
    NOLICENSE(0, "No Resource"),
    SALESFORCE_REVENOVA(1, "Salesforce Revenova"),
    ;

    private final String displayName;
    private final int colVal;


    LicenseType(int colVal, String displayName) {
        this.colVal = colVal;
        this.displayName = displayName;
    }

    public int getColVal() {
        return colVal;
    }

    public String getDisplayName() {
        return displayName;
    }


    private static final Map<Integer, LicenseType> licenseTypeMap;

    public static LicenseType fromColVal(int colVal) {
        return licenseTypeMap.get(colVal);
    }

    static {
        Map<Integer, LicenseType> ltMap = new HashMap<>();
        for (LicenseType licenseType : LicenseType.values()) {
            ltMap.put(licenseType.getColVal(), licenseType);
        }

        licenseTypeMap = ImmutableMap.copyOf(ltMap);
    }
}