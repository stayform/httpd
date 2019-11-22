package com.stay.jerrymouse;

import com.stay.jerrymouse.http.Controller;
import com.stay.jerrymouse.http.Request;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * AuThor：StAY_
 * Create:2019/11/16
 */
public class WebApp {

    private Map<String,String> nameToClassName = new HashMap<>();
    private Map<String,String> urlToName = new HashMap<>();
    private static final String HOME = System.getenv("JM_HOME");
    private final ClassLoader classLoader = new JMClassLoader();
    public static WebApp parseXML() throws MalformedURLException {
        WebApp webApp = new WebApp();
        SAXReader reader = new SAXReader();//解析xml文件用
        Document doc = null;//拿到xml文档  ClassLoader.getResource找资源文件，都是从类的包的根路径开始找
        try {
            doc = reader.read(xmlFileName());//问题是unknow protocal e 可见文件根路径e://被当成了协议（比如http://）去解析，所以在方法中把文件路径转化为一个URL，避免被当成协议解析
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element root = doc.getRootElement();
        for(Iterator<Element> it = root.elementIterator(); it.hasNext();){
            Element element = it.next();
            switch(element.getName()){
                case "controller":{
                    String name = element.element("name").getText();
                    String className = element.element("class").getText();
                    webApp.nameToClassName.put(name,className);
                    break;
                }
                case "mapping": {
                    String name = element.element("name").getText();
                    String urlPattern = element.element("url-pattern").getText();
                    webApp.urlToName.put(urlPattern,name);
                    break;
                }
                default:
        }
    }
        return webApp;
    }

    private static URL xmlFileName() throws MalformedURLException {
        return new File(HOME+ File.separator+"webapp"+File.separator+"WEB-INF"+File.separator+"web.xml").toURI().toURL();
    }

    public Controller findController(Request request) throws ClassNotFoundException, IOException {
        String name = findName(request.getUrl());
        if(name==null){
            return null;
        }
        String className = findClassName(name);
        if(className==null){
            return null;
        }
        Controller controller = findInstance(className);
        return controller;
    }

    private Controller findInstance(String className) throws IOException {
        Class<?> cls = null;
        try {
            cls = classLoader.loadClass(className);
            return (Controller)cls.newInstance();
        } catch (IllegalAccessException|InstantiationException|ClassNotFoundException e) {
            throw new IOException(e);
        }

    }

    private String findClassName(String name) {
        return nameToClassName.get(name);
    }

    private String findName(String url) {
        return urlToName.get(url);
    }
}
