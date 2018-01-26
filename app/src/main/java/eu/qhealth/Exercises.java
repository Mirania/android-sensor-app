package eu.qhealth;

import java.io.Serializable;

public class Exercises implements Serializable {
    private String description;
    private String type;
    private int id;
    public Exercises(){};
    public Exercises(int id,String type, String description){
        this.id=id;
        this.type=type;
        this.description=description;
    }

    public Exercises(String type, String description){
        this.type=type;
        this.description=description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {
        return id+":"+type+":"+description;
    }
}
