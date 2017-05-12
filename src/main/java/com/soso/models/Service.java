package com.soso.models;

/**
 * Created by Garik Kalashyan on 3/8/2017.
 */
public class Service {
    private Integer id;

    private String serviceName_arm;

    private String serviceName_eng;

    private String imgpath;

    private Integer parentid;

    public Service(){}

    public Service(Integer id, String serviceName_arm, String serviceName_eng,String imgpath,Integer parentid) {
        this.id = id;
        this.serviceName_arm = serviceName_arm;
        this.serviceName_eng = serviceName_eng;
        this.imgpath = imgpath;
        this.parentid = parentid;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
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

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }
}
