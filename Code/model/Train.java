package snchbilletterie.model;

public class Train {
    private int idTrain;
    private String numeroTrain;
    private String typeTrain;

    public Train() {
    }

    public Train(int idTrain, String numeroTrain, String typeTrain) {
        this.idTrain = idTrain;
        this.numeroTrain = numeroTrain;
        this.typeTrain = typeTrain;
    }

    public Train(String numeroTrain, String typeTrain) {
        this.numeroTrain = numeroTrain;
        this.typeTrain = typeTrain;
    }

    public int getIdTrain() {
        return idTrain;
    }

    public void setIdTrain(int idTrain) {
        this.idTrain = idTrain;
    }

    public String getNumeroTrain() {
        return numeroTrain;
    }

    public void setNumeroTrain(String numeroTrain) {
        this.numeroTrain = numeroTrain;
    }

    public String getTypeTrain() {
        return typeTrain;
    }

    public void setTypeTrain(String typeTrain) {
        this.typeTrain = typeTrain;
    }

    @Override
    public String toString() {
        return numeroTrain + " (" + typeTrain + ")";
    }
}
