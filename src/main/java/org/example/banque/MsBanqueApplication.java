package org.example.banque;

import org.example.banque.entities.Client;
import org.example.banque.entities.Compte;
import org.example.banque.entities.Operation;
import org.example.banque.enums.TypeCompte;
import org.example.banque.enums.TypeOperation;
import org.example.banque.repositories.ClientRepository;
import org.example.banque.repositories.CompteRepository;
import org.example.banque.services.OperationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

/** Point d'entrée du micro-service bancaire. */
@SpringBootApplication
public class MsBanqueApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsBanqueApplication.class, args);
    }

    // Remplit la base au démarrage pour pouvoir tester l'API immédiatement
    @Bean
    CommandLineRunner start(ClientRepository depotClients,
                            CompteRepository depotComptes,
                            OperationService serviceOperations) {
        return args -> {
            Client sara = depotClients.save(new Client(null, "Alaoui", "Sara", "sara.alaoui@example.com", null));
            Client youssef = depotClients.save(new Client(null, "Bennani", "Youssef", "youssef.bennani@example.com", null));

            Compte epargneSara = depotComptes.save(new Compte(null, Math.random() * 9000, new Date(), TypeCompte.EPARGNE, sara, null));
            depotComptes.save(new Compte(null, Math.random() * 9000, new Date(), TypeCompte.COURANT, sara, null));
            depotComptes.save(new Compte(null, Math.random() * 9000, new Date(), TypeCompte.EPARGNE, youssef, null));

            // Le dépôt précède le retrait pour que le solde soit toujours suffisant
            serviceOperations.enregistrer(epargneSara.getIdentifiant(), new Operation(null, 500, null, TypeOperation.DEPOT, null));
            serviceOperations.enregistrer(epargneSara.getIdentifiant(), new Operation(null, 120, null, TypeOperation.RETRAIT, null));

            // Vérification visuelle dans la console
            depotComptes.findAll().forEach(compte -> System.out.println(compte.toString()));
        };
    }
}
