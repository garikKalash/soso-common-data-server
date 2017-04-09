package com.soso.models;

/**
 * Created by Garik Kalashyan on 3/8/2017.
 */
public class CountryPhoneModel {
    private Integer id;
    private String iso;
    private String nicename;
    private String iso3;
    private String name;
    private String numcode;
    private String phonecode;

    public CountryPhoneModel(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getNicename() {
        return nicename;
    }

    public void setNicename(String nicename) {
        this.nicename = nicename;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumcode() {
        return numcode;
    }

    public void setNumcode(String numcode) {
        this.numcode = numcode;
    }

    public String getPhonecode() {
        return phonecode;
    }

    public void setPhonecode(String phonecode) {
        this.phonecode = phonecode;
    }

    public CountryPhoneModel(Integer id, String iso, String nicename, String iso3, String name, String numcode, String phonecode) {
        this.id = id;
        this.iso = iso;
        this.nicename = nicename;
        this.iso3 = iso3;
        this.name = name;
        this.numcode = numcode;
        this.phonecode = phonecode;
    }
}
