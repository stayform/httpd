package com.stay.jerrymouse;

import com.stay.jerrymouse.controllers.StaticController;
import com.stay.jerrymouse.http.Controller;
import com.stay.jerrymouse.http.Request;
import com.stay.jerrymouse.http.Response;
import com.stay.jerrymouse.http.Status;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import static java.awt.Event.HOME;

/**
 * AuThor：StAY_
 * Create:2019/11/16
 */
public class Server {
    private static final String HOME = System.getenv("JM_HOME");
    private final Controller staticController = new StaticController();
    private final WebApp webApp = WebApp.parseXML();

    public Server() throws MalformedURLException {
    }

    public void run(int port) throws IOException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket(port);
        while(true) {
            try {
                Socket socket = serverSocket.accept();
                Request request = Request.parse(socket.getInputStream());
                Response response = Response.build(socket.getOutputStream());
                //如果存在url的静态文件存在，就当成静态文件处理，否则就是动态文件
                String filename = getFileName(request.getUrl());
                File file = new File(filename);
                Controller controller = null;
                if (file.exists()) {//
                    controller = staticController;//当成静态文件处理
                } else {
                    //当成动态文件处理
                    controller = webApp.findController(request);
                    System.out.println(request.getUrl());
                }


                if (controller == null) {
                    response.setStatus(Status.NOT_FOUND);
                    response.println(Status.NOT_FOUND.getReason());
                } else {
                    switch (request.getMethod()) {
                        case "GET":
                            controller.doGet(request, response);
                            break;
                        case "POST":
                            controller.doPost(request, response);
                            break;
                        default:
                            response.setStatus(Status.METHOD_NOT_ALLOWED);
                            response.println(Status.METHOD_NOT_ALLOWED.getReason());
                    }
                }
                response.flush();
                socket.close();
            }catch(SocketException e){
                e.printStackTrace();
            }finally {
                continue;
            }
        }
    }


    private String getFileName(String url) {
        if(url.equals("/")){
            url = "/index.html";
        }
        return HOME+ File.separator+"webapp"+File.separator+url.replace("/",File.separator);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new Server().run(8080);
    }
}
