[![Build Status](http://circleci-badges-max.herokuapp.com/img/TristanMngr/recommendator-backend?token=ea4b8a9e3612900f9ce71652664ce17cd336b4ac)](https://circleci.com/gh/TristanMngr/recommendator-backend)

# recommendator-backend
## INSTALLATION

### todo only one time

* install maven 
* install postgresql
* create two postgresql databases : recommendator and recommendator_test

### launch server (port 8080)

+ mvn spring-boot:run (console dans /recommendator-backend) 

__or__

+   run avec Intellij/Eclipse le main de Application.java


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