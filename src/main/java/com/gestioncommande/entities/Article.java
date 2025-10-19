package com.gestioncommande.entities;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Classe représentant l'entité Article dans le système de gestion de commandes.
 * Un article peut être commandé par les clients et son stock est géré automatiquement.
 * 
 * @author Système de Gestion de Commandes
 * @version 1.0
 * @since 2024
 */
public class Article {
    
    // ========== ATTRIBUTS ==========
    
    /** Identifiant unique de l'article (généré automatiquement par la base de données) */
    private int id;
    
    /** Désignation/nom de l'article */
    private String designation;
    
    /** Catégorie de l'article (ex: Informatique, Mobilier, Livre) */
    private String categorie;
    
    /** Prix unitaire de l'article en euros */
    private BigDecimal prix;
    
    /** Quantité disponible en stock */
    private int stock;
    
    /** Seuil minimum de stock pour les alertes */
    private int stockMinimum;
    
    /** Description détaillée de l'article */
    private String description;
    
    /** Date de création de l'enregistrement article */
    private Date dateCreation;
    
    /** Date de dernière modification de l'article */
    private Date dateModification;
    
    /** État de l'article : 1 = actif (peut être commandé), 0 = inactif */
    private int etat;

    // ========== CONSTRUCTEURS ==========
    
    /**
     * Constructeur par défaut sans paramètres.
     * Utilisé par les frameworks de mapping objet-relationnel.
     */
    public Article() {}

    /**
     * Constructeur avec paramètres pour créer un nouvel article.
     * Initialise automatiquement les dates de création/modification et l'état actif.
     * 
     * @param designation La désignation de l'article
     * @param categorie La catégorie de l'article
     * @param prix Le prix unitaire de l'article
     * @param stock La quantité initiale en stock
     * @param stockMinimum Le seuil minimum de stock
     * @param description La description de l'article
     */
    public Article(String designation, String categorie, BigDecimal prix, int stock, 
                   int stockMinimum, String description) {
        this.designation = designation;
        this.categorie = categorie;
        this.prix = prix;
        this.stock = stock;
        this.stockMinimum = stockMinimum;
        this.description = description;
        this.dateCreation = new Date(); // Date actuelle
        this.dateModification = new Date(); // Date actuelle
        this.etat = 1; // Article actif par défaut
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
        this.dateModification = new Date();
    }

    public int getStockMinimum() {
        return stockMinimum;
    }

    public void setStockMinimum(int stockMinimum) {
        this.stockMinimum = stockMinimum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDateModification() {
        return dateModification;
    }

    public void setDateModification(Date dateModification) {
        this.dateModification = dateModification;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public boolean isActif() {
        return etat == 1;
    }

    public void activer() {
        this.etat = 1;
        this.dateModification = new Date();
    }

    public void desactiver() {
        this.etat = 0;
        this.dateModification = new Date();
    }

    // ========== MÉTHODES MÉTIER ==========
    
    /**
     * Vérifie si le stock est en quantité faible (inférieur ou égal au stock minimum).
     * @return true si le stock est faible, false sinon
     */
    public boolean isStockFaible() {
        return stock <= stockMinimum;
    }

    /**
     * Ajoute une quantité au stock de l'article.
     * Met à jour automatiquement la date de modification.
     * 
     * @param quantite La quantité à ajouter au stock
     */
    public void ajouterStock(int quantite) {
        this.stock += quantite;
        this.dateModification = new Date(); // Mise à jour de la date de modification
    }

    /**
     * Retire une quantité du stock de l'article.
     * Vérifie d'abord si le stock est suffisant.
     * 
     * @param quantite La quantité à retirer du stock
     * @return true si le retrait a été effectué, false si stock insuffisant
     */
    public boolean retirerStock(int quantite) {
        if (this.stock >= quantite) {
            this.stock -= quantite;
            this.dateModification = new Date(); // Mise à jour de la date de modification
            return true;
        }
        return false; // Stock insuffisant
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", designation='" + designation + '\'' +
                ", categorie='" + categorie + '\'' +
                ", prix=" + prix +
                ", stock=" + stock +
                ", stockMinimum=" + stockMinimum +
                ", description='" + description + '\'' +
                ", dateCreation=" + dateCreation +
                ", dateModification=" + dateModification +
                ", etat=" + (etat == 1 ? "Actif" : "Inactif") +
                '}';
    }
}
