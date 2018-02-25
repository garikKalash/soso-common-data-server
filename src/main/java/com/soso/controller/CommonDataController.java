package com.soso.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.soso.models.CountryPhoneModel;
import com.soso.models.MessageDto;
import com.soso.models.Service;
import com.soso.service.CommonDataService;
import com.soso.service.JsonConverter;
import com.soso.service.JsonMapBuilder;
import com.soso.validator.MessageValidator;
import com.soso.validator.ServiceValidator;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sun.plugin2.message.Message;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.Map;

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
    private ServiceValidator serviceValidator;

    @Autowired
    private MessageValidator messageValidator;

    @Autowired
    public CommonDataController(CommonDataService commonDataService) {
        this.commonDataService = commonDataService;
    }

    @PostConstruct
    public void init(){
      serviceValidator.setCommonDataService(commonDataService);
    }

    @RequestMapping(value = "/getSosoServices/{parentId}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<List> getServices(@PathVariable("parentId") Integer parentId, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        List<Service> sosoServicesList = commonDataService.getServicesByParentId(parentId);
        return new ResponseEntity<>(sosoServicesList, HttpStatus.OK);
    }


    @RequestMapping(value = "/appsososervices", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<List> getServices(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        List<Service> sosoServicesList = commonDataService.getServices();
        return new ResponseEntity<>(sosoServicesList, HttpStatus.OK);
    }


    @RequestMapping(value = "/deleteSosoServices/{serviceId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> deleteServiceById(@PathVariable("serviceId") Integer serviceId, HttpServletResponse response) throws IOException {
       return new ResponseEntity<>(commonDataService.deleteSosoService(serviceId), HttpStatus.OK);
    }

    @RequestMapping(value = "/createSosoService", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createService(@RequestBody Service service, HttpServletResponse response,Errors errors,
                              @RequestHeader(HttpHeaders.ACCEPT_LANGUAGE) String language) throws IOException {
        response.setCharacterEncoding("UTF-8");
        serviceValidator.validate(service, language, errors);
        if(!errors.hasErrors()){
                Integer newServiceId = commonDataService.createSosoService(service);
                service.setId(newServiceId);
                return new ResponseEntity<>(service, HttpStatus.OK);
        } else{
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/countryCodes", method = RequestMethod.GET)
    public ResponseEntity<List> loadClassifiersFolRegister(HttpServletResponse response) throws IOException {
        List<CountryPhoneModel> countryPhoneModelList = commonDataService.getAllCountryCodes();
        return new ResponseEntity<>(countryPhoneModelList,HttpStatus.OK);
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
    public ResponseEntity<Service> getServiceById(@PathVariable(value = "serviceId") Integer serviceId, HttpServletResponse response) throws IOException {
        Service service = commonDataService.getServiceById(serviceId);
        return new ResponseEntity<>(service, HttpStatus.OK);
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

    @RequestMapping(value = "/systemmessages", method = RequestMethod.GET)
    public ResponseEntity<List> getMessages(){
        return new ResponseEntity<>(commonDataService.getMessages(), HttpStatus.OK);
    }

    @RequestMapping(value = "/systemmessage/{id}", method = RequestMethod.GET)
    public ResponseEntity<MessageDto> getMessageById(@PathVariable(value = "id") Integer id){
        return new ResponseEntity<>(commonDataService.getMessageById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/addmessage", method = RequestMethod.POST)
    public ResponseEntity addMessage(@RequestBody MessageDto messageDto, Errors errors,
                                     @RequestHeader(HttpHeaders.ACCEPT_LANGUAGE) String language) {
        messageValidator.validate(messageDto, language, errors);
        if(!errors.hasErrors()){
            Integer newMessageId = commonDataService.addMessage(messageDto);
            messageDto.setId(newMessageId);
            return new ResponseEntity<>(messageDto, HttpStatus.OK);
        } else{
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/deletesystemmessage/{messageid}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> deleteMessageById(Integer messageId) {
        return new ResponseEntity<>(commonDataService.deleteMessageById(messageId), HttpStatus.OK);
    }




    private String getBasePathOfResources() {
        return new File(".").getAbsoluteFile().getParentFile().getPath();
    }


}
