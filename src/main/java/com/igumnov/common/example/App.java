package com.igumnov.common.example;

import com.igumnov.common.ORM;
import com.igumnov.common.WebServer;

import java.util.Date;

public class App {

    public static void main(String[] args) throws Exception {

        ORM.connectionPool("org.h2.Driver", "jdbc:h2:mem:test", "SA", "", 10, 30);
        ORM.applyDDL("sql");
        WebServer.init("localhost", 8181);

        WebServer.security("/login", "/login", "/logout");
        WebServer.addRestrictRule("/*", new String[]{"user_role"});
        WebServer.addAllowRule("/static/*");
        WebServer.addUser("demo", "demo", new String[]{"user_role"});

        WebServer.addTemplates("pages");
        WebServer.addController("/", (request, model) -> {
            model.put("time", new Date());
            return "index";
        });
        WebServer.addController("/login", (request, model) -> {
            return "login";
        });



        WebServer.start();

    }

}