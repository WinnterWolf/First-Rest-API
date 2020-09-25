package com.acme.app.main.rest;

import com.acme.app.main.models.Client;
import com.acme.app.main.resources.Auxiliar;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;


@Path("/rest")
public class RestResource {
    static Map<Integer, Object> lista = new HashMap<>();

    //Adiciona um novo Cliente através de Json
    @POST
    @Path("add")
    @Consumes({MediaType.APPLICATION_JSON})
    public void addClient(Client client){
        lista.put(Auxiliar.autoIncrementKey(), client);
    }


    //Retorna a Lista completa de Clientes
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response allClients() {

        Map<Integer, Object> clientes = lista;
        return Response.ok(clientes).build();
    }

    //Cria 3 Clientes para exemplo
    @GET
    @Path("exemplo")
    @Produces({MediaType.APPLICATION_JSON})
    public void createExampleClients() {
        long[] ids = {1, 2, 3};
        String[] nomes = {"João", "Maria", "Jorge"};
        int[] idades = {32, 23, 54};
        for(int i = 0;i<ids.length;i++){
            Client client = new Client();
            client.setId(ids[i]);
            client.setIdade(idades[i]);
            client.setNome(nomes[i]);
            lista.put(Auxiliar.autoIncrementKey(), client);
        }
    }

    //Busca e Retorna um cliente através da Key fornecida na URI
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("{key}")
    public Response getClientByKey(@PathParam("key") int key){
        var resultado = lista.get(key);

        return Response.ok(resultado).build();
    }

    //Altera um cliente existente através da key fornecida na URI
    @PUT
    @Path("change/{key}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void addClient(@PathParam("key") int key, Client client){
        lista.replace(key, client);
    }

    //Deleta um cliente pela key fornecida na URI e retorna o Cliente deletado
    @DELETE
    @Path("/delete/{key}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response deleteClientByKey(@PathParam("key") int key) {
        var deleted = lista.remove(key);
        return Response.ok(deleted).build();
    }

    //Deleta todos os Clientes
    @DELETE
    @Path("/delete/all")
    @Produces({MediaType.APPLICATION_JSON})
    public void deleteAllClients() {
        lista.clear();
    }
}