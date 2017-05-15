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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    private static final String RELATIVE_PATH_FOR_UPLOADS = File.separatorChar + "work" + File.separatorChar + "soso-common-data-service-uploads" + File.separatorChar;


    @Autowired
    private final CommonDataService commonDataService;

    @Autowired
    public CommonDataController(CommonDataService commonDataService) {
        this.commonDataService = commonDataService;
    }

    @RequestMapping(value = "/getSosoServices/{parentId}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public
    @ResponseBody
    void getServices(@PathVariable("parentId") Integer parentId, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        List<Service> sosoServicesList = commonDataService.getServicesByParentId(parentId);
        String servicesListJsonString = JsonConverter.toJson(new JsonMapBuilder()
                .add("sosoServices", sosoServicesList)
                .build());
        response.getWriter().write(servicesListJsonString);
    }


    @RequestMapping(value = "/appsososervices", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public
    @ResponseBody
    void getServices(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        List<Service> sosoServicesList = commonDataService.getServices();
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
        if (imgPath != null && getImageInputStreamByImgPath(getBasePathOfResources() + imgPath) != null) {
            IOUtils.copy(getImageInputStreamByImgPath(getBasePathOfResources() + imgPath), response.getOutputStream());
        }
    }

    @RequestMapping(value = "/service/{serviceId}", method = RequestMethod.GET)
    public void getServiceById(@PathVariable(value = "serviceId") Integer serviceId, HttpServletResponse response) throws IOException {
        Service service = commonDataService.getServiceById(serviceId);
        response.getWriter().write(JsonConverter.toJson(service));
    }

    @RequestMapping(value = "/commonphoto/{id}", method = RequestMethod.GET)
    public void getCommonPhotoById(@PathVariable(value = "id") Integer id, HttpServletResponse response) throws IOException {
        String imgPath = commonDataService.getImgPathWithId(id);
        response.setContentType(MediaType.IMAGE_GIF_VALUE);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        IOUtils.copy(getImageInputStreamByImgPath(getBasePathOfResources() + imgPath), response.getOutputStream());
    }

    @RequestMapping(value = "/uploadserviceimage", method = RequestMethod.POST, consumes = {"multipart/mixed", "multipart/form-data"})
    public String uploadAccountImage(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, @RequestParam("id") Integer serviceId) throws IOException {

        Service service = commonDataService.getServiceById(serviceId);
        System.out.println("***** --> Initializing file with name " + getBasePathOfResources() + RELATIVE_PATH_FOR_UPLOADS + " <--  *****");
        File directory = new File(getBasePathOfResources() + RELATIVE_PATH_FOR_UPLOADS);
        String newLogoPath = null;
        if (directory.exists() && directory.isDirectory()) {
            System.out.println("***** --> Directory is existed " + getBasePathOfResources() + RELATIVE_PATH_FOR_UPLOADS + " <--  *****");
            newLogoPath = RELATIVE_PATH_FOR_UPLOADS + file.getOriginalFilename();
        } else if (directory.mkdirs()) {
            System.out.println("***** --> Creating file with name " + getBasePathOfResources() + RELATIVE_PATH_FOR_UPLOADS + " <--  *****");
            newLogoPath = RELATIVE_PATH_FOR_UPLOADS + file.getOriginalFilename();
        }
        if (newLogoPath != null) {
            if (service.getImgpath() != null) {
                commonDataService.deleteServiceOldLogoFromFiles(service.getImgpath());
            }
            commonDataService.updateLogoOfService(serviceId, newLogoPath);
            System.out.println("***** --> Transfering file with name " + newLogoPath + " <--  *****");
            file.transferTo(new File(getBasePathOfResources() + newLogoPath));
            redirectAttributes.addFlashAttribute("Your account image is changed successfully!");
        } else {
            System.out.println("***** --> New Logo is not created in" + getBasePathOfResources() + RELATIVE_PATH_FOR_UPLOADS + " <--  *****");

        }
        return "redirect:/";

    }


    private InputStream getImageInputStreamByImgPath(String imagePath) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(imagePath));
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", os);
            return new ByteArrayInputStream(os.toByteArray());
        } catch (IOException e) {
            IOException ex = new IOException("There is problem with reading file"+ imagePath);
            ex.initCause(e);
            ex.printStackTrace();
        }
        return null;

    }


    private String getBasePathOfResources() {
        return new File(".").getAbsoluteFile().getParentFile().getPath();
    }


}
