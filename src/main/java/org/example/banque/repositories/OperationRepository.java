package org.example.banque.repositories;

import org.example.banque.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** Accès à l'historique des opérations. */
public interface OperationRepository extends JpaRepository<Operation, Long> {

    // Historique d'un compte, de la plus récente à la plus ancienne
    List<Operation> findByCompteIdentifiantOrderByDateOperationDesc(Long identifiantCompte);
}
