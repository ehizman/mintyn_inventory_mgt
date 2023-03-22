package com.ehizman.inventorymgt.model;

import com.ehizman.inventorymgt.util.GsonFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderFactory {
    public static Order fromJson(String json){
        Gson gson = GsonFactory.getGsonObject();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        log.info("Json Object --> {}", jsonObject.toString());
        return gson.fromJson(jsonObject.toString(), Order.class);
    }
}
