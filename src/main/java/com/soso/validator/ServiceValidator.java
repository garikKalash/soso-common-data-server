package com.soso.validator;

import com.soso.models.MessageDto;
import com.soso.models.Service;
import com.soso.service.CommonDataService;
import com.sun.istack.internal.NotNull;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Garik Kalashyan on 22-Feb-18.
 */

@Component
@Scope("singleton")
public class ServiceValidator {

  private CommonDataService commonDataService;


  public void validate(@NotNull Service newService,@NotNull String lang, Errors errors){
     if(hasEmptyName(newService)){
         MessageDto messageDtoFromDb = commonDataService.getMessageById(79);
         if(lang.equals("HAY")){
             errors.rejectValue("servicename","wrong_content", messageDtoFromDb.getHay());
         }else if(lang.equals("ENG")){
             errors.rejectValue("servicename","wrong_content",messageDtoFromDb.getEng());
         }
     }

  }

  private boolean hasEmptyName(Service newService){
    return  (newService.getServiceName_arm() == null || newService.getServiceName_arm().trim().equals(""))
            && (newService.getServiceName_eng() == null || newService.getServiceName_eng().trim().equals(""));
  }

  public void setCommonDataService(CommonDataService commonDataService){
      this.commonDataService = commonDataService;
  }

}
