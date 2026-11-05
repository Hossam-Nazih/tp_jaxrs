package org.example.banque.services;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.example.banque.entities.Compte;
import org.example.banque.entities.Operation;
import org.example.banque.enums.TypeOperation;
import org.example.banque.repositories.CompteRepository;
import org.example.banque.repositories.OperationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/** Règles métier appliquées lors d'un dépôt ou d'un retrait. */
@Service
public class OperationService {

    private final CompteRepository depotComptes;
    private final OperationRepository depotOperations;

    public OperationService(CompteRepository depotComptes, OperationRepository depotOperations) {
        this.depotComptes = depotComptes;
        this.depotOperations = depotOperations;
    }

    // Transaction unique : le solde et l'historique sont modifiés ensemble, ou pas du tout
    @Transactional
    public Operation enregistrer(Long identifiantCompte, Operation demande) {
        Compte compte = depotComptes.findById(identifiantCompte)
                .orElseThrow(() -> new NotFoundException("Compte introuvable"));

        if (demande.getNature() == null) {
            throw new BadRequestException("La nature de l'opération est obligatoire");
        }
        if (demande.getMontant() <= 0) {
            throw new BadRequestException("Le montant doit être strictement positif");
        }

        // Un retrait ne peut pas rendre le compte débiteur
        boolean estDepot = demande.getNature() == TypeOperation.DEPOT;
        if (!estDepot && demande.getMontant() > compte.getMontantDisponible()) {
            throw new BadRequestException("Solde insuffisant");
        }
        double variation = estDepot ? demande.getMontant() : -demande.getMontant();
        compte.setMontantDisponible(compte.getMontantDisponible() + variation);

        // L'identifiant et la date sont fixés par le serveur, quelle que soit la requête reçue
        demande.setIdentifiant(null);
        demande.setDateOperation(new Date());
        demande.setCompte(compte);
        return depotOperations.save(demande);
    }
}
