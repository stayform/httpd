package com.stay.jerrymouse;

import java.io.*;
import java.net.URL;

/**
 * AuThor：StAY_
 * Create:2019/11/17
 */
public class JMClassLoader extends ClassLoader {

    private static final String HOME = System.getenv("JM_HOME");

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        //根据类名称找到name对应的*.class文件
        File classFile = getClassFile(name);
        byte[] buf = null;
        //读取该文件的内容
        try {
            buf = readClassBytes(classFile);
        } catch (IOException e) {
            throw new ClassNotFoundException(name,e);
        }
        // 调用defineClass,转为Class对象
        return defineClass(name,buf,0,buf.length);




    }

    private byte[] readClassBytes(File classFile) throws IOException {
        int len =(int)classFile.length();
        byte[] buf = new byte[len];
        InputStream is = new FileInputStream(classFile);
        is.read(buf,0,len);
        return buf;
    }


    //HOME/webapp/WEB-INF/classes
    private File getClassFile(String name){
        String filename = HOME+File.separator+"webapp"+File.separator+"WEB-INF"+ File.separator+"classes"+File.separator+name+".class";
        return new File(filename);
    }
}
