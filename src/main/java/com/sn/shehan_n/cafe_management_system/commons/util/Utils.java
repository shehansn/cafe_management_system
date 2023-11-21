package com.sn.shehan_n.cafe_management_system.commons.util;

import com.google.common.base.Strings;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileFilter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Utils {
    private Utils(){

    }
    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus){

        return new ResponseEntity<String>("{\"Message \":\""+responseMessage+"\"}", httpStatus);
    }

    public static String getUUID(){
        Date date =new Date();
        long time =date.getTime();

        return "BILL-"+time;
    }

    public static JSONArray getJsonArrayFromString(String data) throws JSONException{
        JSONArray jsonArray=new JSONArray(data);
        return jsonArray;
    }

    public static Map<String,Object> getMapFromJson(String data){
        if(!Strings.isNullOrEmpty(data)){
            return new Gson().fromJson(data,new TypeToken<Map<String,Object>>(){

            }.getType());
        }
        return new HashMap<>();
    }

    public static Boolean isFileExist(String path){
        log.info("Inside is File Exist {}",path);
        try {
            File file=new File(path);
            return (file!=null && file.exists()) ? Boolean.TRUE:Boolean.FALSE;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }
}
