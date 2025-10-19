# Application de Gestion de Commandes

Une application Java complète pour la gestion de commandes avec trois entités principales : Clients, Articles et Commandes.

## 🚀 Fonctionnalités

### Gestion des Clients
- ✅ Ajouter un nouveau client
- ✅ Modifier les informations d'un client
- ✅ Activer/Désactiver un client
- ✅ Lister tous les clients
- ✅ Rechercher un client
- ✅ Consulter les détails d'un client

### Gestion des Articles
- ✅ Ajouter un nouvel article
- ✅ Modifier un article existant
- ✅ Activer/Désactiver un article
- ✅ Gérer le stock (modification, alerte stock faible)
- ✅ Lister tous les articles
- ✅ Rechercher un article
- ✅ Consulter les détails d'un article

### Gestion des Commandes
- ✅ Effectuer une nouvelle commande
- ✅ Valider une commande
- ✅ Annuler une commande
- ✅ Modifier une commande
- ✅ Lister toutes les commandes
- ✅ Consulter les détails d'une commande
- ✅ Statistiques des commandes

## 🛠️ Technologies Utilisées

- **Java** : Langage de programmation principal
- **MySQL** : Base de données relationnelle
- **JDBC** : Connexion à la base de données
- **Architecture MVC** : Modèle-Vue-Contrôleur avec pattern DAO

## 📋 Prérequis

- Java 8 ou supérieur
- MySQL 5.7 ou supérieur
- Driver MySQL Connector/J

## 🔧 Installation

### 1. Configuration de la Base de Données

1. **Démarrer MySQL** :
   ```bash
   # Windows
   net start mysql
   
   # Linux/Mac
   sudo systemctl start mysql
   ```

2. **Créer la base de données** :
   ```bash
   mysql -u root -p < database/create_database.sql
   ```

3. **Vérifier la création** :
   ```bash
   mysql -u root -p
   USE gestion_commande;
   SHOW TABLES;
   ```

### 2. Configuration du Projet Java

1. **Télécharger le driver MySQL** :
   - Télécharger `mysql-connector-java-8.0.33.jar` depuis [MySQL Downloads](https://dev.mysql.com/downloads/connector/j/)
   - Placer le fichier JAR dans le dossier `lib/` du projet

2. **Configuration de la connexion** :
   - Modifier les paramètres dans `src/main/java/com/gestioncommande/dao/DatabaseConnection.java` :
     ```java
     private static final String URL = "jdbc:mysql://localhost:3306/gestion_commande";
     private static final String USERNAME = "root";
     private static final String PASSWORD = "votre_mot_de_passe";
     ```

### 3. Compilation et Exécution

1. **Compiler le projet** :
   ```bash
   # Créer le dossier de compilation
   mkdir -p build/classes
   
   # Compiler les classes
   javac -cp "lib/mysql-connector-java-8.0.33.jar" -d build/classes src/main/java/com/gestioncommande/*.java src/main/java/com/gestioncommande/entities/*.java src/main/java/com/gestioncommande/dao/*.java src/main/java/com/gestioncommande/service/*.java
   ```

2. **Exécuter l'application** :
   ```bash
   java -cp "build/classes:lib/mysql-connector-java-8.0.33.jar" com.gestioncommande.Main
   ```

## 📁 Structure du Projet

```
commande_gestion/
├── src/main/java/com/gestioncommande/
│   ├── Main.java                          # Classe principale
│   ├── entities/                          # Entités métier
│   │   ├── Client.java
│   │   ├── Article.java
│   │   └── Commande.java
│   ├── dao/                               # Accès aux données
│   │   ├── DatabaseConnection.java
│   │   ├── ClientDAO.java
│   │   ├── ArticleDAO.java
│   │   └── CommandeDAO.java
│   └── service/                           # Logique métier
│       ├── ClientService.java
│       ├── ArticleService.java
│       └── CommandeService.java
├── database/                              # Scripts SQL
│   ├── create_database.sql
│   ├── drop_database.sql
│   └── reset_database.sql
├── lib/                                   # Bibliothèques externes
│   └── mysql-connector-java-8.0.33.jar
└── README.md
```

## 🗄️ Structure de la Base de Données

### Table `clients`
- `id` : Identifiant unique (AUTO_INCREMENT)
- `nom` : Nom du client
- `prenom` : Prénom du client
- `sexe` : Sexe (M/F)
- `type` : Type de client (Particulier/Entreprise)
- `contact` : Numéro de téléphone
- `email` : Adresse email
- `adresse` : Adresse complète
- `date_creation` : Date de création
- `etat` : État (1=actif, 0=inactif)

### Table `articles`
- `id` : Identifiant unique (AUTO_INCREMENT)
- `designation` : Désignation de l'article
- `categorie` : Catégorie de l'article
- `prix` : Prix unitaire
- `stock` : Quantité en stock
- `stock_minimum` : Seuil de stock minimum
- `description` : Description détaillée
- `date_creation` : Date de création
- `date_modification` : Date de dernière modification
- `etat` : État (1=actif, 0=inactif)

### Table `commandes`
- `id` : Identifiant unique (AUTO_INCREMENT)
- `client_id` : Référence vers le client
- `article_id` : Référence vers l'article
- `quantite` : Quantité commandée
- `prix_unitaire` : Prix unitaire au moment de la commande
- `montant_total` : Montant total de la commande
- `date_commande` : Date de la commande
- `type_commande` : Type (en_cours/validee/annulee)
- `statut` : Statut (en_attente/traitee/livree/annulee)
- `observations` : Observations
- `date_validation` : Date de validation

## 🎯 Utilisation

### Menu Principal
L'application propose un menu principal avec les options suivantes :
1. **Gestion des Clients** : Gérer les clients
2. **Gestion des Articles** : Gérer les articles et le stock
3. **Gestion des Commandes** : Effectuer et gérer les commandes
4. **Test de connexion** : Vérifier la connexion à la base
5. **Quitter** : Fermer l'application

### Workflow des Commandes
1. **Effectuer une commande** : Sélectionner un client et un article, saisir la quantité
2. **Valider la commande** : Vérifier le stock et confirmer la commande
3. **Suivi** : Consulter l'état et les détails des commandes

## 🔧 Scripts de Maintenance

### Réinitialiser la base de données
```bash
mysql -u root -p < database/reset_database.sql
```

### Supprimer la base de données
```bash
mysql -u root -p < database/drop_database.sql
```

## 🐛 Dépannage

### Problèmes de Connexion
1. Vérifier que MySQL est démarré
2. Vérifier les identifiants dans `DatabaseConnection.java`
3. Vérifier que la base `gestion_commande` existe
4. Vérifier que le driver MySQL est dans le classpath

### Problèmes de Compilation
1. Vérifier que Java 8+ est installé
2. Vérifier que le driver MySQL est présent
3. Vérifier la syntaxe des chemins (Windows vs Linux/Mac)

## 📝 Notes de Développement

- L'application utilise le pattern DAO (Data Access Object)
- Les connexions à la base sont gérées de manière centralisée
- Les erreurs SQL sont capturées et affichées à l'utilisateur
- L'interface est en mode console avec des menus interactifs

## 🤝 Contribution

Pour contribuer au projet :
1. Fork le repository
2. Créer une branche pour votre fonctionnalité
3. Commiter vos changements
4. Pousser vers la branche
5. Ouvrir une Pull Request

## 📄 Licence

Ce projet est sous licence MIT. Voir le fichier LICENSE pour plus de détails.
