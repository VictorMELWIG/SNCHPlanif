modele relationnel snchplanif


ARRET(
  idArret, 
  nom
)
clé primaire : idArret

ITINERAIRE(
  idItineraire,
  dureePrevue
)    
clé primaire : idItineraire

TRAIN(
  idTrain,
  numeroTrain,
  typeTrain : ENUM ( "TGV", "TER")
)    
clé primaire : idTrain

TRAJET(
  idTrajet,
  dateHeureDepart,
  dateHeureArrivee,
  idTrain, 
  idArretDepart,
  idArretArrive,
)      
clé primaire : idTrajet 
clé étrangère : idTrain référence à TRAIN(idTrain)
idArretDepart référence à ARRET(idArret)
idArretArrive référence à ARRET(idArret)


UTILISATEUR(
  idUtilisateur,
  login,
  motDePasse : 256
  nom,
  prenom,
  email,
  role : ENUM (plannificateur, admin)
)       
clé primaire : idUtilisateur
