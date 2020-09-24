package com.acme.app.main;

import javax.ws.rs.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;


@Path("/rest")
public class RestResource {
    static Map<String, String> json = new HashMap<>();



    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getClients() {
        json.put("framework", "KumuluzEE");
        json.put("Teste", "API");
        json.put("Tendencia", "Hello");
        String resultado = json.get("Teste");
        System.out.println(resultado);

        return Response.ok(json).build();
    }

    @GET
    @Produces({MediaType.TEXT_PLAIN})
    @Path("{key}")
    public Response getClientByKey(@PathParam("key") String key){
        String resultado = json.get(key);
        System.out.println(resultado);
        return Response.ok(resultado).build();
    }

    @DELETE
    @Path("/delete/{key}")
    @Produces({MediaType.TEXT_PLAIN})
    public Response deleteClientByKey(@PathParam("key") String key) {
        String deleted = json.remove(key);
        return Response.ok(deleted).build();
    }


}