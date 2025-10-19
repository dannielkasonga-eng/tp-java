@echo off
echo ========================================
echo   EXECUTION DE L'APPLICATION
echo ========================================

REM Vérifier si Java est installé
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERREUR: Java n'est pas installé ou pas dans le PATH
    pause
    exit /b 1
)

REM Vérifier si les classes compilées existent
if not exist "build\classes\com\gestioncommande\Main.class" (
    echo ERREUR: Classes non compilées
    echo Veuillez d'abord exécuter scripts\compile.bat
    pause
    exit /b 1
)

REM Vérifier si le driver MySQL est présent
if not exist "lib\mysql-connector-java-8.0.33.jar" (
    echo ERREUR: Driver MySQL non trouvé
    echo Veuillez placer mysql-connector-java-8.0.33.jar dans le dossier lib\
    pause
    exit /b 1
)

echo Lancement de l'application...
echo.

REM Exécuter l'application
java -cp "build\classes;lib\mysql-connector-java-8.0.33.jar" com.gestioncommande.Main

echo.
echo ========================================
echo   APPLICATION TERMINEE
echo ========================================
pause
