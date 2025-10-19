#!/bin/bash

echo "========================================"
echo "   COMPILATION DU PROJET JAVA"
echo "========================================"

# Vérifier si Java est installé
if ! command -v java &> /dev/null; then
    echo "ERREUR: Java n'est pas installé ou pas dans le PATH"
    exit 1
fi

# Créer le dossier de compilation
mkdir -p build/classes

# Vérifier si le driver MySQL est présent
if [ ! -f "lib/mysql-connector-java-8.0.33.jar" ]; then
    echo "ERREUR: Driver MySQL non trouvé"
    echo "Veuillez placer mysql-connector-java-8.0.33.jar dans le dossier lib/"
    exit 1
fi

echo "Compilation en cours..."

# Compiler les classes
javac -cp "lib/mysql-connector-java-8.0.33.jar" -d build/classes src/main/java/com/gestioncommande/*.java src/main/java/com/gestioncommande/entities/*.java src/main/java/com/gestioncommande/dao/*.java src/main/java/com/gestioncommande/service/*.java

if [ $? -eq 0 ]; then
    echo ""
    echo "========================================"
    echo "   COMPILATION REUSSIE !"
    echo "========================================"
    echo "Les classes ont été compilées dans build/classes/"
    echo ""
    echo "Pour exécuter l'application, utilisez: ./scripts/run.sh"
else
    echo ""
    echo "========================================"
    echo "   ERREUR DE COMPILATION !"
    echo "========================================"
    echo "Vérifiez les erreurs ci-dessus"
fi
