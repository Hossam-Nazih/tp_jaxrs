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

/** Endpoints CRUD des comptes ; le format JSON ou XML est choisi par le client via ses en-têtes HTTP. */
@Component
@Path("/banque")
public class CompteRestJaxRSAPI {

    @Autowired
    private CompteRepository depotComptes;

    @Autowired
    private ClientRepository depotClients;

    // Liste complète des comptes
    @Path("/comptes")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Compte> getComptes() {
        return depotComptes.findAll();
    }

    // Détail d'un compte, ou 404 si l'identifiant n'existe pas
    @Path("/comptes/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Compte getCompte(@PathParam("id") Long id) {
        return depotComptes.findById(id).orElseThrow(NotFoundException::new);
    }

    // Ouverture d'un compte ; l'identifiant envoyé est ignoré pour ne jamais écraser un compte existant
    @Path("/comptes")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Compte addCompte(Compte compte) {
        compte.setIdentifiant(null);
        compte.setTitulaire(chargerTitulaire(compte.getTitulaire()));
        return depotComptes.save(compte);
    }

    // Modification d'un compte ; sans titulaire dans la requête, le titulaire actuel est conservé
    @Path("/comptes/{id}")
    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Compte updateCompte(@PathParam("id") Long id, Compte compte) {
        Compte compteExistant = depotComptes.findById(id).orElseThrow(NotFoundException::new);
        compteExistant.setMontantDisponible(compte.getMontantDisponible());
        compteExistant.setDateOuverture(compte.getDateOuverture());
        compteExistant.setCategorie(compte.getCategorie());
        if (compte.getTitulaire() != null) {
            compteExistant.setTitulaire(chargerTitulaire(compte.getTitulaire()));
        }
        return depotComptes.save(compteExistant);
    }

    // Fermeture d'un compte et suppression de son historique d'opérations
    @Path("/comptes/{id}")
    @DELETE
    public void deleteCompte(@PathParam("id") Long id) {
        if (!depotComptes.existsById(id)) {
            throw new NotFoundException();
        }
        depotComptes.deleteById(id);
    }

    // La requête ne porte que l'identifiant du titulaire : on recharge le client complet depuis la base
    private Client chargerTitulaire(Client reference) {
        if (reference == null) {
            return null;
        }
        if (reference.getIdentifiant() == null) {
            throw new BadRequestException("L'identifiant du titulaire est obligatoire");
        }
        return depotClients.findById(reference.getIdentifiant())
                .orElseThrow(() -> new NotFoundException("Titulaire introuvable"));
    }
}
