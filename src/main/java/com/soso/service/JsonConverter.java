package com.soso.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

/**
 * Created by Garik Kalashyan on 3/8/2017.
 */

public class JsonConverter {
    private final static Gson gson = new GsonBuilder().create();

    public static String toJson(Map<String,Object> object){
        return gson.toJson(object);
    }

}
