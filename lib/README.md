# Bibliothèques Externes

Ce dossier contient les bibliothèques externes nécessaires au fonctionnement de l'application.

## Driver MySQL

### Téléchargement
- **Fichier requis** : `mysql-connector-java-8.0.33.jar`
- **Source** : [MySQL Downloads](https://dev.mysql.com/downloads/connector/j/)
- **Version recommandée** : 8.0.33 ou supérieure

### Installation
1. Télécharger le fichier JAR depuis le site officiel MySQL
2. Placer le fichier dans ce dossier (`lib/`)
3. Le fichier doit être nommé exactement : `mysql-connector-java-8.0.33.jar`

### Structure attendue
```
lib/
└── mysql-connector-java-8.0.33.jar
```

## Compilation avec le Driver

### Commande de compilation
```bash
javac -cp "lib/mysql-connector-java-8.0.33.jar" -d build/classes src/main/java/com/gestioncommande/*.java src/main/java/com/gestioncommande/entities/*.java src/main/java/com/gestioncommande/dao/*.java src/main/java/com/gestioncommande/service/*.java
```

### Commande d'exécution
```bash
java -cp "build/classes:lib/mysql-connector-java-8.0.33.jar" com.gestioncommande.Main
```

## Notes importantes

- **Windows** : Utiliser `;` comme séparateur de classpath
- **Linux/Mac** : Utiliser `:` comme séparateur de classpath
- Vérifier que le fichier JAR est présent avant la compilation
- Le driver doit être compatible avec la version de MySQL utilisée
