package com.gestioncommande.service;

import com.gestioncommande.dao.ArticleDAO;
import com.gestioncommande.entities.Article;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Service pour la gestion des articles
 */
public class ArticleService {
    private ArticleDAO articleDAO;
    private Scanner scanner;

    public ArticleService() throws SQLException {
        this.articleDAO = new ArticleDAO();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Affiche le menu de gestion des articles
     */
    public void afficherMenu() {
        boolean continuer = true;
        
        while (continuer) {
            System.out.println("\n=== GESTION DES ARTICLES ===");
            System.out.println("1. Ajouter un article");
            System.out.println("2. Modifier un article");
            System.out.println("3. Activer/Désactiver un article");
            System.out.println("4. Modifier le stock");
            System.out.println("5. Lister tous les articles");
            System.out.println("6. Lister les articles actifs");
            System.out.println("7. Lister les articles en stock faible");
            System.out.println("8. Rechercher un article");
            System.out.println("9. Consulter un article");
            System.out.println("10. Retour au menu principal");
            System.out.print("Choisissez une option : ");
            
            int choix = scanner.nextInt();
            scanner.nextLine(); // Consommer la ligne vide
            
            switch (choix) {
                case 1:
                    ajouterArticle();
                    break;
                case 2:
                    modifierArticle();
                    break;
                case 3:
                    activerDesactiverArticle();
                    break;
                case 4:
                    modifierStock();
                    break;
                case 5:
                    listerTousArticles();
                    break;
                case 6:
                    listerArticlesActifs();
                    break;
                case 7:
                    listerArticlesStockFaible();
                    break;
                case 8:
                    rechercherArticle();
                    break;
                case 9:
                    consulterArticle();
                    break;
                case 10:
                    continuer = false;
                    break;
                default:
                    System.out.println("Option invalide !");
            }
        }
    }

    /**
     * Ajoute un nouvel article
     */
    private void ajouterArticle() {
        System.out.println("\n--- AJOUT D'UN NOUVEAU ARTICLE ---");
        
        System.out.print("Désignation : ");
        String designation = scanner.nextLine();
        
        System.out.print("Catégorie : ");
        String categorie = scanner.nextLine();
        
        System.out.print("Prix : ");
        BigDecimal prix = scanner.nextBigDecimal();
        scanner.nextLine();
        
        System.out.print("Stock initial : ");
        int stock = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Stock minimum : ");
        int stockMinimum = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Description : ");
        String description = scanner.nextLine();
        
        Article article = new Article(designation, categorie, prix, stock, stockMinimum, description);
        
        if (articleDAO.ajouter(article)) {
            System.out.println("Article ajouté avec succès ! ID : " + article.getId());
        } else {
            System.out.println("Erreur lors de l'ajout de l'article !");
        }
    }

    /**
     * Modifie un article existant
     */
    private void modifierArticle() {
        System.out.println("\n--- MODIFICATION D'UN ARTICLE ---");
        
        System.out.print("ID de l'article à modifier : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Article article = articleDAO.trouverParId(id);
        if (article == null) {
            System.out.println("Article non trouvé !");
            return;
        }
        
        System.out.println("Article actuel : " + article);
        System.out.println("\nNouvelles informations :");
        
        System.out.print("Désignation (" + article.getDesignation() + ") : ");
        String designation = scanner.nextLine();
        if (!designation.isEmpty()) article.setDesignation(designation);
        
        System.out.print("Catégorie (" + article.getCategorie() + ") : ");
        String categorie = scanner.nextLine();
        if (!categorie.isEmpty()) article.setCategorie(categorie);
        
        System.out.print("Prix (" + article.getPrix() + ") : ");
        String prixStr = scanner.nextLine();
        if (!prixStr.isEmpty()) {
            article.setPrix(new BigDecimal(prixStr));
        }
        
        System.out.print("Stock (" + article.getStock() + ") : ");
        String stockStr = scanner.nextLine();
        if (!stockStr.isEmpty()) {
            article.setStock(Integer.parseInt(stockStr));
        }
        
        System.out.print("Stock minimum (" + article.getStockMinimum() + ") : ");
        String stockMinStr = scanner.nextLine();
        if (!stockMinStr.isEmpty()) {
            article.setStockMinimum(Integer.parseInt(stockMinStr));
        }
        
        System.out.print("Description (" + article.getDescription() + ") : ");
        String description = scanner.nextLine();
        if (!description.isEmpty()) article.setDescription(description);
        
        if (articleDAO.modifier(article)) {
            System.out.println("Article modifié avec succès !");
        } else {
            System.out.println("Erreur lors de la modification de l'article !");
        }
    }

    /**
     * Active ou désactive un article
     */
    private void activerDesactiverArticle() {
        System.out.println("\n--- ACTIVATION/DÉSACTIVATION D'UN ARTICLE ---");
        
        System.out.print("ID de l'article : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Article article = articleDAO.trouverParId(id);
        if (article == null) {
            System.out.println("Article non trouvé !");
            return;
        }
        
        System.out.println("Article actuel : " + article);
        
        System.out.print("Activer (1) ou Désactiver (0) ? ");
        int etat = scanner.nextInt();
        scanner.nextLine();
        
        if (articleDAO.activerDesactiver(id, etat)) {
            String action = etat == 1 ? "activé" : "désactivé";
            System.out.println("Article " + action + " avec succès !");
        } else {
            System.out.println("Erreur lors de la modification de l'état de l'article !");
        }
    }

    /**
     * Modifie le stock d'un article
     */
    private void modifierStock() {
        System.out.println("\n--- MODIFICATION DU STOCK ---");
        
        System.out.print("ID de l'article : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Article article = articleDAO.trouverParId(id);
        if (article == null) {
            System.out.println("Article non trouvé !");
            return;
        }
        
        System.out.println("Article : " + article.getDesignation());
        System.out.println("Stock actuel : " + article.getStock());
        System.out.println("Stock minimum : " + article.getStockMinimum());
        
        System.out.print("Nouveau stock : ");
        int nouveauStock = scanner.nextInt();
        scanner.nextLine();
        
        if (nouveauStock < 0) {
            System.out.println("Le stock ne peut pas être négatif !");
            return;
        }
        
        if (articleDAO.modifierStock(id, nouveauStock)) {
            System.out.println("Stock modifié avec succès !");
            if (nouveauStock <= article.getStockMinimum()) {
                System.out.println("ATTENTION : Stock faible détecté !");
            }
        } else {
            System.out.println("Erreur lors de la modification du stock !");
        }
    }

    /**
     * Liste tous les articles
     */
    private void listerTousArticles() {
        System.out.println("\n--- LISTE DE TOUS LES ARTICLES ---");
        
        List<Article> articles = articleDAO.listerTous();
        if (articles.isEmpty()) {
            System.out.println("Aucun article trouvé !");
        } else {
            System.out.printf("%-5s %-30s %-20s %-10s %-8s %-8s %-10s%n", 
                            "ID", "DÉSIGNATION", "CATÉGORIE", "PRIX", "STOCK", "STOCK MIN", "ÉTAT");
            System.out.println("-".repeat(95));
            
            for (Article article : articles) {
                System.out.printf("%-5d %-30s %-20s %-10s %-8d %-8d %-10s%n",
                                article.getId(),
                                article.getDesignation(),
                                article.getCategorie(),
                                article.getPrix(),
                                article.getStock(),
                                article.getStockMinimum(),
                                article.isActif() ? "Actif" : "Inactif");
            }
        }
    }

    /**
     * Liste les articles actifs
     */
    private void listerArticlesActifs() {
        System.out.println("\n--- LISTE DES ARTICLES ACTIFS ---");
        
        List<Article> articles = articleDAO.listerActifs();
        if (articles.isEmpty()) {
            System.out.println("Aucun article actif trouvé !");
        } else {
            System.out.printf("%-5s %-30s %-20s %-10s %-8s %-8s%n", 
                            "ID", "DÉSIGNATION", "CATÉGORIE", "PRIX", "STOCK", "STOCK MIN");
            System.out.println("-".repeat(85));
            
            for (Article article : articles) {
                String stockInfo = article.getStock() + (article.isStockFaible() ? " ⚠️" : "");
                System.out.printf("%-5d %-30s %-20s %-10s %-8s %-8d%n",
                                article.getId(),
                                article.getDesignation(),
                                article.getCategorie(),
                                article.getPrix(),
                                stockInfo,
                                article.getStockMinimum());
            }
        }
    }

    /**
     * Liste les articles en stock faible
     */
    private void listerArticlesStockFaible() {
        System.out.println("\n--- ARTICLES EN STOCK FAIBLE ---");
        
        List<Article> articles = articleDAO.listerStockFaible();
        if (articles.isEmpty()) {
            System.out.println("Aucun article en stock faible !");
        } else {
            System.out.printf("%-5s %-30s %-20s %-8s %-8s %-10s%n", 
                            "ID", "DÉSIGNATION", "CATÉGORIE", "STOCK", "STOCK MIN", "PRIX");
            System.out.println("-".repeat(85));
            
            for (Article article : articles) {
                System.out.printf("%-5d %-30s %-20s %-8d %-8d %-10s%n",
                                article.getId(),
                                article.getDesignation(),
                                article.getCategorie(),
                                article.getStock(),
                                article.getStockMinimum(),
                                article.getPrix());
            }
        }
    }

    /**
     * Recherche des articles
     */
    private void rechercherArticle() {
        System.out.println("\n--- RECHERCHE D'ARTICLES ---");
        
        System.out.print("Critère de recherche (désignation ou catégorie) : ");
        String critere = scanner.nextLine();
        
        List<Article> articles = articleDAO.rechercher(critere);
        if (articles.isEmpty()) {
            System.out.println("Aucun article trouvé avec ce critère !");
        } else {
            System.out.println("\nRésultats de la recherche :");
            System.out.printf("%-5s %-30s %-20s %-10s %-8s %-8s %-10s%n", 
                            "ID", "DÉSIGNATION", "CATÉGORIE", "PRIX", "STOCK", "STOCK MIN", "ÉTAT");
            System.out.println("-".repeat(95));
            
            for (Article article : articles) {
                System.out.printf("%-5d %-30s %-20s %-10s %-8d %-8d %-10s%n",
                                article.getId(),
                                article.getDesignation(),
                                article.getCategorie(),
                                article.getPrix(),
                                article.getStock(),
                                article.getStockMinimum(),
                                article.isActif() ? "Actif" : "Inactif");
            }
        }
    }

    /**
     * Consulte un article spécifique
     */
    private void consulterArticle() {
        System.out.println("\n--- CONSULTATION D'UN ARTICLE ---");
        
        System.out.print("ID de l'article : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Article article = articleDAO.trouverParId(id);
        if (article == null) {
            System.out.println("Article non trouvé !");
        } else {
            System.out.println("\nInformations de l'article :");
            System.out.println(article);
        }
    }

    /**
     * Trouve un article par ID (pour utilisation par d'autres services)
     */
    public Article trouverArticleParId(int id) {
        return articleDAO.trouverParId(id);
    }

    /**
     * Liste les articles actifs (pour utilisation par d'autres services)
     */
    public List<Article> obtenirArticlesActifs() {
        return articleDAO.listerActifs();
    }

    /**
     * Vérifie et met à jour le stock après une commande
     */
    public boolean verifierEtMettreAJourStock(int articleId, int quantite) {
        Article article = articleDAO.trouverParId(articleId);
        if (article == null || !article.isActif()) {
            return false;
        }
        
        if (article.getStock() >= quantite) {
            return articleDAO.modifierStock(articleId, article.getStock() - quantite);
        }
        
        return false;
    }
}
