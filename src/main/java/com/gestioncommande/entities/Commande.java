package com.gestioncommande.entities;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Classe représentant l'entité Commande dans le système de gestion de commandes.
 * Une commande lie un client à un article avec une quantité et un prix.
 * Elle peut être en cours, validée ou annulée.
 * 
 * @author Système de Gestion de Commandes
 * @version 1.0
 * @since 2024
 */
public class Commande {
    
    // ========== ATTRIBUTS ==========
    
    /** Identifiant unique de la commande (généré automatiquement par la base de données) */
    private int id;
    
    /** Identifiant du client qui passe la commande */
    private int clientId;
    
    /** Identifiant de l'article commandé */
    private int articleId;
    
    /** Quantité d'articles commandés */
    private int quantite;
    
    /** Prix unitaire de l'article au moment de la commande */
    private BigDecimal prixUnitaire;
    
    /** Montant total de la commande (quantité × prix unitaire) */
    private BigDecimal montantTotal;
    
    /** Date de création de la commande */
    private Date dateCommande;
    
    /** Type de commande : "en_cours", "validee", "annulee" */
    private String typeCommande;
    
    /** Statut de la commande : "en_attente", "traitee", "livree", "annulee" */
    private String statut;
    
    /** Observations ou commentaires sur la commande */
    private String observations;
    
    /** Date de validation de la commande (null si non validée) */
    private Date dateValidation;

    // Constructeurs
    public Commande() {}

    public Commande(int clientId, int articleId, int quantite, BigDecimal prixUnitaire) {
        this.clientId = clientId;
        this.articleId = articleId;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.montantTotal = prixUnitaire.multiply(BigDecimal.valueOf(quantite));
        this.dateCommande = new Date();
        this.typeCommande = "en_cours";
        this.statut = "en_attente";
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
        this.montantTotal = this.prixUnitaire.multiply(BigDecimal.valueOf(quantite));
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
        this.montantTotal = prixUnitaire.multiply(BigDecimal.valueOf(this.quantite));
    }

    public BigDecimal getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(BigDecimal montantTotal) {
        this.montantTotal = montantTotal;
    }

    public Date getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public String getTypeCommande() {
        return typeCommande;
    }

    public void setTypeCommande(String typeCommande) {
        this.typeCommande = typeCommande;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Date getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(Date dateValidation) {
        this.dateValidation = dateValidation;
    }

    // ========== MÉTHODES MÉTIER ==========
    
    /**
     * Marque la commande comme étant en cours.
     * Utilisé lors de la création d'une nouvelle commande.
     */
    public void effectuer() {
        this.typeCommande = "en_cours";
        this.statut = "en_attente";
    }

    /**
     * Valide la commande et met à jour son statut.
     * Définit automatiquement la date de validation.
     */
    public void valider() {
        this.typeCommande = "validee";
        this.statut = "traitee";
        this.dateValidation = new Date(); // Date actuelle
    }

    /**
     * Annule la commande.
     * Change le type et le statut en "annulee".
     */
    public void annuler() {
        this.typeCommande = "annulee";
        this.statut = "annulee";
    }

    /**
     * Vérifie si la commande est validée.
     * @return true si la commande est validée, false sinon
     */
    public boolean isValidee() {
        return "validee".equals(this.typeCommande);
    }

    /**
     * Vérifie si la commande est en cours.
     * @return true si la commande est en cours, false sinon
     */
    public boolean isEnCours() {
        return "en_cours".equals(this.typeCommande);
    }

    /**
     * Vérifie si la commande est annulée.
     * @return true si la commande est annulée, false sinon
     */
    public boolean isAnnulee() {
        return "annulee".equals(this.typeCommande);
    }

    @Override
    public String toString() {
        return "Commande{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", articleId=" + articleId +
                ", quantite=" + quantite +
                ", prixUnitaire=" + prixUnitaire +
                ", montantTotal=" + montantTotal +
                ", dateCommande=" + dateCommande +
                ", typeCommande='" + typeCommande + '\'' +
                ", statut='" + statut + '\'' +
                ", observations='" + observations + '\'' +
                ", dateValidation=" + dateValidation +
                '}';
    }
}
