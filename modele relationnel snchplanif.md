MELWIG VICTOR 04/03/26
modele relationnel snchplanif


ARRET(
  idArret :  INT AUTO_INCREMENT,
  nom : VARCHAR(100)
)       
clé primaire : idArret

ITINERAIRE(
  idItineraire : INT AUTO_INCREMENT,
  dureePrevue : INT,
)         
clé primaire : idItineraire

TRAIN(
  idTrain : INT AUTO_INCREMENT,
  numeroTrain : VARCHAR(100),
  typeTrain : ENUM ( "TGV", "TER"),
)    
clé primaire : idTrain

TRAJET(
  idTrajet : INT AUTO_INCREMENT,
  dateHeureDepart : DATETIME,
  dateHeureArrivee : DATETIME
  idTrain, 
  idArretDepart,
  idArretArrive,
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
  role : ENUM (PLANNIFICATEUR, ADMIN),
)       
clé primaire : idUtilisateur



