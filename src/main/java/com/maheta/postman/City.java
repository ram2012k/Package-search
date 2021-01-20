package com.maheta.postman;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TODO: Document this class.
 */
@Entity
@Table(name = "CITY")
public class City {
    @Id
//defining id as column name  
    @Column(name = "City_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cityCode;

    @Column(name = "city_name")
    private String cityName;
    
    @Column(name = "city_pincode")
    private int cityPincode;

    public int getCityCode() {
        return this.cityCode;
    }

    public void setCityCode(final int cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return this.cityName;
    }

    public void setCityName(final String cityName) {
        this.cityName = cityName;
    }

    public int getCityPincode() {
        return this.cityPincode;
    }

    public void setCityPincode(final int cityPincode) {
        this.cityPincode = cityPincode;
    }
}
