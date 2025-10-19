package com.gestioncommande.dao;

import com.gestioncommande.entities.Article;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO pour la gestion des articles
 */
public class ArticleDAO {
    private Connection connection;

    public ArticleDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    /**
     * Ajoute un nouvel article
     * @param article
     * @return boolean
     */
    public boolean ajouter(Article article) {
        String sql = "INSERT INTO articles (designation, categorie, prix, stock, stock_minimum, " +
                     "description, date_creation, date_modification, etat) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, article.getDesignation());
            statement.setString(2, article.getCategorie());
            statement.setBigDecimal(3, article.getPrix());
            statement.setInt(4, article.getStock());
            statement.setInt(5, article.getStockMinimum());
            statement.setString(6, article.getDescription());
            statement.setTimestamp(7, new Timestamp(article.getDateCreation().getTime()));
            statement.setTimestamp(8, new Timestamp(article.getDateModification().getTime()));
            statement.setInt(9, article.getEtat());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    article.setId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'article : " + e.getMessage());
        }
        return false;
    }

    /**
     * Met à jour un article existant
     * @param article
     * @return boolean
     */
    public boolean modifier(Article article) {
        String sql = "UPDATE articles SET designation=?, categorie=?, prix=?, stock=?, stock_minimum=?, " +
                     "description=?, date_modification=?, etat=? WHERE id=?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, article.getDesignation());
            statement.setString(2, article.getCategorie());
            statement.setBigDecimal(3, article.getPrix());
            statement.setInt(4, article.getStock());
            statement.setInt(5, article.getStockMinimum());
            statement.setString(6, article.getDescription());
            statement.setTimestamp(7, new Timestamp(article.getDateModification().getTime()));
            statement.setInt(8, article.getEtat());
            statement.setInt(9, article.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification de l'article : " + e.getMessage());
        }
        return false;
    }

    /**
     * Supprime un article (soft delete)
     * @param id
     * @return boolean
     */
    public boolean supprimer(int id) {
        return activerDesactiver(id, 0);
    }

    /**
     * Active ou désactive un article
     * @param id
     * @param etat
     * @return boolean
     */
    public boolean activerDesactiver(int id, int etat) {
        String sql = "UPDATE articles SET etat=?, date_modification=? WHERE id=?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, etat);
            statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            statement.setInt(3, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification de l'état de l'article : " + e.getMessage());
        }
        return false;
    }

    /**
     * Met à jour le stock d'un article
     * @param id
     * @param nouveauStock
     * @return boolean
     */
    public boolean modifierStock(int id, int nouveauStock) {
        String sql = "UPDATE articles SET stock=?, date_modification=? WHERE id=?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, nouveauStock);
            statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            statement.setInt(3, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du stock : " + e.getMessage());
        }
        return false;
    }

    /**
     * Trouve un article par son ID
     * @param id
     * @return Article
     */
    public Article trouverParId(int id) {
        String sql = "SELECT * FROM articles WHERE id=?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return creerArticleDepuisResultSet(resultSet);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de l'article : " + e.getMessage());
        }
        return null;
    }

    /**
     * Liste tous les articles
     * @return List<Article>
     */
    public List<Article> listerTous() {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM articles ORDER BY designation";
        
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                articles.add(creerArticleDepuisResultSet(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des articles : " + e.getMessage());
        }
        return articles;
    }

    /**
     * Liste les articles actifs
     * @return List<Article>
     */
    public List<Article> listerActifs() {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM articles WHERE etat=1 ORDER BY designation";
        
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                articles.add(creerArticleDepuisResultSet(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des articles actifs : " + e.getMessage());
        }
        return articles;
    }

    /**
     * Liste les articles en rupture de stock
     * @return List<Article>
     */
    public List<Article> listerStockFaible() {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM articles WHERE stock <= stock_minimum AND etat=1 ORDER BY stock";
        
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                articles.add(creerArticleDepuisResultSet(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des articles en stock faible : " + e.getMessage());
        }
        return articles;
    }

    /**
     * Recherche des articles par désignation ou catégorie
     * @param critere
     * @return List<Article>
     */
    public List<Article> rechercher(String critere) {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM articles WHERE designation LIKE ? OR categorie LIKE ? ORDER BY designation";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            String recherche = "%" + critere + "%";
            statement.setString(1, recherche);
            statement.setString(2, recherche);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                articles.add(creerArticleDepuisResultSet(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des articles : " + e.getMessage());
        }
        return articles;
    }

    /**
     * Crée un objet Article à partir d'un ResultSet
     * @param resultSet
     * @return Article
     * @throws SQLException
     */
    private Article creerArticleDepuisResultSet(ResultSet resultSet) throws SQLException {
        Article article = new Article();
        article.setId(resultSet.getInt("id"));
        article.setDesignation(resultSet.getString("designation"));
        article.setCategorie(resultSet.getString("categorie"));
        article.setPrix(resultSet.getBigDecimal("prix"));
        article.setStock(resultSet.getInt("stock"));
        article.setStockMinimum(resultSet.getInt("stock_minimum"));
        article.setDescription(resultSet.getString("description"));
        article.setDateCreation(resultSet.getTimestamp("date_creation"));
        article.setDateModification(resultSet.getTimestamp("date_modification"));
        article.setEtat(resultSet.getInt("etat"));
        return article;
    }
}
