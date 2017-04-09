package com.soso.controller;

import com.soso.models.CountryPhoneModel;
import com.soso.models.Service;
import com.soso.service.CommonDataService;
import com.soso.service.JsonConverter;
import com.soso.service.JsonMapBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PathParam;
import java.io.IOException;
import java.util.List;

/**
 * Created by Garik Kalashyan on 3/4/2017.
 */

@CrossOrigin("*")
@Controller
@RequestMapping("commonData")
public class CommonDataController {
    
    @Autowired
    private CommonDataService commonDataService;

    @RequestMapping(value = "/getSosoServices", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, "text/plain;charset=UTF-8"})
    public void getServices(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");

        List<Service> sosoServicesList = commonDataService.getAllSosoServices();
        String servicesListJsonString = JsonConverter.toJson(new JsonMapBuilder()
                .add("sosoServices",sosoServicesList)
                .build());

        response.getWriter().write(servicesListJsonString);
    }

    @RequestMapping(value = "/deleteSosoServices/{serviceId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteServiceById(@PathVariable("serviceId") Integer serviceId, HttpServletResponse response) throws IOException {
          commonDataService.deleteSosoService(serviceId);
    }

    @RequestMapping(value = "/createSosoService", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void createService(@RequestBody Service service, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");

        Integer newServiceId = commonDataService.createSosoService(service);
        if (newServiceId != null) {
            service.setId(newServiceId);
            response.getWriter().write(JsonConverter.toJson(new JsonMapBuilder()
                    .add("service", service)
                    .build()));
        } else {
            response.getWriter().write("Added client's id is NULL. ");
        }
    }

    @RequestMapping(value = "/countryCodes" , method = RequestMethod.GET)
    public  void loadClassifiersFolRegister(HttpServletResponse response) throws IOException {
        List<CountryPhoneModel> countryPhoneModelList = commonDataService.getAllCountryCodes();
        String countryPhoneModelListJsonString = JsonConverter.toJson(new JsonMapBuilder()
                .add("phoneCodes",countryPhoneModelList)
                .build());

        response.getWriter().write(countryPhoneModelListJsonString);
    }



}
