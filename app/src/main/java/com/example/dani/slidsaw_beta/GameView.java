package com.example.dani.slidsaw_beta;

/**
 * Created by dani on 16/12/15.
 */

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.util.AttributeSet;
import java.util.Random;

public class GameView extends View {

    public static int x_pos, y_pos;
    public int rand;
    public static float w,h;
    public static boolean first = true;
    public String[] posicions_random;
    public static long milis;
    public static boolean win;

    private int level = LevelActivity.level, fil_act, col_act;
    private float SIZE_img;
    private String img_vacio = Integer.toString(level-1) + Integer.toString(level-1);
    private Imagen[][] tablero, tablero_aux = new Imagen[level][level];

    // Constructor de la classe GameView:
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    // Métode init()
    private void init() {
        // Paint per "esborrar" la imatge quan es mou
        Paint borrado = new Paint();
        borrado.setStyle(Paint.Style.FILL);
        borrado.setColor(Color.WHITE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        rand = level;
        posicions_random = randomize(rand);
        super.onDraw(canvas);

        w = getWidth();
        h = getHeight();
        float ancho_tab = w - 32;
        float x_tab = 8;
        float y_tab = (h - w) / 2;
        SIZE_img = ancho_tab / level;

        // Dibujo de las casillas
        for (int fil = 0; fil < level; fil++) {
            for (int col = 0; col < level; col++) {

                //Se accede al bitmap de la imagen y se reescala
                Bitmap bitmap = tablero[fil][col].getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap, (int) SIZE_img + 1, (int) SIZE_img + 1, true);
                //Para comprovar que las casillas se pintan correclevelente
                tablero[fil][col].setBitmap(bitmap);
                int padding = 4;
                float x_inicio = x_tab + col * (SIZE_img + padding);
                float y_inicio = y_tab + fil * (SIZE_img + padding);
                //Se guardan las posiciones donde se empieza a pintar la Imagen
                tablero[fil][col].setX(x_inicio);
                tablero[fil][col].setY(y_inicio);
                //Se pinta el bitmap de la imagen
                canvas.drawBitmap(tablero[fil][col].getBitmap(), x_inicio, y_inicio, null);
                tablero_aux[fil][col] = tablero[fil][col];
            }
        }

        if(first) {
            int counter = 0;
            while (counter < (level*level)) {
                for (int i = 0; i < level; i++) {
                    for (int j = 0; j < level; j++) {
                        int fil = Character.getNumericValue(posicions_random[counter].charAt(0));
                        int col = Character.getNumericValue(posicions_random[counter].charAt(1));
                        updateImagerandom(i, j, fil, col);
                        Log.i("POS/ID", "En posicion: " + i + j + ", está el ID: " + tablero[i][j].getId());
                        counter++;
                    }
                }
            }
            first = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (win){
            return false;
        }
        int action = event.getActionMasked();
        //Se hace el paint para que se pinte encima del bitmap que se ha movido

        float x = event.getX();
        float y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // Mainloop, es per tallar el doble bucle en quan es presenti un break
                mainloop:
                for (int fil = 0; fil < level; fil++) {
                    for (int col = 0; col < level; col++) {

                        x_pos = tablero[fil][col].getX();
                        y_pos = tablero[fil][col].getY();

                        //Si corresponde a este rango de coordenadas
                        if (x_pos < x &&
                                x < x_pos + SIZE_img &&
                                y_pos < y &&
                                y < y_pos + SIZE_img) {

                            fil_act = fil;
                            col_act = col;
                            break mainloop;
                        }
                    }
                }

            boolean vacio = estaVacio(fil_act,col_act);
            if(!vacio){
                if (CompruebaDra(fil_act, col_act)) {
                    int col_next = col_act + 1;
                    updateImage(fil_act, col_act, fil_act, col_next);
                } else if (CompruebaIzq(fil_act, col_act)) {
                    int col_prev = col_act - 1;
                    updateImage(fil_act, col_act, fil_act, col_prev);
                } else if (CompruebaSup(fil_act, col_act)) {
                    int fil_sup = fil_act - 1;
                    updateImage(fil_act, col_act, fil_sup, col_act);
                } else if (CompruebaInf(fil_act, col_act)) {
                    int fil_inf = fil_act + 1;
                    updateImage(fil_act, col_act, fil_inf, col_act);
                }
            }
                for(int id_i = 0; id_i < level; id_i++){
                    for(int id_j = 0; id_j < level; id_j++){
                        Log.i("ID_pos","ID: " + tablero[id_j][id_i].getId() + " en posicion: " + tablero[id_j][id_i].getPos());
                    }
            }
        }
        win(tablero);
        return true;
    }

