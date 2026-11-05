package org.example.banque.repositories;

import org.example.banque.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

/** Accès aux clients : le CRUD de base est fourni par Spring Data. */
public interface ClientRepository extends JpaRepository<Client, Long> {
}
