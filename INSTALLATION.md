# Guide d'Installation - Application de Gestion de Commandes

## Prérequis

Avant d'installer l'application, assurez-vous d'avoir :

- Java 8 ou supérieur** installé sur votre système
- MySQL 5.7 ou supérieur** installé et démarré
- Driver MySQL Connector/J** téléchargé

## Installation Pas à Pas

### Étape 1 : Vérifier Java

**Windows :**
```cmd
java -version
```

**Linux/Mac :**
```bash
java -version
```

Si Java n'est pas installé, téléchargez-le depuis [Oracle Java](https://www.oracle.com/java/technologies/downloads/) ou [OpenJDK](https://openjdk.org/).

### Étape 2 : Configurer MySQL

1. **Démarrer MySQL** :
   - Windows : `net start mysql`
   - Linux : `sudo systemctl start mysql`
   - Mac : `brew services start mysql`

2. **Se connecter à MySQL** :
   ```bash
   mysql -u root -p
   ```

3. **Créer la base de données** :
   ```bash
   mysql -u root -p < database/create_database.sql
   ```

4. **Vérifier la création** :
   ```sql
   USE gestion_commande;
   SHOW TABLES;
   SELECT COUNT(*) FROM clients;
   ```

### Étape 3 : Installer le Driver MySQL

1. **Télécharger le driver** :
   - Aller sur [MySQL Downloads](https://dev.mysql.com/downloads/connector/j/)
   - Télécharger `mysql-connector-java-8.0.33.jar`

2. **Placer le fichier** :
   - Créer le dossier `lib/` s'il n'existe pas
   - Placer le fichier JAR dans `lib/mysql-connector-java-8.0.33.jar`

### Étape 4 : Configurer la Connexion

1. **Modifier les paramètres** dans `src/main/java/com/gestioncommande/dao/DatabaseConnection.java` :
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/gestion_commande";
   private static final String USERNAME = "root";        // Votre nom d'utilisateur
   private static final String PASSWORD = "votre_mdp";   // Votre mot de passe
   ```

2. **Ou modifier** le fichier `config/database.properties` si vous utilisez la version avancée.

### Étape 5 : Compiler l'Application

**Windows :**
```cmd
scripts\compile.bat
```

**Linux/Mac :**
```bash
./scripts/compile.sh
```

**Ou manuellement :**
```bash
mkdir -p build/classes
javac -cp "lib/mysql-connector-java-8.0.33.jar" -d build/classes src/main/java/com/gestioncommande/*.java src/main/java/com/gestioncommande/entities/*.java src/main/java/com/gestioncommande/dao/*.java src/main/java/com/gestioncommande/service/*.java
```

### Étape 6 : Exécuter l'Application

**Windows :**
```cmd
scripts\run.bat
```

**Linux/Mac :**
```bash
./scripts/run.sh
```

**Ou manuellement :**
```bash
java -cp "build/classes:lib/mysql-connector-java-8.0.33.jar" com.gestioncommande.Main
```

##  Configuration Avancée

### Variables d'Environnement

Vous pouvez définir des variables d'environnement pour faciliter la configuration :

```bash
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=gestion_commande
export DB_USER=root
export DB_PASSWORD=votre_mot_de_passe
```

### Fichier de Configuration

Modifiez `config/database.properties` pour personnaliser la connexion :

```properties
db.url=jdbc:mysql://localhost:3306/gestion_commande
db.username=root
db.password=votre_mot_de_passe
```

## Résolution des Problèmes

### Erreur : "Driver MySQL non trouvé"

**Solution :**
1. Vérifier que le fichier `mysql-connector-java-8.0.33.jar` est dans le dossier `lib/`
2. Vérifier le nom exact du fichier
3. Vérifier les permissions de lecture

### Erreur : "Impossible de se connecter à la base de données"

**Solutions :**
1. Vérifier que MySQL est démarré :
   ```bash
   mysqladmin ping
   ```

2. Vérifier les identifiants dans `DatabaseConnection.java`

3. Vérifier que la base `gestion_commande` existe :
   ```sql
   SHOW DATABASES LIKE 'gestion_commande';
   ```

4. Tester la connexion manuellement :
   ```bash
   mysql -u root -p gestion_commande
   ```

### Erreur : "Classes non trouvées"

**Solution :**
1. Vérifier que la compilation a réussi
2. Vérifier que les classes sont dans `build/classes/`
3. Vérifier le classpath dans la commande d'exécution

### Erreur : "Port 3306 déjà utilisé"

**Solution :**
1. Vérifier quel processus utilise le port :
   ```bash
   netstat -an | grep 3306
   ```

2. Arrêter MySQL et le redémarrer :
   ```bash
   sudo systemctl stop mysql
   sudo systemctl start mysql
   ```

## Test de l'Installation

Une fois l'application démarrée, vous devriez voir :

```
=== APPLICATION DE GESTION DE COMMANDES ===
Initialisation en cours...
Connexion à la base de données établie avec succès !
Application initialisée avec succès !

==================================================
           MENU PRINCIPAL
==================================================
1. Gestion des Clients
2. Gestion des Articles
3. Gestion des Commandes
4. Test de connexion à la base de données
5. Quitter l'application
==================================================
Choisissez une option :
```

##  Données de Test

L'application est livrée avec des données de test :

- **4 clients** d'exemple
- **6 articles** d'exemple
- **4 commandes** d'exemple

Vous pouvez les supprimer avec :
```bash
mysql -u root -p < database/reset_database.sql
```

##  Mise à Jour

Pour mettre à jour l'application :

1. Sauvegarder vos données :
   ```bash
   mysqldump -u root -p gestion_commande > backup.sql
   ```

2. Recompiler :
   ```bash
   scripts/compile.bat  # Windows
   ./scripts/compile.sh # Linux/Mac
   ```

3. Redémarrer l'application

## Support

Si vous rencontrez des problèmes :

1. Vérifiez les logs d'erreur
2. Consultez la section "Résolution des Problèmes"
3. Vérifiez que tous les prérequis sont installés
4. Testez la connexion à MySQL manuellemment

## Vérification Finale

Votre installation est réussie si :

-  L'application se lance sans erreur
-  Le menu principal s'affiche
-  Le test de connexion fonctionne
- ✅ Vous pouvez naviguer dans les menus
- ✅ Les données de test sont visibles
