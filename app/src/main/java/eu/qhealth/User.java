package eu.qhealth;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;

public class User implements Serializable {
    private String name;
    private String typeofuser;
    private int age;
    private int height;
    private float weight;
    private String typeofbody;
    private String treiner;
    private String pword;
    private int amt;
    private float score;
    private int image;

    public User(){};
    //On Creation of the user
    public User(String name,int age,float weight,int height,String typeofuser,String typeofbody,String pword){
        this.name=name;
        this.pword=pword;
        this.typeofuser=typeofuser;
        this.age=age;
        this.height=height;
        this.weight=weight;
        this.typeofbody=typeofbody;
        amt=0;
        score=0;
    }

    public String getInfo() {
        if (Locale.getDefault().getLanguage().equals("pt")) {
            String s = "";
            if (getAmt()>0) s = "\nJá efetuou "+getAmt()+" exercícios.\nRating médio de "+getRatingString()+".";
            return "Tem corpo "+typeofbody+".\n"+
                    "BMI de "+getBMIstring()+".\n"+
                    "Tem "+(Calendar.getInstance().get(Calendar.YEAR)-age)+" anos."+s;

        } else {
            String s = "";
            if (getAmt()>0) s = "\nThey've completed "+getAmt()+" exercises.\nAverage rating of "+getRatingString()+".";
            return "Has a " + typeofbody + " body type.\n" +
                    "BMI of " + getBMIstring() + ".\n" +
                    "Is " + (Calendar.getInstance().get(Calendar.YEAR) - age) + " years old."+s;

        }
    }

    public float getBMI() {
        float heightM = ((float) height)/100;
        return weight / (heightM*heightM);
    }

    public String getBMIstring() {
        float bmi = getBMI();
        String s = String.format(Locale.US, "%.1f ",bmi);
        if (Locale.getDefault().getLanguage().equals("pt")) {
            if (bmi < 18)
                s += "(muito baixo!)";
            else if (bmi < 25)
                s += "(normal)";
            else if (bmi < 30)
                s += "(alto)";
            else
                s += "(muito alto!)";
            return s;
        } else {
            if (bmi < 18)
                s += "(very low!)";
            else if (bmi < 25)
                s += "(normal)";
            else if (bmi < 30)
                s += "(high)";
            else
                s += "(very high!)";
            return s;
        }
    }

    public String getRatingString() {
        float rating = getRating();
        return String.format(Locale.US, "%.1f/10",rating);
    }

    public int getImage() {return image;}

    public void setImage(int i) {image=i;}

    public int getAmt() {return amt;}

    public void setAmt(int amt) {this.amt=amt;}

    public void setScore(float score) {this.score=score;}

    public void incAmt() { amt++; }

    public float getScore() {return score;}

    public void addScore(float score) {this.score+=score;}

    public float getRating() {
        if (amt==0) return 0;
        else return score/(float)amt;
    }

    public String getName() {
        return name;
    }

    public String getPword() { return pword; }

    public void setPword(String pword) { this.pword = pword; }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeofuser() {
        return typeofuser;
    }

    public void setTypeofuser(String typeofuser) {
        this.typeofuser = typeofuser;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getTypeofbody() {
        return typeofbody;
    }

    public void setTypeofbody(String typeofbody) {
        this.typeofbody = typeofbody;
    }

    public String getTreiner() {
        return treiner;
    }

    public void setTreiner(String treiner) {
        this.treiner = treiner;
    }

    public String toString() {
        return name+":"+getBMI()+":"+getRating();
    }
}
