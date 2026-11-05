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

import java.util.List;

/** Personne titulaire d'un ou plusieurs comptes bancaires. */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD) // JAXB lit les champs, ce qui rend @XmlTransient effectif
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long identifiant;

    private String nom;

    private String prenom;

    private String courriel;

    // Masqué dans les réponses : le compte affiche déjà son titulaire, on évite ainsi une boucle infinie.
    // Supprimer un client supprime aussi ses comptes.
    @OneToMany(mappedBy = "titulaire", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @XmlTransient
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Compte> comptes;
}
