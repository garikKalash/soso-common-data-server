package com.soso.service;

import com.soso.dto.CommonDataDAO;
import com.soso.models.CountryPhoneModel;
import com.soso.models.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Garik Kalashyan on 3/8/2017.
 */
@Repository
public class CommonDataService {

    @Autowired
    private CommonDataDAO commonDataDAO;


    public List<Service> getAllSosoServices(){
        return commonDataDAO.getAllServices();
    }

    public void deleteSosoService(Integer serviceId){
        commonDataDAO.deleteService(serviceId); ;
    }

    public Integer createSosoService(Service service){
        return commonDataDAO.createService(service);
    }

    public List<CountryPhoneModel> getAllCountryCodes(){
        return commonDataDAO.getCountryPhoneCodes();
    }




}
