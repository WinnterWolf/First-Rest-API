package com.acme.app.main.rest;

import com.acme.app.main.models.Client;
import com.acme.app.main.resources.EnviromentVar;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Path("/rest")
public class RestResource {

    @PersistenceContext
    EntityManager entityManager;

    {
        try {
            entityManager = EnviromentVar.getConnection();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

//    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Teste");
//    EntityManager entityManager = entityManagerFactory.createEntityManager();


    static Map<Integer, Object> lista = new HashMap<>();

    //Adiciona um novo Cliente através de Json
    @POST
    @Path("add")
    @Consumes({MediaType.APPLICATION_JSON})
    public void addClient(Client client){
        entityManager.getTransaction().begin();
        entityManager.persist(client);
        lista.put(client.getId(), client);
        entityManager.getTransaction().commit();
        entityManager.close();
    }


    //Retorna a Lista completa de Clientes
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response allClients() {

        String jpqlList = "select a from Client a";
        List<Client> clients = entityManager
                .createQuery(jpqlList, Client.class)
                .getResultList();

        entityManager.close();
        return Response.ok(clients).build();
    }

    //Cria 3 Clientes para exemplo
    @POST
    @Path("exemplo")
    public void createExampleClients() {

        String[] nomes = {"João", "Maria", "Jorge"};
        int[] idades = {32, 23, 54};
        for(int i = 0;i<nomes.length;i++){
            entityManager.getTransaction().begin();
            Client client = new Client(nomes[i], idades[i]);
            entityManager.persist(client);
            lista.put(client.getId(), client);
            entityManager.getTransaction().commit();

            entityManager.close();
        }
    }

    //Busca e Retorna um cliente através da Key fornecida na URI
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("{id}")
    public Response getClientByKey(@PathParam("id") int id){
        String jpql = "select a from Client a where a.id = :id";
        Client client = entityManager
                .createQuery(jpql, Client.class)
                .setParameter("id", id)
                .getSingleResult();

        entityManager.close();
        return client != null
                ? Response.ok(client).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    //Altera um cliente existente através da key fornecida na URI
    @PUT
    @Path("change/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void addClient(@PathParam("id") int id, Client client){
        Client clientEntityManager = entityManager.find(Client.class, id);
        entityManager.getTransaction().begin();
        clientEntityManager.setNome(client.getNome());
        clientEntityManager.setIdade(client.getIdade());
        entityManager.getTransaction().commit();

        entityManager.close();
    }

    //Deleta um cliente pela id fornecida na URI e retorna o Cliente deletado
    @DELETE
    @Path("/delete/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response deleteClientByKey(@PathParam("id") int id) {
        Client clientToRemove = entityManager.find(Client.class, id);
        entityManager.getTransaction().begin();
        entityManager.remove(clientToRemove);
        entityManager.getTransaction().commit();

        entityManager.close();
        return clientToRemove != null
                ? Response.ok(clientToRemove).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    //Deleta todos os Clientes
    @DELETE
    @Path("/delete/all")
    public Response deleteAllClients() {
        entityManager.getTransaction().begin();
        int deletedCount = entityManager.createQuery("DELETE FROM Client").executeUpdate();
        entityManager.getTransaction().commit();

        entityManager.close();

        return Response.ok(deletedCount + " Registros deletados").build();
    }
}