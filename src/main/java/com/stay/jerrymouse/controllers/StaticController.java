package com.stay.jerrymouse.controllers;

import com.stay.jerrymouse.http.Controller;
import com.stay.jerrymouse.http.Request;
import com.stay.jerrymouse.http.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * AuThor：StAY_
 * Create:2019/11/16
 */
public class StaticController extends Controller {
    private static final String HOME = System.getenv("JM_HOME");
    public void doGet(Request request, Response response) throws IOException {
        //根据url找到文件路径
        String filename = getFileName(request.getUrl());

        //选择content-type
        String suffix = getSuffix(filename);
        String contentType = getContentType(suffix);
        response.setContentType(contentType);


        //把文件内容作为response的body发送
        InputStream is = new FileInputStream(filename);
        byte[] buf = new byte[1024];
        int len;
        while((len = is.read(buf))!=-1){
            response.write(buf,0,len);
        }
    }

    private final Map<String,String> CONTENT_TYPE = new HashMap<String,String>(){
        {
            put("html","text/html");
            put("css","text/css");
            put("js","application/js");
        }
    };

    private String getContentType(String suffix){
        String contentType = CONTENT_TYPE.get(suffix);
        if(contentType == null){
            contentType = "text/html";
        }

        return contentType;
    }

    private String getSuffix(String filename) {
        int index = filename.lastIndexOf(".");
        if(index==-1){
            return null;
        }
        return filename.substring(index+1 );
    }

    private String getFileName(String url) {
        if(url.equals("/")){
            url = "/index.html";
        }
        return HOME+ File.separator+"webapp"+url.replace("/",File.separator);
    }
}
