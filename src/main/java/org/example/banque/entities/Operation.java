package org.example.banque.entities;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.banque.enums.TypeOperation;

import java.util.Date;

/** Dépôt ou retrait réalisé sur un compte. */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long identifiant;

    // Toujours positif : c'est la nature de l'opération qui donne le sens du mouvement
    private double montant;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOperation;

    // Stockée en texte pour rester lisible dans la console H2
    @Enumerated(EnumType.STRING)
    private TypeOperation nature;

    @ManyToOne
    private Compte compte;
}
