package com.soso.validator;

import com.soso.models.MessageDto;
import com.soso.service.CommonDataService;
import com.sun.istack.internal.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Garik Kalashyan on 22-Feb-18.
 */

@Component
public class MessageValidator {
    private CommonDataService commonDataService;


    public Map<String,String> validate(@NotNull MessageDto newMessageDto, @NotNull String lang, Errors errors){
        if(hasEmptyContent(newMessageDto)){
            MessageDto messageDtoFromDb = commonDataService.getMessageById(78);
            if(lang.equals("HAY")){
                errors.rejectValue("content","wrong_content", messageDtoFromDb.getHay());
            }else if(lang.equals("ENG")){
                errors.rejectValue("content","wrong_content",messageDtoFromDb.getEng());
            }
        }
    }

    private boolean hasEmptyContent(MessageDto newMessageDto){
        return  (newMessageDto.getEng() == null || newMessageDto.getEng().trim().equals(""))
                && (newMessageDto.getHay() == null || newMessageDto.getHay().trim().equals(""));
    }

    public void setCommonDataService(CommonDataService commonDataService){
        this.commonDataService = commonDataService;
    }
}
