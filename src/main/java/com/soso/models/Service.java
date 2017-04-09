package com.soso.models;

/**
 * Created by Garik Kalashyan on 3/8/2017.
 */
public class Service {
    private Integer id;

    private String serviceName_arm;

    private String serviceName_eng;


    public Service(){}

    public Service(Integer id, String serviceName_arm, String serviceName_eng) {
        this.id = id;
        this.serviceName_arm = serviceName_arm;
        this.serviceName_eng = serviceName_eng;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServiceName_arm() {
        return serviceName_arm;
    }

    public void setServiceName_arm(String serviceName_arm) {
        this.serviceName_arm = serviceName_arm;
    }

    public String getServiceName_eng() {
        return serviceName_eng;
    }

    public void setServiceName_eng(String serviceName_eng) {
        this.serviceName_eng = serviceName_eng;
    }
}
