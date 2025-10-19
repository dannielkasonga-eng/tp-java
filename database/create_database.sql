-- Script de création de la base de données gestion_commande
-- Exécuter ce script avec MySQL pour créer la base de données et les tables

-- Création de la base de données

CREATE DATABASE IF NOT EXISTS gestion_commande
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Utilisation de la base de données
USE gestion_commande;

-- Table des clients

CREATE TABLE IF NOT EXISTS clients (
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
CREATE TABLE IF NOT EXISTS articles (
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
CREATE TABLE IF NOT EXISTS commandes (
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

-- Insertion de données d'exemple
-- Clients d'exemple
INSERT INTO clients (nom, prenom, sexe, type, contact, email, adresse) VALUES
('Dupont', 'Jean', 'M', 'Particulier', '0123456789', 'jean.dupont@email.com', '123 Rue de la Paix, Paris'),
('Martin', 'Marie', 'F', 'Entreprise', '0987654321', 'marie.martin@company.com', '456 Avenue des Champs, Lyon'),
('Bernard', 'Pierre', 'M', 'Particulier', '0555123456', 'pierre.bernard@email.com', '789 Boulevard Saint-Germain, Marseille'),
('Dubois', 'Sophie', 'F', 'Entreprise', '0555987654', 'sophie.dubois@business.com', '321 Rue de Rivoli, Toulouse');

-- Articles d'exemple
INSERT INTO articles (designation, categorie, prix, stock, stock_minimum, description) VALUES
('Ordinateur Portable HP', 'Informatique', 899.99, 15, 3, 'Ordinateur portable HP 15 pouces, 8GB RAM, 256GB SSD'),
('Souris Sans Fil Logitech', 'Informatique', 29.99, 50, 10, 'Souris sans fil ergonomique avec connectivité Bluetooth'),
('Chaise de Bureau Ergonomique', 'Mobilier', 199.99, 8, 2, 'Chaise de bureau avec support lombaire et accoudoirs réglables'),
('Livre "Java pour débutants"', 'Livre', 35.99, 25, 5, 'Guide complet pour apprendre Java avec des exemples pratiques'),
('Cafetière Automatique', 'Électroménager', 89.99, 12, 3, 'Cafetière automatique avec broyeur intégré'),
('Smartphone Samsung Galaxy', 'Téléphonie', 699.99, 6, 2, 'Smartphone Android avec écran 6.1 pouces et 128GB de stockage');

-- Commandes d'exemple
INSERT INTO commandes (client_id, article_id, quantite, prix_unitaire, montant_total, type_commande, statut, observations) VALUES
(1, 1, 1, 899.99, 899.99, 'en_cours', 'en_attente', 'Commande pour usage personnel'),
(2, 4, 10, 35.99, 359.90, 'validee', 'traitee', 'Commande pour formation entreprise'),
(3, 3, 2, 199.99, 399.98, 'en_cours', 'en_attente', 'Mobilier pour nouveau bureau'),
(4, 5, 1, 89.99, 89.99, 'validee', 'traitee', 'Pour la salle de pause');

-- Mise à jour des dates de validation pour les commandes validées
UPDATE commandes SET date_validation = DATE_SUB(NOW(), INTERVAL 2 DAY) WHERE type_commande = 'validee';

-- Affichage des informations de création
SELECT 'Base de données gestion_commande créée avec succès !' as message;
SELECT COUNT(*) as nb_clients FROM clients;
SELECT COUNT(*) as nb_articles FROM articles;
SELECT COUNT(*) as nb_commandes FROM commandes;
