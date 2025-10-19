package com.gestioncommande.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe utilitaire pour gérer la connexion à la base de données MySQL.
 * Implémente le pattern Singleton pour assurer une seule connexion à la base.
 * 
 * @author Système de Gestion de Commandes
 * @version 1.0
 * @since 2024
 */
public class DatabaseConnection {
    
    // ========== PARAMÈTRES DE CONNEXION ==========
    
    /** URL de connexion à la base de données MySQL */
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_commande";
    
    /** Nom d'utilisateur MySQL */
    private static final String USERNAME = "root";
    
    /** Mot de passe MySQL */
    private static final String PASSWORD = "MYSQLNEWDEV@882004";
    
    /** Instance unique de connexion (pattern Singleton) */
    private static Connection connection = null;

    // ========== MÉTHODES DE CONNEXION ==========
    
    /**
     * Établit la connexion à la base de données MySQL.
     * Utilise le pattern Singleton pour réutiliser la même connexion.
     * 
     * @return L'objet Connection à la base de données
     * @throws SQLException En cas d'erreur de connexion ou de driver non trouvé
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Vérifier si la connexion n'existe pas ou est fermée
            if (connection == null || connection.isClosed()) {
                // Charger le driver MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");
                // Établir la connexion
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Connexion à la base de données établie avec succès !");
            }
            return connection;
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL non trouvé : " + e.getMessage());
        }
    }

    /**
     * Ferme la connexion à la base de données.
     * Vérifie que la connexion existe et n'est pas déjà fermée avant de la fermer.
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexion à la base de données fermée !");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
        }
    }

    /**
     * Teste la connexion à la base de données.
     * Utile pour vérifier la disponibilité de la base avant d'utiliser l'application.
     * 
     * @return true si la connexion fonctionne, false sinon
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Erreur de connexion : " + e.getMessage());
            return false;
        }
    }
}
