package com.soso.cache;

import com.soso.models.MessageDto;
import com.soso.persistance.CommonDataDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Garik Kalashyan on 22-Feb-18.
 */

@Component
@Scope("singletone")
public class MessageCacheImpl implements Cache<MessageDto> {
    private final Map<Integer,MessageDto> messageDtoMap = new HashMap<>();

    @Autowired
    private CommonDataDAO commonDataDAO;

    @PostConstruct
    private void init(){
        List<MessageDto> messageDtoList = commonDataDAO.getMessages();
        messageDtoList.forEach(messageDto -> {
            messageDtoMap.put(messageDto.getId(),messageDto);
        });
    }

    @Override
    public MessageDto getById(@NotNull Integer id) {
        return messageDtoMap.get(id);
    }

    @Override
    public void put(MessageDto messageDto) {
        messageDtoMap.put(messageDto.getId(), messageDto);
    }

    @Override
    public Map<Integer, MessageDto> loadAll() {
        return messageDtoMap;
    }

    @Override
    public void putAll(Map<Integer, MessageDto> data) {
        messageDtoMap.putAll(data);
    }

    @Override
    public void refreshAll() {
        messageDtoMap.clear();
        commonDataDAO.getMessages().forEach(messageDto -> {
            messageDtoMap.put(messageDto.getId(), messageDto);
        });
    }

    @Override
    public boolean remove(Integer id) {
        return messageDtoMap.remove(id) != null;
    }

    @Override
    public void load(Integer... ids) {
        for (Integer id : ids) {
            MessageDto messageDto = commonDataDAO.getMessageById(id);
            messageDtoMap.put(messageDto.getId(), messageDto);
        }
    }
}


