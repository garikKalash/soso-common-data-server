package com.soso.controller;

import com.soso.models.CountryPhoneModel;
import com.soso.models.Service;
import com.soso.service.CommonDataService;
import com.soso.service.JsonConverter;
import com.soso.service.JsonMapBuilder;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
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

    @RequestMapping(value = "/getSosoServices/{parentId}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, "text/plain;charset=UTF-8"})
    public void getServices(@PathVariable("parentId") Integer parentId, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");

        List<Service> sosoServicesList = commonDataService.getServicesByParentId(parentId);
        String servicesListJsonString = JsonConverter.toJson(new JsonMapBuilder()
                .add("sosoServices", sosoServicesList)
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

    @RequestMapping(value = "/countryCodes", method = RequestMethod.GET)
    public void loadClassifiersFolRegister(HttpServletResponse response) throws IOException {
        List<CountryPhoneModel> countryPhoneModelList = commonDataService.getAllCountryCodes();
        String countryPhoneModelListJsonString = JsonConverter.toJson(new JsonMapBuilder()
                .add("phoneCodes", countryPhoneModelList)
                .build());

        response.getWriter().write(countryPhoneModelListJsonString);
    }

    @RequestMapping(value = "/servicephoto/{serviceId}", method = RequestMethod.GET)
    public void getPhotoById(@PathVariable(value = "serviceId") Integer serviceId, HttpServletResponse response) throws IOException {
        String imgPath = commonDataService.getImgPathOfService(serviceId);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        IOUtils.copy(getImageInputStreamByImgPath(imgPath), response.getOutputStream());
    }

    @RequestMapping(value = "/commonphoto/{id}", method = RequestMethod.GET)
    public void getCommonPhotoById(@PathVariable(value = "id") Integer id, HttpServletResponse response) throws IOException {
        String imgPath = commonDataService.getImgPathWithId(id);
        response.setContentType(MediaType.IMAGE_GIF_VALUE);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        IOUtils.copy(getImageInputStreamByImgPath(imgPath), response.getOutputStream());
    }


    private InputStream getImageInputStreamByImgPath(String imagePath) throws IOException {
        BufferedImage image = ImageIO.read(new File(imagePath));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", os);
        return new ByteArrayInputStream(os.toByteArray());
    }


}
