MELWIG VICTOR 04/03/26
modele relationnel snchplanif


ARRET(
  idArret :  INT AUTO_INCREMENT,
  nom : VARCHAR(100)
)       
clé primaire : idArret

ITINERAIRE(
  idItineraire : INT AUTO_INCREMENT,
  dureePrevue : INT
)         
clé primaire : idItineraire

TRAIN(
  idTrain : INT AUTO_INCREMENT,
  numeroTrain : VARCHAR(10),
  typeTrain : ENUM ( "TGV", "TER")
)    
clé primaire : idTrain

TRAJET(
  idTrajet : INT AUTO_INCREMENT,
  dateHeureDepart : DATETIME,
  dateHeureArrivee : DATETIME,
  idTrain INT, 
  idArretDepart INT,
  idArretArrive INT
)      
clé primaire : idTrajet      
clé étrangère : idTrain référence à TRAIN(idTrain)    
idArretDepart référence à ARRET(idArret)     
idArretArrive référence à ARRET(idArret)    


UTILISATEUR(
  idUtilisateur : INT AUTO_INCREMENT,
  login : VARCHAR(100),
  motDePasse : VARCHAR(255),
  nom : VARCHAR(100),
  prenom : VARCHAR(100),
  email : VARCHAR(100),
  role : ENUM (PLANNIFICATEUR, ADMIN, AGENT)
)       
clé primaire : idUtilisateur

ITINERAIRE_TRAJET(
  idItineraire : INT,
  idTrajet : INT
)
Clé primaire : (idItineraire, idTrajet)
Clé étrangère : idItineraire référence à ITINERAIRE(idItineraire)
Clé étrangère : idTrajet référence à TRAJET(idTrajet)


