package com.soso.persistance;

import com.soso.models.CountryPhoneModel;
import com.soso.models.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Garik Kalashyan on 3/8/2017.
 */

@Repository
public class CommonDataDAO {
    private final String QUERY_FOR_LOADING_SERVICES = "SELECT * FROM public.C_Service WHERE parentid = :parentid";
    private final String QUERY_FOR_LOADING_COUNTRY_CODES = "SELECT * FROM public.C_CountryPhoneCodes";
    private final String GET_SERVICE_BY_ID = "SELECT * FROM public.c_service WHERE id=:_id";
    private final String UPDATE_LOGO_SRC_PATH_OF_SERVICE_QUERY = "UPDATE public.c_service SET imgpath = :imgpath WHERE id = :id";

    @Autowired
    private NamedParameterJdbcOperations namedParameterJdbcOperations;

    public NamedParameterJdbcOperations getNamedParameterJdbcOperations() {
        return namedParameterJdbcOperations;
    }

    public Integer createService(Service item) {
        String createUserQuery = "SELECT addservice ( :_servicename_arm, :_parentid ,:_servicename_eng)";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("_servicename_arm", item.getServiceName_arm());
        paramMap.put("_parentid",item.getParentid());
        paramMap.put("_servicename_eng", item.getServiceName_eng());
        return getNamedParameterJdbcOperations().queryForObject(createUserQuery, paramMap, Integer.class);
    }

    public void deleteService(Integer serviceId) {
        String createUserQuery = "SELECT deleteservice ( :_serviceId)";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("_serviceId", serviceId);
        getNamedParameterJdbcOperations().queryForObject(createUserQuery, paramMap, Integer.class);

    }


    public List<Service> getServicesByParent(Integer parentId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("parentid", parentId);
        return getNamedParameterJdbcOperations().query(QUERY_FOR_LOADING_SERVICES, paramMap, new BeanPropertyRowMapper<>(Service.class));

    }

    public List<Service> getServices() {
        return getNamedParameterJdbcOperations().query("SELECT * FROM public.c_service", new BeanPropertyRowMapper<>(Service.class));
    }


    public List<CountryPhoneModel> getCountryPhoneCodes() {
        return getNamedParameterJdbcOperations().query(QUERY_FOR_LOADING_COUNTRY_CODES, new BeanPropertyRowMapper<>(CountryPhoneModel.class));
    }

    public String getImgPathOfService(Integer serviceId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("serviceid", serviceId);
        return getNamedParameterJdbcOperations().queryForObject("SELECT imgpath FROM c_service WHERE id=:serviceid", paramMap, String.class);

    }

    public String getImgPathWithId(Integer id) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);
        return getNamedParameterJdbcOperations().queryForObject("SELECT path FROM c_commonimages WHERE id=:id", paramMap, String.class);

    }

    public Service getServiceById(Integer serviceId) {
        Map<String, Object> params = new HashMap<>();
        params.put("_id", serviceId);
        return getNamedParameterJdbcOperations().queryForObject(GET_SERVICE_BY_ID, params, new BeanPropertyRowMapper<>(Service.class));

    }

    public void updateLogoOfService(Integer serviceId, String path) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", serviceId);
        paramMap.put("imgpath", path);
        getNamedParameterJdbcOperations().update(UPDATE_LOGO_SRC_PATH_OF_SERVICE_QUERY, paramMap);


    }

}
