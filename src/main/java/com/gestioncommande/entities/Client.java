package com.gestioncommande.entities;

import java.util.Date;

/**
 * Classe représentant l'entité Client dans le système de gestion de commandes.
 * Un client peut être un particulier ou une entreprise et peut passer des commandes.
 * 
 * @author Système de Gestion de Commandes
 * @version 1.0
 * @since 2024
 */
public class Client {
    
    // ========== ATTRIBUTS ==========
    
    /** Identifiant unique du client (généré automatiquement par la base de données) */
    private int id;
    
    /** Nom de famille du client */
    private String nom;
    
    /** Prénom du client */
    private String prenom;
    
    /** Sexe du client (M pour Masculin, F pour Féminin) */
    private String sexe;
    
    /** Type de client (ex: Particulier, Entreprise, Professionnel) */
    private String type;
    
    /** Numéro de téléphone ou contact du client */
    private String contact;
    
    /** Adresse email du client */
    private String email;
    
    /** Adresse postale complète du client */
    private String adresse;
    
    /** Date de création de l'enregistrement client */
    private Date dateCreation;
    
    /** État du client : 1 = actif (peut passer des commandes), 0 = inactif */
    private int etat;

    // ========== CONSTRUCTEURS ==========
    
    /**
     * Constructeur par défaut sans paramètres.
     * Utilisé par les frameworks de mapping objet-relationnel.
     */
    public Client() {}

    /**
     * Constructeur avec paramètres pour créer un nouveau client.
     * Initialise automatiquement la date de création et l'état actif.
     * 
     * @param nom Le nom de famille du client
     * @param prenom Le prénom du client
     * @param sexe Le sexe du client (M ou F)
     * @param type Le type de client (ex: Particulier, Entreprise)
     * @param contact Le numéro de contact du client
     * @param email L'adresse email du client
     * @param adresse L'adresse postale du client
     */
    public Client(String nom, String prenom, String sexe, String type, String contact, 
                  String email, String adresse) {
        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.type = type;
        this.contact = contact;
        this.email = email;
        this.adresse = adresse;
        this.dateCreation = new Date(); // Date actuelle
        this.etat = 1; // Client actif par défaut
    }

    // ========== MÉTHODES D'ACCÈS (GETTERS ET SETTERS) ==========
    
    /**
     * Retourne l'identifiant unique du client.
     * @return L'ID du client
     */
    public int getId() {
        return id;
    }

    /**
     * Définit l'identifiant unique du client.
     * @param id L'ID du client
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retourne le nom de famille du client.
     * @return Le nom du client
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom de famille du client.
     * @param nom Le nom du client
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Retourne le prénom du client.
     * @return Le prénom du client
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Définit le prénom du client.
     * @param prenom Le prénom du client
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Retourne le sexe du client.
     * @return Le sexe du client (M ou F)
     */
    public String getSexe() {
        return sexe;
    }

    /**
     * Définit le sexe du client.
     * @param sexe Le sexe du client (M ou F)
     */
    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    /**
     * Retourne le type de client.
     * @return Le type de client
     */
    public String getType() {
        return type;
    }

    /**
     * Définit le type de client.
     * @param type Le type de client
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Retourne le contact du client.
     * @return Le numéro de contact du client
     */
    public String getContact() {
        return contact;
    }

    /**
     * Définit le contact du client.
     * @param contact Le numéro de contact du client
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * Retourne l'email du client.
     * @return L'adresse email du client
     */
    public String getEmail() {
        return email;
    }

    /**
     * Définit l'email du client.
     * @param email L'adresse email du client
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retourne l'adresse du client.
     * @return L'adresse postale du client
     */
    public String getAdresse() {
        return adresse;
    }

    /**
     * Définit l'adresse du client.
     * @param adresse L'adresse postale du client
     */
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    /**
     * Retourne la date de création du client.
     * @return La date de création
     */
    public Date getDateCreation() {
        return dateCreation;
    }

    /**
     * Définit la date de création du client.
     * @param dateCreation La date de création
     */
    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    /**
     * Retourne l'état du client.
     * @return L'état du client (1 = actif, 0 = inactif)
     */
    public int getEtat() {
        return etat;
    }

    /**
     * Définit l'état du client.
     * @param etat L'état du client (1 = actif, 0 = inactif)
     */
    public void setEtat(int etat) {
        this.etat = etat;
    }

    // ========== MÉTHODES MÉTIER ==========
    
    /**
     * Vérifie si le client est actif.
     * @return true si le client est actif, false sinon
     */
    public boolean isActif() {
        return etat == 1;
    }

    /**
     * Active le client (peut passer des commandes).
     */
    public void activer() {
        this.etat = 1;
    }

    /**
     * Désactive le client (ne peut plus passer de commandes).
     */
    public void desactiver() {
        this.etat = 0;
    }

    // ========== MÉTHODES UTILITAIRES ==========
    
    /**
     * Retourne une représentation textuelle du client.
     * @return Une chaîne contenant toutes les informations du client
     */
    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", sexe='" + sexe + '\'' +
                ", type='" + type + '\'' +
                ", contact='" + contact + '\'' +
                ", email='" + email + '\'' +
                ", adresse='" + adresse + '\'' +
                ", dateCreation=" + dateCreation +
                ", etat=" + (etat == 1 ? "Actif" : "Inactif") +
                '}';
    }
}
