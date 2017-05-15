package com.soso.service;

import com.soso.models.CountryPhoneModel;
import com.soso.models.Service;
import com.soso.persistance.CommonDataDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.List;

/**
 * Created by Garik Kalashyan on 3/8/2017.
 */
@Repository
public class CommonDataService extends BaseRestClient {

    @Autowired
    private CommonDataDAO commonDataDAO;

    @Autowired
    public CommonDataService(@Value("${service.id}") Integer defaultId) {
        super(defaultId);
    }

    public List<Service> getServicesByParentId(Integer parentId) {
        return commonDataDAO.getServicesByParent(parentId);
    }

    public List<Service> getServices() {
        return commonDataDAO.getServices();
    }

    public void deleteSosoService(Integer serviceId) {
        Service service = commonDataDAO.getServiceById(serviceId);
        commonDataDAO.deleteService(serviceId);
        if (service.getImgpath() != null) {
            deleteServiceOldLogoFromFiles(getBasePathOfResources() + service.getImgpath());
        }

    }

    private String getBasePathOfResources() {
        return new File(".").getAbsoluteFile().getParentFile().getPath();
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

    public Service getServiceById(Integer serviceId) {
        return commonDataDAO.getServiceById(serviceId);
    }

    public boolean deleteServiceOldLogoFromFiles(String oldLogoPath) {
        return new File(oldLogoPath).delete();
    }

    public void updateLogoOfService(Integer serviceId, String path) {
        commonDataDAO.updateLogoOfService(serviceId, path);

    }


}
