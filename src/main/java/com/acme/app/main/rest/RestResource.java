package com.acme.app.main.rest;

import com.acme.app.main.models.Client;
import com.acme.app.main.resources.EnviromentVar;

import javax.persistence.*;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Path("/rest")
public class RestResource {

    EntityManager entityManager = null;
    {
        try {
            entityManager = EnviromentVar.getConnection();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    //Adiciona um novo Cliente através de Json
    @POST
    @Path("add")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response addClient(@Valid Client client) throws PersistenceException{

        if(client != null){
            try {
                entityManager.getTransaction().begin();
                entityManager.persist(client);
                entityManager.getTransaction().commit();
            } catch (PersistenceException e){
                String message = "There was an error with your request. Try again with the right fields. nome and idade.";
                return Response.status(Response.Status.NOT_ACCEPTABLE).entity(message).build();
            }
        }
        return Response.ok().build();
    }


    //Retorna a Lista completa de Clientes
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response allClients() {

        String jpqlList = "select a from Client a";
        List<Client> clients = entityManager
                .createQuery(jpqlList, Client.class)
                .getResultList();
        return Response.ok(clients).build();
    }

    //Cria 3 Clientes para exemplo
    @POST
    @Path("exemplo")
    public Response createExampleClients() {

        String[] nomes = {"João", "Maria", "Jorge"};
        int[] idades = {32, 23, 54};
        for(int i = 0;i<nomes.length;i++){

            entityManager.getTransaction().begin();
            Client client = new Client(nomes[i], idades[i]);
            entityManager.persist(client);
            entityManager.getTransaction().commit();

        }
        return Response.noContent().build();
    }

    //Busca e Retorna um cliente através da Key fornecida na URI
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("{id}")
    public Response getClientByKey(@PathParam("id") int id) throws NoResultException{

        String query = "select a from Client a where a.id = :id";
        try {
            Client client = entityManager
                    .createQuery(query, Client.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return Response.ok(client).build();
        } catch (NoResultException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    //Altera um cliente existente através da key fornecida na URI
    @PUT
    @Path("change/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response addClient(@PathParam("id") int id, @Valid Client client) throws NullPointerException{

        try {
            Client clientEntityManager = entityManager.find(Client.class, id);
            entityManager.getTransaction().begin();
            clientEntityManager.setNome(client.getNome());
            clientEntityManager.setIdade(client.getIdade());
            entityManager.getTransaction().commit();
            return Response.ok().build();
        } catch (NullPointerException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    //Deleta um cliente pela id fornecida na URI e retorna o Cliente deletado
    @DELETE
    @Path("/delete/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response deleteClientById(@PathParam("id") int id) throws IllegalArgumentException {

        try {
            Client clientToRemove = entityManager.find(Client.class, id);
            entityManager.getTransaction().begin();
            entityManager.remove(clientToRemove);
            entityManager.getTransaction().commit();

            return Response.ok(clientToRemove).build();


        } catch (IllegalArgumentException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

    }

    //Deleta todos os Clientes
    @DELETE
    @Path("/delete/all")
    public Response deleteAllClients() {
        entityManager.getTransaction().begin();
        int deletedCount = entityManager.createQuery("DELETE FROM Client").executeUpdate();
        entityManager.getTransaction().commit();



        return Response.ok(deletedCount + " Registros deletados").build();
    }
}