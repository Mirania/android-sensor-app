package eu.qhealth;

public class GlobalTrainingProgram {
    private int id;
    private String namecliente;
    private int idexercicio;
    public GlobalTrainingProgram(){};
    public GlobalTrainingProgram(String namecliente,int idexercicio){
        this.namecliente=namecliente;
        this.idexercicio=idexercicio;
    }

    public GlobalTrainingProgram(int id, String namecliente,int idexercicio){
        this.id=id;
        this.namecliente=namecliente;
        this.idexercicio=idexercicio;
    }

    public String toString() {
        return id+":"+idexercicio+":"+namecliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int idunico) {
        this.id= idunico;
    }

    public String getNamecliente() {
        return namecliente;
    }

    public void setNamecliente(String idcliente) {
        this.namecliente = idcliente;
    }

    public int getIdexercicio() {
        return idexercicio;
    }

    public void setIdexercicio(int idexercicio) {
        this.idexercicio = idexercicio;
    }
}
