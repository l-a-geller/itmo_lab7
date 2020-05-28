package data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class LabWork implements Comparable<LabWork>, Serializable {
    private int id;// = this.hashCode(); //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDate creationDate;// = java.time.LocalDate.now(); //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Float minimalPoint; //Поле не может быть null, Значение поля должно быть больше 0
    private long maximumPoint = 0; //Значение поля должно быть больше 0
    private Difficulty difficulty; //Поле может быть null
    private Discipline discipline; //Поле может быть null
    private String user;

    public LabWork(String name, Coordinates coordinates, Float minimalPoint, long maximumPoint, Difficulty difficulty, Discipline discipline){
        this.name = name;
        this.coordinates = coordinates;
        this.minimalPoint = minimalPoint;
        this.maximumPoint = maximumPoint;
        this.difficulty = difficulty;
        this.discipline = discipline;
        id = this.hashCode();
        creationDate = java.time.LocalDate.now();
    }

    public LabWork(int id, String name, Coordinates coordinates, Float minimalPoint, long maximumPoint, Difficulty difficulty, Discipline discipline, String user){
        // DB version
        this.name = name;
        this.coordinates = coordinates;
        this.minimalPoint = minimalPoint;
        this.maximumPoint = maximumPoint;
        this.difficulty = difficulty;
        this.discipline = discipline;
        this.id = id;
        creationDate = java.time.LocalDate.now();
        this.user = user;
    }

    public long getMaximumPointer(){
        return maximumPoint;
    }
    public LabWork(){ }

    public void setUser(String login){
        this.user = login;
    }
    public String getUser(){
        return this.user;
    }

    public String getName(){
        return this.name;
    }
    public int getId(){
        return this.id;
    }
    public Coordinates getCoordinates(){
        return this.coordinates;
    }
    public String getMinPoint(){
        return String.valueOf(this.minimalPoint);
    }
    public Float getMin(){return this.minimalPoint;}
    public String getMaxPoint(){
        return String.valueOf(this.maximumPoint);
    }
    public Long getMax(){return this.maximumPoint;}
    public String getDifficulty(){
        return this.difficulty.name();
    }
    public Difficulty getDiff(){ return this.difficulty;}
    public String getDiscipline(){
        return this.discipline.toString();
    }
    public Discipline getDisc() {return this.discipline;}
    public String toString(){
        return ":name:"+name+":coordinates:"+coordinates.toString()+":creationDate:"+minimalPoint+":maximumPoint:"+maximumPoint+":difficulty:"+difficulty+":discipline:"+discipline;
    }
    public int compareTo(LabWork l){
        return (int)(l.name.compareToIgnoreCase(this.getName()));
    }

    public String print(){
        return "\nid "+ id +"\nname " + name + "\ncoordinates " + coordinates.toString() + "\ncreationDate " + creationDate +"\nminimalPoint " + minimalPoint + "\nmaximumPoint " + maximumPoint + "\ndifficulty " + difficulty + "\ndiscipline " + discipline + "\nauthor " + user + "\n";
    }

    public ArrayList<String> getFieldNames(){
        ArrayList<String> res = new ArrayList<>();
        for (Field f : this.getClass().getDeclaredFields()){
            try {
                if (f.get(this) == null){
                    res.add(f.getName());
                }else if ( f.getLong(this) == 0){
                    res.add(f.getName());
                }
            } catch (IllegalAccessException e) {
                System.out.println("Field is damaged.");
            } catch (IllegalArgumentException e){
                //
            }
        }
        return res;
    }

    public ArrayList<String> getAllFieldNames(){
        ArrayList<String> res = new ArrayList<>();
        for (Field f : this.getClass().getDeclaredFields()){
            res.add(f.getName());
        }
        return res;
    }

    public ArrayList<Field> getAllFields(){
        ArrayList<Field> res = new ArrayList<>();
        for (Field f : this.getClass().getDeclaredFields()){
            try {
                f.get((Object) this);
                res.add(f);
            }catch (IllegalAccessException e){
                System.out.println("Field is damaged.");
            }
        }
        return res;
    }

    public ArrayList<Field> getFields(){
        ArrayList<Field> res = new ArrayList<>();
        for (Field f : this.getClass().getDeclaredFields()) {
            try {
                if (f.get(this) == null) {
                    res.add(f);
                } else if (f.getLong( this) == 0) {
                    res.add(f);
                }
            } catch (IllegalAccessException e) {
                System.out.println("Field is damaged.");
            } catch (IllegalArgumentException e) {
                //
            }
        }
        return res;
    }

    public String update(){
        name += "1";
        return "Name updated: " + name;
    }
    public boolean checkId(int identificator){
        if (identificator == this.id){
            return true;
        }
        return false;
    }
}


/*
CREATE TABLE labworks (
    name varchar NOT NULL UNIQUE ,
    coordinates_x integer NOT NULL,
    coordinates_y float NOT NULL,
    minimal_point float check ( minimal_point > 0 ) NOT NULL,
    maximum_point bigint check ( maximum_point > minimal_point ),
    difficulty_index integer check (difficulty_index < 3 and difficulty_index > -1),
    discipline_name varchar NOT NULL,
    discipline_lecture_hours integer,
    discipline_practice_hours integer NOT NULL ,
    discipline_selfstudy_hours bigint,
    author varchar NOT NULL
);*/