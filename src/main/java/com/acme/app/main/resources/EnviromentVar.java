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

        if(System.getenv("DATABASE_URL") != null) {
            URI dbUri = new URI(System.getenv("DATABASE_URL"));

            String username = dbUri.getUserInfo().split(":")[0];
            System.out.println(username);
            String password = dbUri.getUserInfo().split(":")[1];
            System.out.println(password);
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";

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
        } else {
            EntityManagerFactory managerFactory;
            managerFactory = Persistence.createEntityManagerFactory("localPG");
            EntityManager manager = managerFactory.createEntityManager();
            return manager;
        }
    }
}
