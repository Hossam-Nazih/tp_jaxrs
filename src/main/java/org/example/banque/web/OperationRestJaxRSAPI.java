package org.example.banque.web;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.example.banque.entities.Operation;
import org.example.banque.repositories.CompteRepository;
import org.example.banque.repositories.OperationRepository;
import org.example.banque.services.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Endpoints des opérations bancaires.
 * Pas de PUT ni de DELETE : modifier l'historique rendrait le solde du compte incohérent.
 */
@Component
@Path("/banque")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class OperationRestJaxRSAPI {

    @Autowired
    private OperationRepository depotOperations;

    @Autowired
    private CompteRepository depotComptes;

    @Autowired
    private OperationService serviceOperations;

    // Toutes les opérations de la banque
    @Path("/operations")
    @GET
    public List<Operation> getOperations() {
        return depotOperations.findAll();
    }

    // Détail d'une opération, ou 404 si elle n'existe pas
    @Path("/operations/{id}")
    @GET
    public Operation getOperation(@PathParam("id") Long id) {
        return depotOperations.findById(id).orElseThrow(NotFoundException::new);
    }

    // Historique d'un compte, de la plus récente à la plus ancienne
    @Path("/comptes/{id}/operations")
    @GET
    public List<Operation> getOperationsDuCompte(@PathParam("id") Long id) {
        if (!depotComptes.existsById(id)) {
            throw new NotFoundException();
        }
        return depotOperations.findByCompteIdentifiantOrderByDateOperationDesc(id);
    }

    // Dépôt ou retrait : met aussi à jour le solde du compte
    @Path("/comptes/{id}/operations")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Operation addOperation(@PathParam("id") Long id, Operation operation) {
        return serviceOperations.enregistrer(id, operation);
    }
}
