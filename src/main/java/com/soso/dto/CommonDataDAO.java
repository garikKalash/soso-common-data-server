package com.soso.dto;

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
    @Autowired
    private NamedParameterJdbcOperations namedParameterJdbcOperations;

    public NamedParameterJdbcOperations getNamedParameterJdbcOperations() {
        return namedParameterJdbcOperations;
    }

    private final String QUERY_FOR_LOADING_SERVICES = "SELECT * FROM public.C_Service";
    private final String QUERY_FOR_LOADING_COUNTRY_CODES = "SELECT * FROM public.C_CountryPhoneCodes";

    public Integer createService(Service item) {
        String createUserQuery = "SELECT addservice ( :_servicename_arm, :_servicename_eng)";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("_servicename_arm", item.getServiceName_arm());
        paramMap.put("_servicename_eng", item.getServiceName_eng());
        return getNamedParameterJdbcOperations().queryForObject(createUserQuery, paramMap, Integer.class);
    }

    public void deleteService(Integer serviceId) {
        String createUserQuery = "SELECT deleteservice ( :_serviceId)";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("_serviceId", serviceId);
        getNamedParameterJdbcOperations().queryForObject(createUserQuery, paramMap, Integer.class);

    }

    public List<Service> getAllServices() {
        return getNamedParameterJdbcOperations().query(QUERY_FOR_LOADING_SERVICES,  new BeanPropertyRowMapper<>(Service.class));

    }

    public List<CountryPhoneModel> getCountryPhoneCodes(){
        return getNamedParameterJdbcOperations().query(QUERY_FOR_LOADING_COUNTRY_CODES, new BeanPropertyRowMapper<>(CountryPhoneModel.class));
    }



}
