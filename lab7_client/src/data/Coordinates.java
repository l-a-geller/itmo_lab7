package data;

import java.io.Serializable;

public class Coordinates implements Comparable<Coordinates>, Serializable {
    private Integer x; //Поле не может быть null
    private float y; //Поле не может быть null

    public Integer getX() {
        return x;
    }
    public float getY() {
        return y;
    }

    public Coordinates(String data){
        String[] coord = data.split(":");
        this.x = Integer.parseInt(coord[0]);
        this.y = Float.parseFloat(coord[1]);
    }
    @Override
    public int compareTo(Coordinates coords){
        return (int)(this.x * this.x + this.y * this.y - (coords.x * coords.x + coords.y * coords.y));
    }
    public int toInt(){
        return (int)(this.x * this.x + this.y * this.y);
    }
    public String toString(){
        return this.x + ":" + this.y;
    }
}