package com.gestioncommande;

import com.gestioncommande.dao.DatabaseConnection;
import com.gestioncommande.service.ClientService;
import com.gestioncommande.service.ArticleService;
import com.gestioncommande.service.CommandeService;

import java.sql.SQLException;
import java.util.Scanner;

/**
 * Classe principale de l'application de gestion de commandes.
 * Point d'entrée de l'application qui gère l'initialisation et le menu principal.
 * 
 * @author Système de Gestion de Commandes
 * @version 1.0
 * @since 2024
 */
public class Main {
    
    // ========== ATTRIBUTS STATIQUES ==========
    
    /** Service de gestion des clients */
    private static ClientService clientService;
    
    /** Service de gestion des articles */
    private static ArticleService articleService;
    
    /** Service de gestion des commandes */
    private static CommandeService commandeService;
    
    /** Scanner pour la saisie utilisateur */
    private static Scanner scanner;

    /**
     * Point d'entrée principal de l'application.
     * Initialise les services, teste la connexion à la base de données et lance le menu principal.
     * 
     * @param args Arguments de la ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        System.out.println("=== APPLICATION DE GESTION DE COMMANDES ===");
        System.out.println("Initialisation en cours...");
        
        try {
            // Test de la connexion à la base de données
            if (!DatabaseConnection.testConnection()) {
                System.err.println("Erreur : Impossible de se connecter à la base de données !");
                System.err.println("Vérifiez que MySQL est démarré et que la base 'gestion_commande' existe.");
                return;
            }
            
            // Initialisation des services métier
            clientService = new ClientService();
            articleService = new ArticleService();
            commandeService = new CommandeService();
            scanner = new Scanner(System.in);
            
            System.out.println("Application initialisée avec succès !");
            
            // Lancement du menu principal
            afficherMenuPrincipal();
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'initialisation de l'application : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur inattendue : " + e.getMessage());
        } finally {
            // Nettoyage des ressources
            DatabaseConnection.closeConnection();
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    // ========== MÉTHODES D'INTERFACE UTILISATEUR ==========
    
    /**
     * Affiche et gère le menu principal de l'application.
     * Boucle infinie qui permet à l'utilisateur de naviguer entre les différents modules.
     */
    private static void afficherMenuPrincipal() {
        boolean continuer = true;
        
        while (continuer) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("           MENU PRINCIPAL");
            System.out.println("=".repeat(50));
            System.out.println("1. Gestion des Clients");
            System.out.println("2. Gestion des Articles");
            System.out.println("3. Gestion des Commandes");
            System.out.println("4. Test de connexion à la base de données");
            System.out.println("5. Quitter l'application");
            System.out.println("=".repeat(50));
            System.out.print("Choisissez une option : ");
            
            try {
                int choix = scanner.nextInt();
                scanner.nextLine(); // Consommer la ligne vide
                
                switch (choix) {
                    case 1:
                        clientService.afficherMenu();
                        break;
                    case 2:
                        articleService.afficherMenu();
                        break;
                    case 3:
                        commandeService.afficherMenu();
                        break;
                    case 4:
                        testerConnexion();
                        break;
                    case 5:
                        continuer = false;
                        System.out.println("Merci d'avoir utilisé l'application de gestion de commandes !");
                        break;
                    default:
                        System.out.println("Option invalide ! Veuillez choisir entre 1 et 5.");
                }
            } catch (Exception e) {
                System.err.println("Erreur de saisie : " + e.getMessage());
                scanner.nextLine(); // Nettoyer le buffer
            }
        }
    }

    /**
     * Teste la connexion à la base de données
     */
    private static void testerConnexion() {
        System.out.println("\n--- TEST DE CONNEXION ---");
        
        if (DatabaseConnection.testConnection()) {
            System.out.println("✓ Connexion à la base de données réussie !");
            System.out.println("Base de données : gestion_commande");
            System.out.println("Serveur : localhost:3306");
        } else {
            System.out.println("✗ Échec de la connexion à la base de données !");
            System.out.println("Vérifiez que :");
            System.out.println("- MySQL est démarré");
            System.out.println("- La base de données 'gestion_commande' existe");
            System.out.println("- Les identifiants de connexion sont corrects");
        }
    }

    /**
     * Affiche les informations de l'application
     */
    public static void afficherInformations() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("    APPLICATION DE GESTION DE COMMANDES");
        System.out.println("=".repeat(60));
        System.out.println("Version : 1.0");
        System.out.println("Développé en : Java");
        System.out.println("Base de données : MySQL");
        System.out.println("Architecture : MVC avec DAO");
        System.out.println("=".repeat(60));
        System.out.println("\nFonctionnalités :");
        System.out.println("• Gestion des clients (ajout, modification, activation/désactivation)");
        System.out.println("• Gestion des articles (ajout, modification, gestion du stock)");
        System.out.println("• Gestion des commandes (effectuer, valider, annuler)");
        System.out.println("• Statistiques et rapports");
        System.out.println("=".repeat(60));
    }
}
