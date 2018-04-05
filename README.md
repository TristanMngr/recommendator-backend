# recommendator-backend
## INSTALLATION

il faut installer maven sur votre machine (ordinateur) pour pouvoir utiliser sa command line interface.

Creez deux bases de données vide "recommendator" et "recommendator_test".


ensuite pour lancer le serveur :


+ mvn spring-boot:run (console dans /recommendator-backend) 

ou

+ run avec Intellij/Eclipse le main de Application.java

## TESTS
Les tests s'effectuent sur une base de donnée apart (recommendator_test), afin de ne pas compromettre les données de votre BDD locale.

## STACK
### maven
pour gérer les dépendances du projet
### spring
framework java EE pour éviter de trop réinventer la roue
### spring-boot 
pour éviter des tonnes de config useless (similaire a create-react-app pour react)
### hibernate
ORM pour faciliter manipulation de la BDD. 
les config se trouvent respectivement dans les fichiers src/main/resources/application.properties et src/test/resources/application.properties pour les environnements de dev et de test.

## TODOS :
replace mySQL par postgre :
changer la dépendance mysql-connector-java pour une postgre,
changer les config de application.properties
