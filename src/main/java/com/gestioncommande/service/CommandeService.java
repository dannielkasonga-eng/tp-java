package com.gestioncommande.service;

import com.gestioncommande.dao.CommandeDAO;
import com.gestioncommande.entities.Commande;
import com.gestioncommande.entities.Client;
import com.gestioncommande.entities.Article;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Service pour la gestion des commandes
 */
public class CommandeService {
    private CommandeDAO commandeDAO;
    private ClientService clientService;
    private ArticleService articleService;
    private Scanner scanner;

    public CommandeService() throws SQLException {
        this.commandeDAO = new CommandeDAO();
        this.clientService = new ClientService();
        this.articleService = new ArticleService();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Affiche le menu de gestion des commandes
     */
    public void afficherMenu() {
        boolean continuer = true;
        
        while (continuer) {
            System.out.println("\n=== GESTION DES COMMANDES ===");
            System.out.println("1. Effectuer une commande");
            System.out.println("2. Valider une commande");
            System.out.println("3. Annuler une commande");
            System.out.println("4. Modifier une commande");
            System.out.println("5. Lister toutes les commandes");
            System.out.println("6. Lister les commandes en attente");
            System.out.println("7. Lister les commandes validées");
            System.out.println("8. Lister les commandes d'un client");
            System.out.println("9. Consulter une commande");
            System.out.println("10. Statistiques des commandes");
            System.out.println("11. Retour au menu principal");
            System.out.print("Choisissez une option : ");
            
            int choix = scanner.nextInt();
            scanner.nextLine(); // Consommer la ligne vide
            
            switch (choix) {
                case 1:
                    effectuerCommande();
                    break;
                case 2:
                    validerCommande();
                    break;
                case 3:
                    annulerCommande();
                    break;
                case 4:
                    modifierCommande();
                    break;
                case 5:
                    listerToutesCommandes();
                    break;
                case 6:
                    listerCommandesEnAttente();
                    break;
                case 7:
                    listerCommandesValidees();
                    break;
                case 8:
                    listerCommandesClient();
                    break;
                case 9:
                    consulterCommande();
                    break;
                case 10:
                    afficherStatistiques();
                    break;
                case 11:
                    continuer = false;
                    break;
                default:
                    System.out.println("Option invalide !");
            }
        }
    }

    /**
     * Effectue une nouvelle commande
     */
    private void effectuerCommande() {
        System.out.println("\n--- EFFECTUER UNE COMMANDE ---");
        
        // Sélection du client
        System.out.println("Sélection du client :");
        List<Client> clients = clientService.obtenirClientsActifs();
        if (clients.isEmpty()) {
            System.out.println("Aucun client actif disponible !");
            return;
        }
        
        System.out.printf("%-5s %-20s %-20s %-15s%n", "ID", "NOM", "PRÉNOM", "TYPE");
        System.out.println("-".repeat(60));
        for (Client client : clients) {
            System.out.printf("%-5d %-20s %-20s %-15s%n",
                            client.getId(), client.getNom(), client.getPrenom(), client.getType());
        }
        
        System.out.print("ID du client : ");
        int clientId = scanner.nextInt();
        scanner.nextLine();
        
        Client client = clientService.trouverClientParId(clientId);
        if (client == null || !client.isActif()) {
            System.out.println("Client invalide ou inactif !");
            return;
        }
        
        // Sélection de l'article
        System.out.println("\nSélection de l'article :");
        List<Article> articles = articleService.obtenirArticlesActifs();
        if (articles.isEmpty()) {
            System.out.println("Aucun article actif disponible !");
            return;
        }
        
        System.out.printf("%-5s %-30s %-20s %-10s %-8s%n", "ID", "DÉSIGNATION", "CATÉGORIE", "PRIX", "STOCK");
        System.out.println("-".repeat(75));
        for (Article article : articles) {
            System.out.printf("%-5d %-30s %-20s %-10s %-8d%n",
                            article.getId(), article.getDesignation(), article.getCategorie(),
                            article.getPrix(), article.getStock());
        }
        
        System.out.print("ID de l'article : ");
        int articleId = scanner.nextInt();
        scanner.nextLine();
        
        Article article = articleService.trouverArticleParId(articleId);
        if (article == null || !article.isActif()) {
            System.out.println("Article invalide ou inactif !");
            return;
        }
        
        System.out.println("Stock disponible : " + article.getStock());
        System.out.print("Quantité à commander : ");
        int quantite = scanner.nextInt();
        scanner.nextLine();
        
        if (quantite <= 0) {
            System.out.println("La quantité doit être positive !");
            return;
        }
        
        if (quantite > article.getStock()) {
            System.out.println("Stock insuffisant ! Stock disponible : " + article.getStock());
            return;
        }
        
        System.out.print("Observations (optionnel) : ");
        String observations = scanner.nextLine();
        
        // Création de la commande
        Commande commande = new Commande(clientId, articleId, quantite, article.getPrix());
        commande.setObservations(observations);
        commande.effectuer();
        
        if (commandeDAO.ajouter(commande)) {
            System.out.println("Commande effectuée avec succès ! ID : " + commande.getId());
            System.out.println("Montant total : " + commande.getMontantTotal());
        } else {
            System.out.println("Erreur lors de l'effectuation de la commande !");
        }
    }

    /**
     * Valide une commande
     */
    private void validerCommande() {
        System.out.println("\n--- VALIDATION D'UNE COMMANDE ---");
        
        System.out.print("ID de la commande à valider : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Commande commande = commandeDAO.trouverParId(id);
        if (commande == null) {
            System.out.println("Commande non trouvée !");
            return;
        }
        
        if (commande.isValidee()) {
            System.out.println("Cette commande est déjà validée !");
            return;
        }
        
        if (commande.isAnnulee()) {
            System.out.println("Cette commande est annulée et ne peut pas être validée !");
            return;
        }
        
        // Vérifier le stock avant validation
        Article article = articleService.trouverArticleParId(commande.getArticleId());
        if (article == null || article.getStock() < commande.getQuantite()) {
            System.out.println("Stock insuffisant pour valider cette commande !");
            return;
        }
        
        System.out.println("Commande à valider :");
        System.out.println("Client : " + commande.getClientId());
        System.out.println("Article : " + commande.getArticleId());
        System.out.println("Quantité : " + commande.getQuantite());
        System.out.println("Montant : " + commande.getMontantTotal());
        
        System.out.print("Confirmer la validation (oui/non) ? ");
        String confirmation = scanner.nextLine();
        
        if ("oui".equalsIgnoreCase(confirmation)) {
            if (commandeDAO.valider(id)) {
                // Mettre à jour le stock
                articleService.verifierEtMettreAJourStock(commande.getArticleId(), commande.getQuantite());
                System.out.println("Commande validée avec succès !");
            } else {
                System.out.println("Erreur lors de la validation de la commande !");
            }
        } else {
            System.out.println("Validation annulée.");
        }
    }

    /**
     * Annule une commande
     */
    private void annulerCommande() {
        System.out.println("\n--- ANNULATION D'UNE COMMANDE ---");
        
        System.out.print("ID de la commande à annuler : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Commande commande = commandeDAO.trouverParId(id);
        if (commande == null) {
            System.out.println("Commande non trouvée !");
            return;
        }
        
        if (commande.isAnnulee()) {
            System.out.println("Cette commande est déjà annulée !");
            return;
        }
        
        System.out.print("Raison de l'annulation : ");
        String raison = scanner.nextLine();
        
        if (commandeDAO.annuler(id)) {
            System.out.println("Commande annulée avec succès !");
        } else {
            System.out.println("Erreur lors de l'annulation de la commande !");
        }
    }

    /**
     * Modifie une commande existante
     */
    private void modifierCommande() {
        System.out.println("\n--- MODIFICATION D'UNE COMMANDE ---");
        
        System.out.print("ID de la commande à modifier : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Commande commande = commandeDAO.trouverParId(id);
        if (commande == null) {
            System.out.println("Commande non trouvée !");
            return;
        }
        
        if (commande.isValidee()) {
            System.out.println("Impossible de modifier une commande validée !");
            return;
        }
        
        System.out.println("Commande actuelle : " + commande);
        System.out.println("\nNouvelles informations :");
        
        System.out.print("Quantité (" + commande.getQuantite() + ") : ");
        String quantiteStr = scanner.nextLine();
        if (!quantiteStr.isEmpty()) {
            int nouvelleQuantite = Integer.parseInt(quantiteStr);
            if (nouvelleQuantite > 0) {
                commande.setQuantite(nouvelleQuantite);
            }
        }
        
        System.out.print("Observations (" + commande.getObservations() + ") : ");
        String observations = scanner.nextLine();
        if (!observations.isEmpty()) {
            commande.setObservations(observations);
        }
        
        if (commandeDAO.modifier(commande)) {
            System.out.println("Commande modifiée avec succès !");
        } else {
            System.out.println("Erreur lors de la modification de la commande !");
        }
    }

    /**
     * Liste toutes les commandes
     */
    private void listerToutesCommandes() {
        System.out.println("\n--- LISTE DE TOUTES LES COMMANDES ---");
        
        List<Commande> commandes = commandeDAO.listerToutes();
        if (commandes.isEmpty()) {
            System.out.println("Aucune commande trouvée !");
        } else {
            System.out.printf("%-5s %-10s %-10s %-8s %-12s %-15s %-15s %-20s%n", 
                            "ID", "CLIENT", "ARTICLE", "QTE", "MONTANT", "DATE", "TYPE", "STATUT");
            System.out.println("-".repeat(100));
            
            for (Commande commande : commandes) {
                System.out.printf("%-5d %-10d %-10d %-8d %-12s %-15s %-15s %-20s%n",
                                commande.getId(),
                                commande.getClientId(),
                                commande.getArticleId(),
                                commande.getQuantite(),
                                commande.getMontantTotal(),
                                commande.getDateCommande().toString().substring(0, 10),
                                commande.getTypeCommande(),
                                commande.getStatut());
            }
        }
    }

    /**
     * Liste les commandes en attente
     */
    private void listerCommandesEnAttente() {
        System.out.println("\n--- COMMANDES EN ATTENTE ---");
        
        List<Commande> commandes = commandeDAO.listerParStatut("en_attente");
        if (commandes.isEmpty()) {
            System.out.println("Aucune commande en attente !");
        } else {
            System.out.printf("%-5s %-10s %-10s %-8s %-12s %-15s%n", 
                            "ID", "CLIENT", "ARTICLE", "QTE", "MONTANT", "DATE");
            System.out.println("-".repeat(65));
            
            for (Commande commande : commandes) {
                System.out.printf("%-5d %-10d %-10d %-8d %-12s %-15s%n",
                                commande.getId(),
                                commande.getClientId(),
                                commande.getArticleId(),
                                commande.getQuantite(),
                                commande.getMontantTotal(),
                                commande.getDateCommande().toString().substring(0, 10));
            }
        }
    }

    /**
     * Liste les commandes validées
     */
    private void listerCommandesValidees() {
        System.out.println("\n--- COMMANDES VALIDÉES ---");
        
        List<Commande> commandes = commandeDAO.listerParStatut("traitee");
        if (commandes.isEmpty()) {
            System.out.println("Aucune commande validée !");
        } else {
            System.out.printf("%-5s %-10s %-10s %-8s %-12s %-15s %-20s%n", 
                            "ID", "CLIENT", "ARTICLE", "QTE", "MONTANT", "DATE", "DATE VALIDATION");
            System.out.println("-".repeat(85));
            
            for (Commande commande : commandes) {
                String dateValidation = commande.getDateValidation() != null ? 
                                      commande.getDateValidation().toString().substring(0, 10) : "N/A";
                System.out.printf("%-5d %-10d %-10d %-8d %-12s %-15s %-20s%n",
                                commande.getId(),
                                commande.getClientId(),
                                commande.getArticleId(),
                                commande.getQuantite(),
                                commande.getMontantTotal(),
                                commande.getDateCommande().toString().substring(0, 10),
                                dateValidation);
            }
        }
    }

    /**
     * Liste les commandes d'un client
     */
    private void listerCommandesClient() {
        System.out.println("\n--- COMMANDES D'UN CLIENT ---");
        
        System.out.print("ID du client : ");
        int clientId = scanner.nextInt();
        scanner.nextLine();
        
        List<Commande> commandes = commandeDAO.listerParClient(clientId);
        if (commandes.isEmpty()) {
            System.out.println("Aucune commande trouvée pour ce client !");
        } else {
            System.out.printf("%-5s %-10s %-8s %-12s %-15s %-15s %-20s%n", 
                            "ID", "ARTICLE", "QTE", "MONTANT", "DATE", "TYPE", "STATUT");
            System.out.println("-".repeat(85));
            
            for (Commande commande : commandes) {
                System.out.printf("%-5d %-10d %-8d %-12s %-15s %-15s %-20s%n",
                                commande.getId(),
                                commande.getArticleId(),
                                commande.getQuantite(),
                                commande.getMontantTotal(),
                                commande.getDateCommande().toString().substring(0, 10),
                                commande.getTypeCommande(),
                                commande.getStatut());
            }
        }
    }

    /**
     * Consulte une commande spécifique
     */
    private void consulterCommande() {
        System.out.println("\n--- CONSULTATION D'UNE COMMANDE ---");
        
        System.out.print("ID de la commande : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Commande commande = commandeDAO.trouverParId(id);
        if (commande == null) {
            System.out.println("Commande non trouvée !");
        } else {
            System.out.println("\nInformations de la commande :");
            System.out.println(commande);
        }
    }

    /**
     * Affiche les statistiques des commandes
     */
    private void afficherStatistiques() {
        System.out.println("\n--- STATISTIQUES DES COMMANDES ---");
        
        double montantTotal = commandeDAO.calculerMontantTotalCommandes();
        System.out.println("Montant total des commandes validées : " + montantTotal + " €");
        
        List<Commande> commandesEnAttente = commandeDAO.listerParStatut("en_attente");
        List<Commande> commandesValidees = commandeDAO.listerParStatut("traitee");
        List<Commande> commandesAnnulees = commandeDAO.listerParStatut("annulee");
        
        System.out.println("Nombre de commandes en attente : " + commandesEnAttente.size());
        System.out.println("Nombre de commandes validées : " + commandesValidees.size());
        System.out.println("Nombre de commandes annulées : " + commandesAnnulees.size());
    }
}
