package com.soso.service;

import com.soso.models.CountryPhoneModel;
import com.soso.models.Service;
import com.soso.persistance.CommonDataDAO;
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


    public List<Service> getServicesByParentId(Integer parentId) {
        return commonDataDAO.getServicesByParent(parentId);
    }

    public void deleteSosoService(Integer serviceId) {
        commonDataDAO.deleteService(serviceId);
        ;
    }

    public Integer createSosoService(Service service) {
        return commonDataDAO.createService(service);
    }

    public List<CountryPhoneModel> getAllCountryCodes() {
        return commonDataDAO.getCountryPhoneCodes();
    }

    public String getImgPathOfService(Integer serviceId) {

        return commonDataDAO.getImgPathOfService(serviceId);
    }

    public String getImgPathWithId(Integer serviceId) {

        return commonDataDAO.getImgPathWithId(serviceId);
    }


}
