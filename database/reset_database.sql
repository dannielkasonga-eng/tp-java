-- Script de réinitialisation de la base de données gestion_commande
-- Ce script supprime toutes les données et recrée les tables vides

USE gestion_commande;

-- Désactiver temporairement les vérifications de clés étrangères
SET FOREIGN_KEY_CHECKS = 0;

-- Supprimer toutes les tables
DROP TABLE IF EXISTS commandes;
DROP TABLE IF EXISTS articles;
DROP TABLE IF EXISTS clients;

-- Réactiver les vérifications de clés étrangères
SET FOREIGN_KEY_CHECKS = 1;

-- Recréer les tables
-- Table des clients
CREATE TABLE clients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    sexe ENUM('M', 'F') NOT NULL,
    type VARCHAR(30) NOT NULL,
    contact VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    adresse TEXT,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    etat TINYINT(1) DEFAULT 1 COMMENT '1=actif, 0=inactif',
    INDEX idx_nom_prenom (nom, prenom),
    INDEX idx_contact (contact),
    INDEX idx_etat (etat)
);

-- Table des articles
CREATE TABLE articles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    designation VARCHAR(100) NOT NULL,
    categorie VARCHAR(50) NOT NULL,
    prix DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    stock_minimum INT NOT NULL DEFAULT 5,
    description TEXT,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_modification TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    etat TINYINT(1) DEFAULT 1 COMMENT '1=actif, 0=inactif',
    INDEX idx_designation (designation),
    INDEX idx_categorie (categorie),
    INDEX idx_stock (stock),
    INDEX idx_etat (etat),
    INDEX idx_stock_faible (stock, stock_minimum)
);

-- Table des commandes
CREATE TABLE commandes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    client_id INT NOT NULL,
    article_id INT NOT NULL,
    quantite INT NOT NULL,
    prix_unitaire DECIMAL(10,2) NOT NULL,
    montant_total DECIMAL(12,2) NOT NULL,
    date_commande TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    type_commande ENUM('en_cours', 'validee', 'annulee') DEFAULT 'en_cours',
    statut ENUM('en_attente', 'traitee', 'livree', 'annulee') DEFAULT 'en_attente',
    observations TEXT,
    date_validation TIMESTAMP NULL,
    INDEX idx_client (client_id),
    INDEX idx_article (article_id),
    INDEX idx_date_commande (date_commande),
    INDEX idx_type_commande (type_commande),
    INDEX idx_statut (statut),
    FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE RESTRICT,
    FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE RESTRICT
);

SELECT 'Base de données gestion_commande réinitialisée avec succès !' as message;
SELECT 'Toutes les tables ont été supprimées et recréées vides.' as info;
