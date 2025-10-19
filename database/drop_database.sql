-- Script de suppression de la base de données gestion_commande
-- ATTENTION : Ce script supprimera définitivement toutes les données !

-- Suppression de la base de données (cela supprime aussi toutes les tables)
DROP DATABASE IF EXISTS gestion_commande;

SELECT 'Base de données gestion_commande supprimée avec succès !' as message;
