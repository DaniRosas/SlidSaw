package com.example.dani.slidsaw_beta;

/**
 * Created by dani on 16/12/15.
 */

import android.graphics.Bitmap;
import android.util.Log;

import static com.example.dani.slidsaw_beta.LevelActivity.*;

public class Imagen{

    // Variables globals
    private Bitmap bm;
    public String idImagen, idPos;
    private int x_pos, y_pos;

    // Constructor de 'Imagen' buït
    public Imagen (){}

    // Constructor de imagen passant un bitmap i un ID
    public Imagen (Bitmap bitmap, String s){
        idImagen = s;
        idPos = idImagen;
        bm = bitmap;
    }

    // Getters:
    public Bitmap getBitmap(){ return this.bm; }
    public String getPos(){ return this.idPos; }
    public String getId(){ return this.idImagen; }
    public int getX(){ return Math.round(x_pos); }
    public int getY(){ return Math.round(y_pos); }

    // Setters:
    public void setBitmap(Bitmap bitmap){ this.bm = bitmap; }
    public void setPos(String s){ this.idPos = s; }
    public void setId(String s){ this.idImagen = s; }
    public void setX(float x){ this.x_pos = Math.round(x); }
    public void setY(float y){ this.y_pos = Math.round(y); }
}