    public boolean estaVacio(int fil, int col) {
        String iden = tablero[fil][col].getId();
        return iden.equals(img_vacio);
    }

    public boolean CompruebaDra(int fil, int col) {
        int col_dra = col + 1;
        if (col_dra >= level) return false;
        String pos_dra = tablero[fil][col_dra].getPos();
        return pos_dra.equals(img_vacio);
    }

    public boolean CompruebaIzq(int fil, int col) {
        int col_izq = col - 1;
        if (col_izq < 0)return false;
        String pos_vacio = tablero[fil][col_izq].getPos();
        return pos_vacio.equals(img_vacio);
    }

    public boolean CompruebaSup(int fil, int col) {
        int fila_sup = fil - 1;
        if (fila_sup < 0)return false;
        String pos_vacio = tablero[fila_sup][col].getPos();
        return pos_vacio.equals(img_vacio);
    }

    public boolean CompruebaInf(int fil, int col) {
        int fila_inf = fil + 1;
        if (fila_inf == level) return false;
        String pos_vacio = tablero[fila_inf][col].getPos();
        return pos_vacio.equals(img_vacio);
    }

    private void updateImage(int fil_actual, int col_actual, int fil_mov, int col_mov){
        Log.i("update", Integer.toString(fil_mov) + Integer.toString(col_mov) + " " + fil_actual + col_actual);
        Imagen imagen;
        imagen = tablero[fil_mov][col_mov];
        tablero[fil_mov][col_mov] = tablero[fil_actual][col_actual];
        tablero[fil_actual][col_actual] = imagen;
        invalidate();
    }

    private void updateImagerandom(int fil_actual, int col_actual, int fil_mov, int col_mov){
        tablero[fil_actual][col_actual] = tablero_aux[fil_mov][col_mov];
        invalidate();
    }

    // Métode 'randomize()'
    public static String[] randomize(int size) {
        String[] posicions_inici = new String[size * size];
        int count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                String celda = Integer.toString(i) + Integer.toString(j);
                posicions_inici[count] = celda;
                count++;
            }
        }
        Random rgen = new Random();  // Random number generator
        for (int i = 0; i < posicions_inici.length; i++) {
            int randomPosition = rgen.nextInt(posicions_inici.length);
            String temp = posicions_inici[i];
            posicions_inici[i] = posicions_inici[randomPosition];
            posicions_inici[randomPosition] = temp;
        }
        return posicions_inici;
    }

    public void setTablero(Imagen[][] tablero) {
        this.tablero = tablero;
    }

    public void win(Imagen[][] tablero){
        GameView g = (GameView) findViewById(R.id.gameview);

        int counter = 0;
        for(int i = 0; i < level; i++ ){
            for(int j = 0; j < level; j++){
                String aux = Integer.toString(i) + Integer.toString(j);
                if(tablero[i][j].getId().equals(aux)){
                    counter ++;
                }
            }
        }
        if(counter == level*level){
            win = true;
            g.setEnabled(false);
            g.setClickable(false);
            MainActivity.crono.stop();
            int sec, min, ms;
            milis = SystemClock.elapsedRealtime() - MainActivity.crono.getBase();
            sec = (int) milis/1000;
            min = sec/60;
            ms = (int) milis%1000;
            int viewMin = min, viewSec = sec, viewMs = ms;

            if(viewSec >= 60) viewSec = sec - min*60;

            if(viewMin >= 60) viewMin = min - 60;

            MainActivity.timeCrono.setText("Muy bien, has ganado! Has tardado : " + viewMin + "m " + viewSec + "s " + viewMs + "ms");
            MainActivity.timeCrono.setVisibility(VISIBLE);
            MainActivity.crono.setVisibility(INVISIBLE);

            ObjectAnimator.ofFloat(g, "alpha", 1f, 0f).setDuration(1000).start();

            MainActivity.et.setVisibility(VISIBLE);
            MainActivity.ok_btn.setVisibility(VISIBLE);

            //ObjectAnimator.ofFloat(Main.et, "alpha", 0f, 1f).setDuration(300).start();
            //ObjectAnimator.ofFloat(Main.ok_btn, "alpha", 0f, 1f).setDuration(300).start();
        }
        else{
            Log.i("win", "Still not winning ");
        }
    }


}