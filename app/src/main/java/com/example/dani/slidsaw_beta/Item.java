package com.example.dani.slidsaw_beta;

/**
 * Created by dani on 14/01/16.
 */
import java.io.Serializable;

public class Item implements Serializable {
    private static final long serialVersionUID = 0L;

    private String name;
    private int time;
    private int level;

    public Item(String name, int temps, int level) {
        this.name = name;
        this.time = temps;
        this.level = level;
    }

    public String  getName()       { return name; }
    public String getTime(){
        int min = this.time/(60*1000);
        int sec = (this.time%60000)/1000;
        int ms = this.time%100;

        String s =  "Nivel " + this.level + ", " + min + ":" + sec + "." + ms;
        return s; }


}