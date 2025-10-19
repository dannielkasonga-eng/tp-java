@echo off
echo ========================================
echo   COMPILATION DU PROJET JAVA
echo ========================================

REM Vérifier si Java est installé
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERREUR: Java n'est pas installé ou pas dans le PATH
    pause
    exit /b 1
)

REM Créer le dossier de compilation
if not exist "build\classes" mkdir "build\classes"

REM Vérifier si le driver MySQL est présent
if not exist "lib\mysql-connector-java-8.0.33.jar" (
    echo ERREUR: Driver MySQL non trouvé
    echo Veuillez placer mysql-connector-java-8.0.33.jar dans le dossier lib\
    pause
    exit /b 1
)

echo Compilation en cours...

REM Compiler les classes
javac -cp "lib\mysql-connector-java-8.0.33.jar" -d build\classes src\main\java\com\gestioncommande\*.java src\main\java\com\gestioncommande\entities\*.java src\main\java\com\gestioncommande\dao\*.java src\main\java\com\gestioncommande\service\*.java

if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo   COMPILATION REUSSIE !
    echo ========================================
    echo Les classes ont été compilées dans build\classes\
    echo.
    echo Pour exécuter l'application, utilisez: scripts\run.bat
) else (
    echo.
    echo ========================================
    echo   ERREUR DE COMPILATION !
    echo ========================================
    echo Vérifiez les erreurs ci-dessus
)

pause
