package com.skyguard.teraka.util;

import com.skyguard.teraka.http.HttpRequest;

import java.util.Map;

public class UrlUtil {


    public static String getUrl(HttpRequest request){

        StringBuilder url = new StringBuilder();
        if(request!=null){
            url.append("http://").append(request.getIp()).append(":").append(request.getPort())
                    .append(request.getUrl());
        }

        return url.toString();
    }

    public static String getUrlWithParam(String url,Map<String,Object> param){

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(url).append("?");
        int index = 0;
        for(Map.Entry entry:param.entrySet()){
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue());
            if(index<param.entrySet().size()-1){
                stringBuilder.append("&");
            }
            index++;
        }


        return stringBuilder.toString();
    }





}
