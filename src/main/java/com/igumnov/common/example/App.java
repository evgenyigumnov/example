package com.igumnov.common.example;

import com.igumnov.common.ORM;
import com.igumnov.common.WebServer;

public class App {

    public static void main(String[] args) throws Exception {

        ORM.connectionPool("org.h2.Driver", "jdbc:h2:mem:test", "SA", "", 10, 30);
        ORM.applyDDL("sql");
        WebServer.init("localhost", 8181);
        WebServer.start();

    }

}