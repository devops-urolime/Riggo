package io.riggo.data.helpers;

import com.fasterxml.jackson.annotation.JsonAlias;


/**
 * Maps incoming data form SF
 */
public class AddressDTOLoad {


    @JsonAlias({"CustomerId", "LastStopId", "FirstStopId", "billToId", "CarrierRemitToId"})
    String extSysId;
    @JsonAlias({"CustomerName", "BillToName", "CarrierRemitToName"})
    String name;
    @JsonAlias({"CustomerTMSType", "billToTMSType", "CarrierRemitToTMSType"})
    String type;
    @JsonAlias({"CustomerShippingStreet", "billToShippingStreet", "CarrierRemitToShippingStreet"})
    String street;
    @JsonAlias({"CustomerShippingCity", "FirstStoprtms__City__c", "LastStoprtms__City__c", "billToShippingCity", "CarrierRemitToShippingCity"})
    String city;
    @JsonAlias({"CustomerShippingPostalCode", "billToShippingPostalCode", "CarrierRemitToShippingPostalCode"})
    Integer postalCode;
    @JsonAlias({"CustomerShippingState", "FirstStoprtms__Postal_Code__c", "FirstStoprtms__State_Province__c", "LastStoprtms__State_Province__c", "billToShippingState"})
    String state;

    @JsonAlias({"CustomerShippingCountry", "FirstStoprtms__Country__c", "LastStoprtms__Country__c", "CarrierRemitToShippingCountry"})
    String country;


    public AddressDTOLoad() {

    }


    public AddressDTOLoad(String extSysId, String name, String type, String street,
                          String city, Integer postalCode, String country) {
        this.extSysId = extSysId;
        this.name = name;
        this.type = type;
        this.street = street;
        this.postalCode = postalCode;
        this.country = country;
        this.city = city;

    }

    public String getExtSysId() {
        return extSysId;
    }

    public void setExtSysId(String extSysId) {
        this.extSysId = extSysId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
