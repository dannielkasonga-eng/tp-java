package com.gestioncommande.service;

import com.gestioncommande.dao.ClientDAO;
import com.gestioncommande.entities.Client;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Service pour la gestion des clients
 */
public class ClientService {
    private ClientDAO clientDAO;
    private Scanner scanner;

    public ClientService() throws SQLException {
        this.clientDAO = new ClientDAO();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Affiche le menu de gestion des clients
     */
    public void afficherMenu() {
        boolean continuer = true;
        
        while (continuer) {
            System.out.println("\n=== GESTION DES CLIENTS ===");
            System.out.println("1. Ajouter un client");
            System.out.println("2. Modifier un client");
            System.out.println("3. Activer/Désactiver un client");
            System.out.println("4. Lister tous les clients");
            System.out.println("5. Lister les clients actifs");
            System.out.println("6. Rechercher un client");
            System.out.println("7. Consulter un client");
            System.out.println("8. Retour au menu principal");
            System.out.print("Choisissez une option : ");
            
            int choix = scanner.nextInt();
            scanner.nextLine(); // Consommer la ligne vide
            
            switch (choix) {
                case 1:
                    ajouterClient();
                    break;
                case 2:
                    modifierClient();
                    break;
                case 3:
                    activerDesactiverClient();
                    break;
                case 4:
                    listerTousClients();
                    break;
                case 5:
                    listerClientsActifs();
                    break;
                case 6:
                    rechercherClient();
                    break;
                case 7:
                    consulterClient();
                    break;
                case 8:
                    continuer = false;
                    break;
                default:
                    System.out.println("Option invalide !");
            }
        }
    }

    /**
     * Ajoute un nouveau client
     */
    private void ajouterClient() {
        System.out.println("\n--- AJOUT D'UN NOUVEAU CLIENT ---");
        
        System.out.print("Nom : ");
        String nom = scanner.nextLine();
        
        System.out.print("Prénom : ");
        String prenom = scanner.nextLine();
        
        System.out.print("Sexe (M/F) : ");
        String sexe = scanner.nextLine();
        
        System.out.print("Type de client : ");
        String type = scanner.nextLine();
        
        System.out.print("Contact : ");
        String contact = scanner.nextLine();
        
        System.out.print("Email : ");
        String email = scanner.nextLine();
        
        System.out.print("Adresse : ");
        String adresse = scanner.nextLine();
        
        Client client = new Client(nom, prenom, sexe, type, contact, email, adresse);
        
        if (clientDAO.ajouter(client)) {
            System.out.println("Client ajouté avec succès ! ID : " + client.getId());
        } else {
            System.out.println("Erreur lors de l'ajout du client !");
        }
    }

    /**
     * Modifie un client existant
     */
    private void modifierClient() {
        System.out.println("\n--- MODIFICATION D'UN CLIENT ---");
        
        System.out.print("ID du client à modifier : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Client client = clientDAO.trouverParId(id);
        if (client == null) {
            System.out.println("Client non trouvé !");
            return;
        }
        
        System.out.println("Client actuel : " + client);
        System.out.println("\nNouvelles informations :");
        
        System.out.print("Nom (" + client.getNom() + ") : ");
        String nom = scanner.nextLine();
        if (!nom.isEmpty()) client.setNom(nom);
        
        System.out.print("Prénom (" + client.getPrenom() + ") : ");
        String prenom = scanner.nextLine();
        if (!prenom.isEmpty()) client.setPrenom(prenom);
        
        System.out.print("Sexe (" + client.getSexe() + ") : ");
        String sexe = scanner.nextLine();
        if (!sexe.isEmpty()) client.setSexe(sexe);
        
        System.out.print("Type (" + client.getType() + ") : ");
        String type = scanner.nextLine();
        if (!type.isEmpty()) client.setType(type);
        
        System.out.print("Contact (" + client.getContact() + ") : ");
        String contact = scanner.nextLine();
        if (!contact.isEmpty()) client.setContact(contact);
        
        System.out.print("Email (" + client.getEmail() + ") : ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) client.setEmail(email);
        
        System.out.print("Adresse (" + client.getAdresse() + ") : ");
        String adresse = scanner.nextLine();
        if (!adresse.isEmpty()) client.setAdresse(adresse);
        
        if (clientDAO.modifier(client)) {
            System.out.println("Client modifié avec succès !");
        } else {
            System.out.println("Erreur lors de la modification du client !");
        }
    }

    /**
     * Active ou désactive un client
     */
    private void activerDesactiverClient() {
        System.out.println("\n--- ACTIVATION/DÉSACTIVATION D'UN CLIENT ---");
        
        System.out.print("ID du client : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Client client = clientDAO.trouverParId(id);
        if (client == null) {
            System.out.println("Client non trouvé !");
            return;
        }
        
        System.out.println("Client actuel : " + client);
        
        System.out.print("Activer (1) ou Désactiver (0) ? ");
        int etat = scanner.nextInt();
        scanner.nextLine();
        
        if (clientDAO.activerDesactiver(id, etat)) {
            String action = etat == 1 ? "activé" : "désactivé";
            System.out.println("Client " + action + " avec succès !");
        } else {
            System.out.println("Erreur lors de la modification de l'état du client !");
        }
    }

    /**
     * Liste tous les clients
     */
    private void listerTousClients() {
        System.out.println("\n--- LISTE DE TOUS LES CLIENTS ---");
        
        List<Client> clients = clientDAO.listerTous();
        if (clients.isEmpty()) {
            System.out.println("Aucun client trouvé !");
        } else {
            System.out.printf("%-5s %-20s %-20s %-5s %-15s %-30s %-10s%n", 
                            "ID", "NOM", "PRÉNOM", "SEXE", "TYPE", "CONTACT", "ÉTAT");
            System.out.println("-".repeat(105));
            
            for (Client client : clients) {
                System.out.printf("%-5d %-20s %-20s %-5s %-15s %-30s %-10s%n",
                                client.getId(),
                                client.getNom(),
                                client.getPrenom(),
                                client.getSexe(),
                                client.getType(),
                                client.getContact(),
                                client.isActif() ? "Actif" : "Inactif");
            }
        }
    }

    /**
     * Liste les clients actifs
     */
    private void listerClientsActifs() {
        System.out.println("\n--- LISTE DES CLIENTS ACTIFS ---");
        
        List<Client> clients = clientDAO.listerActifs();
        if (clients.isEmpty()) {
            System.out.println("Aucun client actif trouvé !");
        } else {
            System.out.printf("%-5s %-20s %-20s %-5s %-15s %-30s%n", 
                            "ID", "NOM", "PRÉNOM", "SEXE", "TYPE", "CONTACT");
            System.out.println("-".repeat(95));
            
            for (Client client : clients) {
                System.out.printf("%-5d %-20s %-20s %-5s %-15s %-30s%n",
                                client.getId(),
                                client.getNom(),
                                client.getPrenom(),
                                client.getSexe(),
                                client.getType(),
                                client.getContact());
            }
        }
    }

    /**
     * Recherche des clients
     */
    private void rechercherClient() {
        System.out.println("\n--- RECHERCHE DE CLIENTS ---");
        
        System.out.print("Critère de recherche (nom ou prénom) : ");
        String critere = scanner.nextLine();
        
        List<Client> clients = clientDAO.rechercher(critere);
        if (clients.isEmpty()) {
            System.out.println("Aucun client trouvé avec ce critère !");
        } else {
            System.out.println("\nRésultats de la recherche :");
            System.out.printf("%-5s %-20s %-20s %-5s %-15s %-30s %-10s%n", 
                            "ID", "NOM", "PRÉNOM", "SEXE", "TYPE", "CONTACT", "ÉTAT");
            System.out.println("-".repeat(105));
            
            for (Client client : clients) {
                System.out.printf("%-5d %-20s %-20s %-5s %-15s %-30s %-10s%n",
                                client.getId(),
                                client.getNom(),
                                client.getPrenom(),
                                client.getSexe(),
                                client.getType(),
                                client.getContact(),
                                client.isActif() ? "Actif" : "Inactif");
            }
        }
    }

    /**
     * Consulte un client spécifique
     */
    private void consulterClient() {
        System.out.println("\n--- CONSULTATION D'UN CLIENT ---");
        
        System.out.print("ID du client : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Client client = clientDAO.trouverParId(id);
        if (client == null) {
            System.out.println("Client non trouvé !");
        } else {
            System.out.println("\nInformations du client :");
            System.out.println(client);
        }
    }

    /**
     * Trouve un client par ID (pour utilisation par d'autres services)
     */
    public Client trouverClientParId(int id) {
        return clientDAO.trouverParId(id);
    }

    /**
     * Liste les clients actifs (pour utilisation par d'autres services)
     */
    public List<Client> obtenirClientsActifs() {
        return clientDAO.listerActifs();
    }
}
