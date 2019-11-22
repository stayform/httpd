package com.stay.jerrymouse.http;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * AuThor：StAY_
 * Create:2019/11/16
 */
public class Request {
    private String method;
    private String url;
    private String protocol;
    private Map<String,String> requestParams = new HashMap<>();
    private Map<String,String> headers = new HashMap<>();

    /**
     * 对请求行进行获取、分割、解码
     * 填充request对象的信息
     * @param is
     * @return
     * @throws IOException
     */
    public static Request parse(InputStream is)throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        Request request = new Request();
        parseRequestLine(reader,request);
        parseRequestHeaders(reader,request);

        return request;
    }

    private static void parseRequestLine(BufferedReader reader, Request request) throws IOException {
        String line = reader.readLine();
        if(line == null){
            throw new IOException("读到结尾");
        }
        String[] fragments = line.split(" ");
        if(fragments.length!=3){
            throw new IOException("错误的请求行");
        }

        request.setMethod(fragments[0]);
        request.setUrl(fragments[1]);
        request.setProtocol(fragments[2]);
    }

    private static void parseRequestHeaders(BufferedReader reader, Request request) throws IOException {
        String line;
        while((line=reader.readLine())!=null&&line.trim().length()!=0){//trim()去掉reader头尾的空格，如果length为0则代表里面没有东西了
            String[] fragments = line.split(":");
            String key = fragments[0].trim();
            String value = fragments[1].trim();

            request.setHeader(key,value);
        }
    }





    private void setMethod(String method) {
        this.method = method.toUpperCase();
    }
    private void setUrl(String url) throws UnsupportedEncodingException,IOException {
        String[] fragments = url.split("\\?");
        this.url = URLDecoder.decode(fragments[0],"UTF-8");
        if(fragments.length>1){
            setRequestParams(fragments[1]);
        }
    }

    private void setRequestParams(String queryString) throws IOException {
        for(String kv : queryString.split("&")){
            String[] fragments = kv.split("=");
            String key = fragments[0];
            String value = "";
            if(fragments.length>1){
                value = URLDecoder.decode(fragments[1],"UTF-8");
            }
            requestParams.put(key,value);
        }

    }


    private void setProtocol(String protocol) throws IOException {
        if(!protocol.toUpperCase().startsWith("HTTP")){
            throw new IOException("错误的HTTP版本");
        }
        this.protocol = protocol.toUpperCase();
    }
    private void setHeader(String key, String value) {
        headers.put(key,value);
    }


    public String getMethod(){
        return this.method;
    }

    public String getUrl() {
        return url;
    }

    public String getProtocol() {
        return protocol;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getRequestParams() {
        return requestParams;
    }
}
