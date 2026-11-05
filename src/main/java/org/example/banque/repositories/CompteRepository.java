package org.example.banque.repositories;

import org.example.banque.entities.Compte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** Accès aux comptes : save, findAll, deleteById, etc. sont générés par Spring Data. */
public interface CompteRepository extends JpaRepository<Compte, Long> {

    // Requête déduite du nom de la méthode : tous les comptes d'un client donné
    List<Compte> findByTitulaireIdentifiant(Long identifiantClient);
}
