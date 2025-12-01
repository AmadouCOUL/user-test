Documentation de l'API User pour gérer les utilisateurs.

Résumé et structuration du projet:
- J'ai créé une entité User pour les sorties et un userInputDTO pour les entrées
- J’ai utilisé un aspect AOP qui s’appuie sur la réflexion Java pour logger les entrées, les sorties et le temps d'exécution
- J'ai utilisé des handlers pour gérer les exceptions et renvoyer des messages d'erreur clairs
- J'ai effectué des tests unitaires pour valider les services métier et des tests d'intégration pour valider les endpoints dans son ensemble 
- La collection Postman est fournie au sein du projet pour tester l’API 


Technologies utilisées :
- Java 17
- Spring Boot
- Maven (pour la gestion des dépendances)
- Postman (pour tester l’API)
  L’application tourne sur http://localhost:8080
- Base de données H2 en mémoire, accessible via http://localhost:8080/h2-console
 JDBC URL : jdbc:h2:mem:testdb
 User : sa
 Password :


Fonctionnalités :
- Créer un utilisateur (uniquement majeur et résident français)
- Afficher les détails d’un utilisateur enregistré
- Validation des inputs avec messages d’erreur
- Logging AOP des entrées, sorties et temps d’exécution

   
Logging AOP, Tous les appels aux endpoints sont journalisés :
- Entrées (input DTO)
- Sorties (réponse)
- Temps d’exécution


Tests:
- Les tests Unitaires et d’intégration sont réalisés avec JUnit et Mockito


Endpoints:
- Créer un utilisateur : POST /users
- Obtenir les détails d’un utilisateur : GET /users/{id}
- Obtenir les utilisateurs (optionnel) : GET /users


Validation:
- Les champs: username, birthdate et country sont obligatoires, phone et gender sont optionnels
- L’utilisateur doit être majeur et doit être résident français
- Un Message d’erreur clair doit être renvoyé en cas d’input invalide

Build et exécution:
- clonez le projet: git clone https://github.com/AmadouCOUL/user-test.git
- Se placer dans le répertoire du projet: cd UserTest
- Builder le projet avec maven: mvn clean install
- Démarrer l'application: mvn spring-boot:run
