package org.example.banque.web;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.example.banque.entities.Client;
import org.example.banque.entities.Compte;
import org.example.banque.repositories.ClientRepository;
import org.example.banque.repositories.CompteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/** Endpoints CRUD des clients, disponibles en JSON et en XML. */
@Component
@Path("/banque/clients")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ClientRestJaxRSAPI {

    @Autowired
    private ClientRepository depotClients;

    @Autowired
    private CompteRepository depotComptes;

    // Liste complète des clients
    @GET
    public List<Client> getClients() {
        return depotClients.findAll();
    }

    // Détail d'un client, ou 404 s'il n'existe pas
    @Path("/{id}")
    @GET
    public Client getClient(@PathParam("id") Long id) {
        return depotClients.findById(id).orElseThrow(NotFoundException::new);
    }

    // Tous les comptes dont ce client est titulaire
    @Path("/{id}/comptes")
    @GET
    public List<Compte> getComptesDuClient(@PathParam("id") Long id) {
        if (!depotClients.existsById(id)) {
            throw new NotFoundException();
        }
        return depotComptes.findByTitulaireIdentifiant(id);
    }

    // Création d'un client ; l'identifiant est toujours généré par la base
    @POST
    public Client addClient(Client client) {
        client.setIdentifiant(null);
        return depotClients.save(client);
    }

    // Modification des coordonnées ; les comptes du client ne sont pas touchés
    @Path("/{id}")
    @PUT
    public Client updateClient(@PathParam("id") Long id, Client client) {
        Client clientExistant = depotClients.findById(id).orElseThrow(NotFoundException::new);
        clientExistant.setNom(client.getNom());
        clientExistant.setPrenom(client.getPrenom());
        clientExistant.setCourriel(client.getCourriel());
        return depotClients.save(clientExistant);
    }

    // Suppression d'un client avec ses comptes et leurs opérations
    @Path("/{id}")
    @DELETE
    public void deleteClient(@PathParam("id") Long id) {
        if (!depotClients.existsById(id)) {
            throw new NotFoundException();
        }
        depotClients.deleteById(id);
    }
}
