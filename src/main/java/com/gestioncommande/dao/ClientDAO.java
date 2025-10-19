package com.gestioncommande.dao;

import com.gestioncommande.entities.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO (Data Access Object) pour la gestion des données des clients.
 * Implémente toutes les opérations CRUD (Create, Read, Update, Delete) pour l'entité Client.
 * 
 * @author Système de Gestion de Commandes
 * @version 1.0
 * @since 2024
 */
public class ClientDAO {
    
    // ========== ATTRIBUTS ==========
    
    /** Connexion à la base de données */
    private Connection connection;

    // ========== CONSTRUCTEUR ==========
    
    /**
     * Constructeur qui initialise la connexion à la base de données.
     * @throws SQLException En cas d'erreur de connexion
     */
    public ClientDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    // ========== MÉTHODES CRUD ==========
    
    /**
     * Ajoute un nouveau client dans la base de données.
     * L'ID du client est automatiquement généré et assigné à l'objet.
     * 
     * @param client Le client à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean ajouter(Client client) {
        String sql = "INSERT INTO clients (nom, prenom, sexe, type, contact, email, adresse, date_creation, etat) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Paramètres de la requête
            statement.setString(1, client.getNom());
            statement.setString(2, client.getPrenom());
            statement.setString(3, client.getSexe());
            statement.setString(4, client.getType());
            statement.setString(5, client.getContact());
            statement.setString(6, client.getEmail());
            statement.setString(7, client.getAdresse());
            statement.setTimestamp(8, new Timestamp(client.getDateCreation().getTime()));
            statement.setInt(9, client.getEtat());

            // Exécution de la requête
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                // Récupération de l'ID généré automatiquement
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    client.setId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du client : " + e.getMessage());
        }
        return false;
    }

    /**
     * Met à jour un client existant
     * @param client
     * @return boolean
     */
    public boolean modifier(Client client) {
        String sql = "UPDATE clients SET nom=?, prenom=?, sexe=?, type=?, contact=?, email=?, adresse=?, etat=? WHERE id=?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, client.getNom());
            statement.setString(2, client.getPrenom());
            statement.setString(3, client.getSexe());
            statement.setString(4, client.getType());
            statement.setString(5, client.getContact());
            statement.setString(6, client.getEmail());
            statement.setString(7, client.getAdresse());
            statement.setInt(8, client.getEtat());
            statement.setInt(9, client.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du client : " + e.getMessage());
        }
        return false;
    }

    /**
     * Supprime un client (soft delete)
     * @param id
     * @return boolean
     */
    public boolean supprimer(int id) {
        return activerDesactiver(id, 0);
    }

    /**
     * Active ou désactive un client
     * @param id
     * @param etat
     * @return boolean
     */
    public boolean activerDesactiver(int id, int etat) {
        String sql = "UPDATE clients SET etat=? WHERE id=?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, etat);
            statement.setInt(2, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification de l'état du client : " + e.getMessage());
        }
        return false;
    }

    /**
     * Trouve un client par son ID
     * @param id
     * @return Client
     */
    public Client trouverParId(int id) {
        String sql = "SELECT * FROM clients WHERE id=?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return creerClientDepuisResultSet(resultSet);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du client : " + e.getMessage());
        }
        return null;
    }

    /**
     * Liste tous les clients
     * @return List<Client>
     */
    public List<Client> listerTous() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients ORDER BY nom, prenom";
        
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                clients.add(creerClientDepuisResultSet(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des clients : " + e.getMessage());
        }
        return clients;
    }

    /**
     * Liste les clients actifs
     * @return List<Client>
     */
    public List<Client> listerActifs() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients WHERE etat=1 ORDER BY nom, prenom";
        
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                clients.add(creerClientDepuisResultSet(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des clients actifs : " + e.getMessage());
        }
        return clients;
    }

    /**
     * Recherche des clients par nom ou prénom
     * @param critere
     * @return List<Client>
     */
    public List<Client> rechercher(String critere) {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients WHERE nom LIKE ? OR prenom LIKE ? ORDER BY nom, prenom";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            String recherche = "%" + critere + "%";
            statement.setString(1, recherche);
            statement.setString(2, recherche);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                clients.add(creerClientDepuisResultSet(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des clients : " + e.getMessage());
        }
        return clients;
    }

    /**
     * Crée un objet Client à partir d'un ResultSet
     * @param resultSet
     * @return Client
     * @throws SQLException
     */
    private Client creerClientDepuisResultSet(ResultSet resultSet) throws SQLException {
        Client client = new Client();
        client.setId(resultSet.getInt("id"));
        client.setNom(resultSet.getString("nom"));
        client.setPrenom(resultSet.getString("prenom"));
        client.setSexe(resultSet.getString("sexe"));
        client.setType(resultSet.getString("type"));
        client.setContact(resultSet.getString("contact"));
        client.setEmail(resultSet.getString("email"));
        client.setAdresse(resultSet.getString("adresse"));
        client.setDateCreation(resultSet.getTimestamp("date_creation"));
        client.setEtat(resultSet.getInt("etat"));
        return client;
    }
}
