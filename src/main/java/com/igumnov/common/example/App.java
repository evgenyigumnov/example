package com.igumnov.common.example;

import com.igumnov.common.ORM;
import com.igumnov.common.WebServer;
import com.igumnov.common.reflection.ReflectionException;
import com.igumnov.common.webserver.WebServerException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;

public class App {

    public static void main(String[] args) throws Exception {

        ORM.connectionPool("org.h2.Driver", "jdbc:h2:mem:test", "SA", "", 1, 3);
        ORM.applyDDL("sql");
        WebServer.setPoolSize(5,10);
        WebServer.init("localhost", 8181);

        WebServer.security("/login", "/login?error=1", "/logout");
        WebServer.addRestrictRule("/*", new String[]{"user_role"});
        WebServer.addAllowRule("/static/*");
        WebServer.addClassPathHandler("/static", "META-INF/resources/webjars");
        WebServer.addAllowRule("/js/*");
        WebServer.addStaticContentHandler("/js", "javascript");


        WebServer.addTemplates("pages",0);
        WebServer.addController("/", (request, model) -> {
            model.put("time", new Date());
            return "index";
        });
        WebServer.addController("/login", (request, model) -> {

            return "login";
        });



        WebServer.addRestController("/rest/user", ExampleUser.class, (request, postObj) -> {
            switch (request.getMethod()) {
                case (WebServer.METHOD_GET):
                    ArrayList<Object> users;
                    try {
                        users = ORM.findAll(ExampleUser.class);
                    } catch (Exception e) {
                        throw new WebServerException(e.getMessage());
                    }
                    return users;
                case (WebServer.METHOD_POST):
                    ExampleUser ret = null;
                    try {
                        ret = (ExampleUser) ORM.insert(postObj);
                    } catch (Exception e) {
                        throw new WebServerException(e.getMessage());
                    }
                    return ret;
                case (WebServer.METHOD_DELETE):
                    ExampleUser user;
                    try {
                        user = (ExampleUser) ORM.findOne(ExampleUser.class, request.getParameter("userName"));
                        if(user.getUserName().equals("demo")) {
                            throw new WebServerException("You cant delete user demo");
                        } else {
                            ORM.delete(user);
                        }
                    } catch (Exception e) {
                        throw new WebServerException(e.getMessage());
                    }
                    return user;
                default:
                    throw new WebServerException("Unsupported method");
            }
        });


        ArrayList<Object> users = ORM.findAll(ExampleUser.class);

        if (users.size() == 0) {
            ExampleUser user = new ExampleUser();
            user.setUserName("demo");
            user.setUserPassword("demo");
            ORM.insert(user);
            WebServer.addUser("demo", "demo", new String[]{"user_role"});
        }

        users.stream().forEach((user) -> {
            ExampleUser u = (ExampleUser) user;
            WebServer.addUser(u.getUserName(), u.getUserPassword(), new String[]{"user_role"});
        });

        WebServer.start();

    }

}