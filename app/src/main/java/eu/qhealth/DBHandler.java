package eu.qhealth;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;
    // Database Name
    private static final String DATABASE_NAME = "qHealth";
    // Users table name
    private static final String USERS = "Users";
    //Exercises table name
    private static final String EXERCISES="Exercises";
    //GLOBAL table name
    private static final String GLOBAL="Global";
    //Global
    private static final String KEY_ID = "id";
    // Users Table Columns names
    private static final String KEY_NAME = "name";
    private static final String KEY_PWORD = "pword";
    private static final String KEY_AGE ="age"; //is actually birth year
    private static final String KEY_WEIGHT="weight";
    private static final String KEY_HEIGHT="height";
    private static final String KEY_TYPEUSER="typeofuser";
    private static final String KEY_TYPEBODY="typeofbody";
    private static final String KEY_TREINER="treiner";
    private static final String KEY_AMTDONE="examount";
    private static final String KEY_TOTALSCORE="totalscore";
    private static final String KEY_IMAGE="image";
    //Exercises Table Columns names
    private static final String KEY_DESCRIPTION="description";
    private static final String KEY_EXTYPE="type";
    //Global
    private static final String KEY_NAMECLIENT="nameclient";
    private static final String KEY_IDEXERCISE="idexercise";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USERS_TABLE = "CREATE TABLE " +USERS+ "("
        + KEY_NAME + " TEXT PRIMARY KEY,"
        + KEY_AGE + " INTEGER," + KEY_WEIGHT + " FLOAT(4,1)," + KEY_HEIGHT+ " INTEGER,"
        + KEY_TYPEUSER + " TEXT," + KEY_TYPEBODY + " TEXT," + KEY_TREINER+ " TEXT,"
        + KEY_PWORD + " TEXT, " + KEY_AMTDONE + " INTEGER, " + KEY_TOTALSCORE + " FLOAT(2,1), " +
                KEY_IMAGE + " INTEGER)";

        String CREATE_EXERCISES_TABLE ="CREATE TABLE " +EXERCISES+ "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DESCRIPTION + " TEXT," +
                KEY_EXTYPE + " TEXT )";
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_EXERCISES_TABLE);
        String CREATE_GLOBAL_TABLE ="CREATE TABLE " +GLOBAL+ "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAMECLIENT+ " TEXT," + KEY_IDEXERCISE+ " INTEGER" + ")";
        db.execSQL(CREATE_GLOBAL_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USERS);
        db.execSQL("DROP TABLE IF EXISTS " + EXERCISES);
        db.execSQL("DROP TABLE IF EXISTS " + GLOBAL);
        onCreate(db);
    }
    public int getNewExID() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT max(id) FROM " + EXERCISES;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0) + 1;
        }
        return 0;
    }
    public int getNewGlobalID() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT max(id) FROM " + GLOBAL;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0) + 1;
        }
        return 0;
    }
    public void fakeDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        onUpgrade(db, 1,1);

        User u = new User();
        u.setName("test");
        u.setPword("p");
        u.setTypeofbody("Fat");
        u.setAge(1995);
        u.setHeight(176);
        u.setWeight((float) 82.5);
        u.setTypeofuser("client");
        u.setTreiner("testetr");
        u.setAmt(8);
        u.setScore((float)79);

        User u2 = new User();
        u2.setName("test2");
        u2.setPword("p2");
        u2.setTypeofbody("Fit");
        u2.setAge(1997);
        u2.setHeight(195);
        u2.setWeight((float) 88.5);
        u2.setTypeofuser("client");
        u2.setTreiner("testetr");
        u2.setAmt(15);
        u2.setScore((float)45);

        User u3 = new User();
        u3.setName("testetr");
        u3.setPword("p");
        u3.setTypeofbody("Fit");
        u3.setAge(1995);
        u3.setHeight(190);
        u3.setWeight((float) 100.5);
        u3.setTypeofuser("trainer");

        User u4 = new User();
        u4.setName("test3");
        u4.setPword("p3");
        u4.setTypeofbody("Fit");
        u4.setAge(1995);
        u4.setHeight(150);
        u4.setWeight((float) 98.5);
        u4.setTreiner("testetr");
        u4.setTypeofuser("client");
        u4.setAmt(60);
        u4.setScore((float)450);

        User u5 = new User();
        u5.setName("test5");
        u5.setPword("p5");
        u5.setTypeofbody("Fat");
        u5.setAge(1995);
        u5.setHeight(166);
        u5.setWeight((float) 88.5);
        u5.setTreiner("supertrainer");
        u5.setTypeofuser("client");
        u5.setAmt(12);
        u5.setScore((float)110.1);

        User u6 = new User();
        u6.setName("supertrainer");
        u6.setPword("ps");
        u6.setTypeofbody("Fit");
        u6.setAge(1995);
        u6.setHeight(100);
        u6.setWeight((float) 58.5);
        u6.setTypeofuser("trainer");

        User u7 = new User();
        u7.setName("test7");
        u7.setPword("p7");
        u7.setTypeofbody("Fat");
        u7.setAge(1995);
        u7.setHeight(140);
        u7.setWeight((float) 118.5);
        u7.setAmt(0);
        u7.setScore((float)0);
        u7.setTypeofuser("client");

        addUSERS(u);
        addUSERS(u2);
        addUSERS(u3);
        addUSERS(u4);
        addUSERS(u5);
        addUSERS(u6);
        addUSERS(u7);
        for (int i=1;i<8;i++) {
            Exercises e = new Exercises("Push-ups","Do them properly.");
            addAndLinkExercise(e,u2);
        }
        Exercises e5 = new Exercises("Diet","Eat mainly salad today.");
        addAndLinkExercise(e5,u);
        Exercises e4 = new Exercises("Push-ups","Exercise well.");
        addAndLinkExercise(e4,u);
        Exercises e1 = new Exercises("Abdominals","Try your best.");
        addAndLinkExercise(e1,u);
        Exercises e2 = new Exercises("Jogging","Exercise well.");
        addAndLinkExercise(e2,u);
        Exercises e3 = new Exercises("Sleep","Sleep at least 8 hours.");
        addAndLinkExercise(e3,u);
        
        Globals.checkIfFirstTime=false;

    }
    public User getAllUserStats(String n) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT name,pword,age,weight,height,typeofbody,typeofuser,treiner,examount,totalscore,image" +
                " FROM " + USERS + " WHERE "
                +"name = '"+n+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        User u = new User();
        if (cursor.moveToFirst()) {
            u.setName(cursor.getString(0));
            u.setPword(cursor.getString(1));
            u.setAge(cursor.getInt(2));
            u.setWeight(cursor.getFloat(3));
            u.setHeight(cursor.getInt(4));
            u.setTypeofbody(cursor.getString(5));
            u.setTypeofuser(cursor.getString(6));
            u.setTreiner(cursor.getString(7));
            u.setAmt(cursor.getInt(8));
            u.setScore(cursor.getFloat(9));
            u.setImage(cursor.getInt(10));
        }
        return u;
    }
    public boolean isEmpty() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT name FROM " + USERS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        return !cursor.moveToFirst();
    }
    public void setImage(String n, int i) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME,n);
        values.put(KEY_IMAGE,i);
        db.update(USERS,values,"name='"+n+"'",null);
    }
    public Integer getImage(String n) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT name,image FROM " + USERS + " WHERE "
                +"name = '"+n+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(1);
        } else return null;
    }
    //Add users
    public void addUSERS(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME,user.getName());
        values.put(KEY_AGE,user.getAge());
        values.put(KEY_WEIGHT,user.getWeight());
        values.put(KEY_HEIGHT,user.getHeight());
        values.put(KEY_TYPEUSER,user.getTypeofuser());
        values.put(KEY_TYPEBODY,user.getTypeofbody());
        values.put(KEY_TREINER,user.getTreiner());
        values.put(KEY_PWORD,user.getPword());
        values.put(KEY_TOTALSCORE,user.getScore());
        values.put(KEY_AMTDONE,user.getAmt());
        values.put(KEY_IMAGE,1);
        db.insert(USERS, null, values);
        db.close();
    }
    public void updateScore(User u) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_AMTDONE,u.getAmt());
        values.put(KEY_TOTALSCORE,u.getScore());
        db.update(USERS,values,"name='"+u.getName()+"'",null);
    }
    public Boolean[] validateLogin(String n, String p) {
        Boolean[] res = new Boolean[2];
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT name,pword,weight,height,typeofuser FROM " + USERS + " WHERE "
                            +"name = '"+n+"' AND pword = '"+p+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            res[0]=true;
            Globals.setUsername(cursor.getString(0));
            Globals.setWeight(cursor.getFloat(2));
            Globals.setHeight(cursor.getInt(3));
            if (cursor.getString(4).equals("trainer")) res[1]=true;
            else res[1]=false;
            return res;
        } else return new Boolean[]{false,false};
    }
    public boolean validateUsername(String n) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT name FROM " + USERS + " WHERE "
                +"name = '"+n+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor.moveToFirst();
    }
    public String[] getUserInfo(String n) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT name,pword,age,weight,height,typeofbody FROM " + USERS + " WHERE "
                +"name = '"+n+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            String name = cursor.getString(0);
            String pw = cursor.getString(1);
            Integer age = cursor.getInt(2);
            Float weight = cursor.getFloat(3);
            Integer height = cursor.getInt(4);
            String body = cursor.getString(5);
            return new String[]{name,pw,age.toString(),weight.toString(),height.toString(),body};
        } else return new String[6];

    }
    public User getUserStats(String n) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT "+KEY_AMTDONE+","+KEY_TOTALSCORE+" FROM " + USERS + " WHERE "
                +"name = '"+n+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            int amt = cursor.getInt(0);
            float score = cursor.getFloat(1);
            User u = new User();
            u.setName(n);
            u.setScore(score);
            u.setAmt(amt);
            return u;
        } else return new User();
    }
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_AGE,user.getAge());
        values.put(KEY_WEIGHT,user.getWeight());
        values.put(KEY_HEIGHT,user.getHeight());
        values.put(KEY_TYPEBODY,user.getTypeofbody());
        values.put(KEY_PWORD,user.getPword());
        db.update(USERS,values,"name='"+user.getName()+"'",null);
    }
    public ArrayList<User> getClients(String n) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT name,age,weight,height,typeofbody,examount,totalscore FROM " + USERS + " WHERE "
                + KEY_TYPEUSER + " = 'client' AND " + KEY_TREINER + " = '" + n + "'";
        ArrayList<User> usr = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                Integer age = cursor.getInt(1);
                Float weight = cursor.getFloat(2);
                Integer height = cursor.getInt(3);
                String body = cursor.getString(4);
                Integer amt = cursor.getInt(5);
                Float score = cursor.getFloat(6);
                User u = new User();
                u.setName(name);
                u.setAge(age);
                u.setWeight(weight);
                u.setHeight(height);
                u.setTypeofbody(body);
                u.setAmt(amt);
                u.setScore(score);
                usr.add(u);
            } while (cursor.moveToNext());
        }
        return usr;
    }
    public ArrayList<User> getFreeClients() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT name,age,weight,height,typeofbody FROM " + USERS + " WHERE "
                + KEY_TYPEUSER + " = 'client' AND " + KEY_TREINER + " IS NULL";
        ArrayList<User> usr = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                Integer age = cursor.getInt(1);
                Float weight = cursor.getFloat(2);
                Integer height = cursor.getInt(3);
                String body = cursor.getString(4);
                User u = new User();
                u.setName(name);
                u.setAge(age);
                u.setWeight(weight);
                u.setHeight(height);
                u.setTypeofbody(body);
                usr.add(u);
            } while (cursor.moveToNext());
        }
        return usr;
    }
    public boolean approveUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TREINER, Globals.getUsername());
        db.update(USERS, values, "name='" + user.getName() + "'", null);
        return true;
    }
    public ArrayList<Exercises> getExercises(String n) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT idexercise,Exercises.id,nameclient,description,type FROM " +
                GLOBAL + " JOIN " + EXERCISES + " ON "
                + EXERCISES + "." + KEY_ID + "=" + KEY_IDEXERCISE + " WHERE " + KEY_NAMECLIENT + "= '" + n + "'";
        ArrayList<Exercises> ex = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(1);
                String descr = cursor.getString(3);
                String type = cursor.getString(4);
                Exercises e = new Exercises(id,type,descr);
                ex.add(e);
            } while (cursor.moveToNext());
        }
        return ex;
    }
    //Delete Users
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(USERS, KEY_NAME + " = ?",
                new String[] { user.getName() });
        db.close();
    }
    //Select  from users;
    public List<User> getAllUsers() {
        List<User> shopList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + USERS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                User shop = new User();
                shop.setName(cursor.getString(0));
                shop.setAge(Integer.parseInt(cursor.getString(1)));
                shop.setWeight(Float.parseFloat(cursor.getString(2)));
                shop.setHeight(Integer.parseInt(cursor.getString(3)));
                shop.setTypeofuser(cursor.getString(4));
                shop.setTypeofbody(cursor.getString(5));
                shop.setTreiner(cursor.getString(6));
                shop.setPword(cursor.getString(7));
                shopList.add(shop);
            } while (cursor.moveToNext());
        }
        return shopList;
    }
    /*------------------------------------
    -----------------------------------------
    -----------------------------------
     */

    //Add exercises
    public void addEXERCISES(Exercises user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        int id = getNewExID();
        values.put(KEY_ID, id);
        values.put(KEY_EXTYPE, user.getType());
        values.put(KEY_DESCRIPTION,user.getDescription());
        db.insert(EXERCISES, null, values);
        db.close();
    }
    public void addAndLinkExercise(Exercises ex, User u) {
        ex.setId(getNewExID());
        addEXERCISES(ex);
        addGLOBAL(new GlobalTrainingProgram(u.getName(), ex.getId()));
    }
    //Delete exercises
    public void deleteExercise(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(EXERCISES, KEY_ID + " = " + id, null);
        db.close();
    }
    //Select  from exercises;
    public List<Exercises> getAllExercises() {
        List<Exercises> shopList = new ArrayList<Exercises>();
        String selectQuery = "SELECT * FROM " + EXERCISES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Exercises shop = new Exercises();
                shop.setId(Integer.parseInt(cursor.getString(0)));
                shop.setType(cursor.getString(2));
                shop.setDescription(cursor.getString(1));
                shopList.add(shop);
            } while (cursor.moveToNext());
        }
        return shopList;
    }

    /*--------------------------------------------
    -----------------------------------
    ------------------------
     */
    //Add exercises
    public void addGLOBAL(GlobalTrainingProgram user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        int id = getNewGlobalID();
        values.put(KEY_ID, id);
        values.put(KEY_NAMECLIENT,user.getNamecliente());
        values.put(KEY_IDEXERCISE,user.getIdexercicio());
        db.insert(GLOBAL, null, values);
        db.close();
    }
    //Delete exercises
    public void deleteGLOBAL(GlobalTrainingProgram user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(GLOBAL, KEY_ID + " = ?",
                new String[] { String.valueOf(user.getId()) });
        db.close();
    }
    //Select  from exercises;
    public List<GlobalTrainingProgram> getAllGLOBAL() {
        ArrayList<GlobalTrainingProgram> shopList = new ArrayList<GlobalTrainingProgram>();
        String selectQuery = "SELECT * FROM " + GLOBAL;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                GlobalTrainingProgram shop = new GlobalTrainingProgram();
                shop.setId(Integer.parseInt(cursor.getString(0)));
                shop.setNamecliente(cursor.getString(1));
                shop.setIdexercicio(Integer.parseInt(cursor.getString(2)));
                shopList.add(shop);
            } while (cursor.moveToNext());
        }
        return shopList;
    }



}
