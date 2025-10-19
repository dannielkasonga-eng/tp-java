#!/bin/bash

echo "========================================"
echo "   EXECUTION DE L'APPLICATION"
echo "========================================"

# Vérifier si Java est installé
if ! command -v java &> /dev/null; then
    echo "ERREUR: Java n'est pas installé ou pas dans le PATH"
    exit 1
fi

# Vérifier si les classes compilées existent
if [ ! -f "build/classes/com/gestioncommande/Main.class" ]; then
    echo "ERREUR: Classes non compilées"
    echo "Veuillez d'abord exécuter ./scripts/compile.sh"
    exit 1
fi

# Vérifier si le driver MySQL est présent
if [ ! -f "lib/mysql-connector-java-8.0.33.jar" ]; then
    echo "ERREUR: Driver MySQL non trouvé"
    echo "Veuillez placer mysql-connector-java-8.0.33.jar dans le dossier lib/"
    exit 1
fi

echo "Lancement de l'application..."
echo ""

# Exécuter l'application
java -cp "build/classes:lib/mysql-connector-java-8.0.33.jar" com.gestioncommande.Main

echo ""
echo "========================================"
echo "   APPLICATION TERMINEE"
echo "========================================"
