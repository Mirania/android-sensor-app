package eu.qhealth;

import java.util.Locale;

public class Globals {
    private static String username;
    private static int height;
    private static float weight;
    private static boolean intromode=true; //true=new user; false=updating user
    private static boolean usertype=false; //false=client; true=trainer
    public static boolean checkIfFirstTime=true;

    public static void setUsername(String u) {
        username=u;
    }

    public static String getUsername() {
        return username;
    }

    public static void setIntromode(boolean b) {
        intromode=b;
    }

    public static void setUsertype(boolean b) {
        usertype=b;
    }

    public static boolean getIntromode() {
        return intromode;
    }

    public static boolean getUserType() {
        return usertype;
    }

    public static void setWeight(float w) {
        weight=w;
    }

    public static void setHeight(int h) {
        height=h;
    }

    public static float getBMI() {
        float heightM = ((float) height)/100;
        return weight / (heightM*heightM);
    }

    public static String getBMIstring() {
        float bmi = getBMI();
        String s = String.format(Locale.US, "%.1f ",bmi);
        if (Locale.getDefault().getLanguage().equals("pt")) {
            if (bmi < 18)
                s += "(muito baixo!)";
            else if (bmi < 25)
                s += "(normal)";
            else if (bmi < 30)
                s += "(alto!)";
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

}
