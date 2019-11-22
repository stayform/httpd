package com.stay.jerrymouse.http;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * AuThor：StAY_
 * Create:2019/11/16
 */
public class Response {
    private OutputStream os;
    private Status status = Status.OK;
    private Map<String,String> headers = new HashMap<>();//储存响应头信息
    private final byte[] buf = new byte[8192];//储存响应信息的字节数组
    private int offset = 0;
    public Response(OutputStream os){
        this.os = os;
    }
    public static Response build(OutputStream os){
        Response response = new Response(os);
        response.setServer();
        response.setDate();
        return new Response(os);
    }

    private void setDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        setHeader("Date",sdf.format(new Date()));
    }

    private void setServer() {
        setHeader("Server","JerryMouse");
    }


    public void setStatus(Status status) {
        this.status = status;
    }

    public void setContentType(String s){
        setHeader("Content-Type",s+" charset=UTF-8");
    }

    private void setHeader(String key, String value) {
        headers.put(key,value);
    }

    /**
     * 把正文放到buf数组里
     * @param o
     * @throws UnsupportedEncodingException
     */
    public void print(Object o) throws UnsupportedEncodingException {
        //TODO:不考虑buf溢出问题
        byte[] src =o.toString().getBytes("UTF-8");
        System.arraycopy(src,0,buf,offset,src.length);
        offset+=src.length;
    }

    public void println(Object o) throws IOException {
        print(String.format("%s%n",o));
    }

    public void printf(String format,Object... o) throws  UnsupportedEncodingException {
        print(new Formatter().format(format,o));//相当于String.  format("%s","/html")
    }

    public void flush() throws IOException {
        setHeader("Content-Length",String.valueOf(offset));//计算正文长度
        sendResponseLine();
        sendResponseHeaders();
        sendResponseBody();
    }

    private void sendResponseLine() throws IOException {
        String responseLine = String.format("HTTP/1.0 %d %s\r\n",status.getCode(),status.getReason());
        os.write(responseLine.getBytes("UTF-8"));//把响应行写入输出流
    }

    private void sendResponseHeaders() throws IOException {
        for(Map.Entry<String,String> entry : headers.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            String headerLine = String.format("%s: %s\r\n",key,value);
            os.write(headerLine.getBytes("UTF-8"));
        }
        os.write('\r');
        os.write('\n');
    }

    private void sendResponseBody() throws IOException {
        os.write(buf,0,offset);//把响应体写入输出流
    }

    public void write(byte[] b, int off, int len) {
        System.arraycopy(b,off,buf,offset,len);
        offset+=len;
    }
}
