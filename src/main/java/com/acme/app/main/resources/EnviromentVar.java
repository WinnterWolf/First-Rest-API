package com.acme.app.main.resources;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;



public class EnviromentVar {



    public EnviromentVar() throws URISyntaxException {
    }


    public static EntityManager getConnection() throws URISyntaxException {
//        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String dbUri = new String(System.getenv("DATABASE_URL"));
        String username = dbUri.split(":")[1];
        username = username.substring(2);
        System.out.println(username);
        String password = dbUri.split(":")[2];
        password = password.substring(0, password.indexOf("@"));
        System.out.println("password "+password);
        String dbHost = dbUri.split("@")[1];
        String dbUrl = "jdbc:postgresql://" + dbHost + "?sslmode=require";

        EntityManagerFactory managerFactory = null;
        Map<String, String> persistenceMap = new HashMap<String, String>();

        persistenceMap.put("javax.persistence.jdbc.url", dbUrl);
        persistenceMap.put("javax.persistence.jdbc.user", username);
        persistenceMap.put("javax.persistence.jdbc.password", password);
        persistenceMap.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");

        System.out.println(persistenceMap);

        managerFactory = Persistence.createEntityManagerFactory("Heroku", persistenceMap);
        EntityManager manager = managerFactory.createEntityManager();

        return manager;
    }
//    EntityManager heroku = getConnection();
}
