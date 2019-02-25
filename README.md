# ﻿Implémentation d’un serveur FTP en Java
BARCHID Sami
25/02/2019

## Introduction
Le contenu de ce projet, développé en java 8, est un serveur FTP servant aux clients pour stocker leurs fichiers dans un système de fichier distant accessible grâce au protocole FTP. 

Les commandes FTP gérées sont :
- PWD
- SYST
- AUTH
- USER
- PASS
- QUIT
- LIST
- TYPE
- PASV
- PORT
- PWD
- CDUP
- CWD
- RETR
- RNFR
- RNTO
- DELE
- RMD
- MKD
- STOR

**Attention :** les commandes liées à IPv6 ne sont pas supportées. Veillez donc à communiquer avec le serveur en IPv4.

## Guide d'utilisation
### Exécution
Lancer les commandes suivantes (à la racine du projet) :
```mvn package```
```java -jar target/CAR-TP01-1.0-SNAPSHOT.jar```

### Configurer le serveur FTP
Configurer le serveur FTP en manipulant le fichier `config.properties` situé à la racine du projet.

Champs de configurations possibles :
- `portNumber` : Le numéro de port du serveur
- `users` : Les utilisateurs disponibles dans le serveur
- `passwords` : Les mots de passe des utilisateurs
- `directories` : Les répertoires racine des utilisateurs


## Architecture


### Organisation des packages :
- **ftp** (package)
	- AppConfig.java
    - FtpCommand.java
    - FtpCommunication.java
    - FtpControlChannel.java
    -  FtpDataChannel.java
    -   FtpReply.java
    -   Main.java
    -   MockFtpDataChannel.java
    -   package-info.java
    -   SessionStore.java
  
 - **ftp.controls** (package)
	 -  FtpAuthControl.java
	 -  FtpCdupControl.java
	 -  FtpControl.java
	 -  FtpControlFactory.java
	 -  FtpCwdControl.java
	 -  FtpDataControl.java
	 -  FtpDeleControl.java
	  - FtpListDataControl.java
	 -  FtpMkdControl.java
	 -  FtpPassControl.java
	 - FtpPasvControl.java
	 -  FtpPortControl.java
	 -  FtpPwdControl.java
	 -  FtpQuitControl.java
	 -  FtpRetrDataControl.java
	 - FtpRmdControl.java
	 - FtpRnfrControl.java
	  - FtpRntoControl.java
	  - FtpStorDataControl.java
	   - FtpSystControl.java
	   - FtpTypeControl.java
	  - FtpUnknownControl.java
	  - FtpUserControl.java
	   - package-info.java

#### Package "ftp"
Le package **ftp** contient toutes les classes liées aux fonctionnalités internes du serveur FTP et qui permettent son bon fonctionnement qui n'est pas directement visible par l'utilisateur. Les fonctionnalités internes dont s'occupe ce package sont :
- La configuration générale du serveur
- Le démarrage du serveur
- La connection à un client 
- L'accès simultané pour plusieurs clients (multi-threading)
- La réception des commandes FTP et l'envoie des réponses au client en utilisant la couche transport
- La retenue d'informations sur le client au fil de son utilisation du service FTP

#### Package "ftp.controls"
Ce package contient des classes dont le but est de gérer chaque commande disponibles sur le serveur FTP. Ces classes sont des objets de contrôle qui implémenteront la logique pour chacune des commandes.

### Design patterns utilisés
- Pattern Strategy
	- Le pattern strategy a été utilisé pour la gestion des objets de contrôles 
- Control object
- Pattern Factory :
	- Le pattern Factory a été utilisé pour gérer 
	- La factory en question est la classe `ftp.controls.FtpControlFactory`
- Injection de dépendance + Singleton
	- La classe `AppConfig` est un singleton indirect qui est injecté par dépendance aux classes s'occupant de la gestion des utilisateurs pour que celles-ci puissent connaître les configurations des utilisateurs du serveur.
	- La classe `SessionStore` représente l'état d'une communication avec le client et les informations que le serveur retient pour ce client. Ce store est injecté par dépendance dans chaque classe qui a besoin des données.
- 