package com.gestioncommande.dao;

import com.gestioncommande.entities.Commande;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO pour la gestion des commandes
 */
public class CommandeDAO {
    private Connection connection;

    public CommandeDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    /**
     * Ajoute une nouvelle commande
     * @param commande
     * @return boolean
     */
    public boolean ajouter(Commande commande) {
        String sql = "INSERT INTO commandes (client_id, article_id, quantite, prix_unitaire, montant_total, " +
                     "date_commande, type_commande, statut, observations, date_validation) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, commande.getClientId());
            statement.setInt(2, commande.getArticleId());
            statement.setInt(3, commande.getQuantite());
            statement.setBigDecimal(4, commande.getPrixUnitaire());
            statement.setBigDecimal(5, commande.getMontantTotal());
            statement.setTimestamp(6, new Timestamp(commande.getDateCommande().getTime()));
            statement.setString(7, commande.getTypeCommande());
            statement.setString(8, commande.getStatut());
            statement.setString(9, commande.getObservations());
            if (commande.getDateValidation() != null) {
                statement.setTimestamp(10, new Timestamp(commande.getDateValidation().getTime()));
            } else {
                statement.setNull(10, Types.TIMESTAMP);
            }

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    commande.setId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la commande : " + e.getMessage());
        }
        return false;
    }

    /**
     * Met à jour une commande existante
     * @param commande
     * @return boolean
     */
    public boolean modifier(Commande commande) {
        String sql = "UPDATE commandes SET client_id=?, article_id=?, quantite=?, prix_unitaire=?, " +
                     "montant_total=?, type_commande=?, statut=?, observations=?, date_validation=? WHERE id=?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, commande.getClientId());
            statement.setInt(2, commande.getArticleId());
            statement.setInt(3, commande.getQuantite());
            statement.setBigDecimal(4, commande.getPrixUnitaire());
            statement.setBigDecimal(5, commande.getMontantTotal());
            statement.setString(6, commande.getTypeCommande());
            statement.setString(7, commande.getStatut());
            statement.setString(8, commande.getObservations());
            if (commande.getDateValidation() != null) {
                statement.setTimestamp(9, new Timestamp(commande.getDateValidation().getTime()));
            } else {
                statement.setNull(9, Types.TIMESTAMP);
            }
            statement.setInt(10, commande.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification de la commande : " + e.getMessage());
        }
        return false;
    }

    /**
     * Valide une commande
     * @param id
     * @return boolean
     */
    public boolean valider(int id) {
        String sql = "UPDATE commandes SET type_commande='validee', statut='traitee', date_validation=? WHERE id=?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            statement.setInt(2, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la validation de la commande : " + e.getMessage());
        }
        return false;
    }

    /**
     * Annule une commande
     * @param id
     * @return boolean
     */
    public boolean annuler(int id) {
        String sql = "UPDATE commandes SET type_commande='annulee', statut='annulee' WHERE id=?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'annulation de la commande : " + e.getMessage());
        }
        return false;
    }

    /**
     * Trouve une commande par son ID
     * @param id
     * @return Commande
     */
    public Commande trouverParId(int id) {
        String sql = "SELECT * FROM commandes WHERE id=?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return creerCommandeDepuisResultSet(resultSet);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de la commande : " + e.getMessage());
        }
        return null;
    }

    /**
     * Liste toutes les commandes
     * @return List<Commande>
     */
    public List<Commande> listerToutes() {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT c.*, cl.nom as client_nom, cl.prenom as client_prenom, " +
                     "a.designation as article_designation FROM commandes c " +
                     "LEFT JOIN clients cl ON c.client_id = cl.id " +
                     "LEFT JOIN articles a ON c.article_id = a.id " +
                     "ORDER BY c.date_commande DESC";
        
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                commandes.add(creerCommandeDepuisResultSet(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des commandes : " + e.getMessage());
        }
        return commandes;
    }

    /**
     * Liste les commandes par statut
     * @param statut
     * @return List<Commande>
     */
    public List<Commande> listerParStatut(String statut) {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT c.*, cl.nom as client_nom, cl.prenom as client_prenom, " +
                     "a.designation as article_designation FROM commandes c " +
                     "LEFT JOIN clients cl ON c.client_id = cl.id " +
                     "LEFT JOIN articles a ON c.article_id = a.id " +
                     "WHERE c.statut = ? ORDER BY c.date_commande DESC";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, statut);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                commandes.add(creerCommandeDepuisResultSet(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des commandes par statut : " + e.getMessage());
        }
        return commandes;
    }

    /**
     * Liste les commandes d'un client
     * @param clientId
     * @return List<Commande>
     */
    public List<Commande> listerParClient(int clientId) {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT c.*, cl.nom as client_nom, cl.prenom as client_prenom, " +
                     "a.designation as article_designation FROM commandes c " +
                     "LEFT JOIN clients cl ON c.client_id = cl.id " +
                     "LEFT JOIN articles a ON c.article_id = a.id " +
                     "WHERE c.client_id = ? ORDER BY c.date_commande DESC";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, clientId);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                commandes.add(creerCommandeDepuisResultSet(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des commandes du client : " + e.getMessage());
        }
        return commandes;
    }

    /**
     * Calcule le montant total des commandes validées
     * @return double
     */
    public double calculerMontantTotalCommandes() {
        String sql = "SELECT SUM(montant_total) as total FROM commandes WHERE type_commande='validee'";
        
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            if (resultSet.next()) {
                return resultSet.getDouble("total");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du calcul du montant total : " + e.getMessage());
        }
        return 0.0;
    }

    /**
     * Crée un objet Commande à partir d'un ResultSet
     * @param resultSet
     * @return Commande
     * @throws SQLException
     */
    private Commande creerCommandeDepuisResultSet(ResultSet resultSet) throws SQLException {
        Commande commande = new Commande();
        commande.setId(resultSet.getInt("id"));
        commande.setClientId(resultSet.getInt("client_id"));
        commande.setArticleId(resultSet.getInt("article_id"));
        commande.setQuantite(resultSet.getInt("quantite"));
        commande.setPrixUnitaire(resultSet.getBigDecimal("prix_unitaire"));
        commande.setMontantTotal(resultSet.getBigDecimal("montant_total"));
        commande.setDateCommande(resultSet.getTimestamp("date_commande"));
        commande.setTypeCommande(resultSet.getString("type_commande"));
        commande.setStatut(resultSet.getString("statut"));
        commande.setObservations(resultSet.getString("observations"));
        commande.setDateValidation(resultSet.getTimestamp("date_validation"));
        return commande;
    }
}
