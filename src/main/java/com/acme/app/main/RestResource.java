package com.acme.app.main;

import javax.ws.rs.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;


@Path("/rest")
public class RestResource {
    Map<String, String> json = new HashMap<>();



    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getResources() {
        json.put("framework", "KumuluzEE");
        json.put("Teste", "API");
        json.put("Tendencia", "Hello");
        String resultado = json.get("Teste");
        System.out.println(resultado);

        return Response.ok(json).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/rest/{key}")
    public String getValorByKey(@PathParam("key") String key){
        String resultado = json.get("Teste");
        System.out.println(resultado);
        return resultado;
    }

//    @DELETE
//    @Path("/rest/delete/{key}")
//    public boolean deleteOrderById(@PathParam("id") int id) {
//        return Response.Instance.deleteDataById(id);
//    }


}