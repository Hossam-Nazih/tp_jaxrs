package org.example.banque.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.banque.enums.TypeCompte;

import java.util.Date;
import java.util.List;

/** Compte bancaire stocké en base et échangé en JSON ou en XML. */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement // <compte> devient la balise racine du document XML
@XmlAccessorType(XmlAccessType.FIELD) // JAXB lit les champs, ce qui rend @XmlTransient effectif
public class Compte {

    // Clé primaire attribuée par la base
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long identifiant;

    // Argent actuellement disponible sur le compte
    private double montantDisponible;

    // Seule la date est conservée, sans l'heure
    @Temporal(TemporalType.DATE)
    private Date dateOuverture;

    // Stocké en base par sa position dans l'enum : 0 = COURANT, 1 = EPARGNE
    @Enumerated(EnumType.ORDINAL)
    private TypeCompte categorie;

    // Propriétaire du compte, facultatif
    @ManyToOne
    private Client titulaire;

    // Historique masqué dans les réponses ; il se consulte via /comptes/{id}/operations
    @OneToMany(mappedBy = "compte", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @XmlTransient
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Operation> operations;
}
