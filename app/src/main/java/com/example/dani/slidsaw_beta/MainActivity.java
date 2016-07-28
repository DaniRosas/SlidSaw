package com.example.dani.slidsaw_beta;

/**
 * Created by dani on 16/12/15.
 */


// 1)TODO NO PERMITIR TIRAR ATRÀS UNA VEZ SE HA GANADO
// TODO ANIMACIONES


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{

    private Imagen[][] tablero;
    private Bitmap[][] b;
    private Bitmap bmp;
    private int level = LevelActivity.level, score = 0;

    static TextView timeCrono;
    static Chronometer crono;
    static EditText et;
    static Button ok_btn;

    long timeWhenStopped = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);


        et = (EditText) findViewById(R.id.editText);
        ok_btn = (Button) findViewById(R.id.btn_ok);
        et.setVisibility(View.INVISIBLE);
        ok_btn.setVisibility(View.INVISIBLE);

        crono = (Chronometer) findViewById(R.id.Crono);
        timeCrono = (TextView) findViewById(R.id.time);
        timeCrono.setVisibility(View.INVISIBLE);
        timeCrono.setText("Time: 0m 0s 0ms");

        Typeface dejavu = TypefacesUtils.get(this, "fonts/Roboto-Light.ttf");

        crono.setTypeface(dejavu);
        et.setTypeface(dejavu);
        timeCrono.setTypeface(dejavu);
        ok_btn.setTypeface(dejavu);
        GameView G = (GameView) findViewById(R.id.gameview);

        float we = G.w;
        float he = G.h;
        float px = timeCrono.getTextSize();
        float scaledDensity = this.getResources().getDisplayMetrics().scaledDensity;
        px = px/scaledDensity;
        Log.i("Tamaños", "Ancho: " + we + ", altura: " + he + "tamaño de ltra original: " + timeCrono.getTextSize() + ", tamaño letra modificado: " + px);
        timeCrono.setTextSize(TypedValue.COMPLEX_UNIT_SP, px);


        // Generem el drawable de la casella buïda
        Drawable drawable = getResources().getDrawable(R.drawable.borrado);
        Bitmap bmp_borrado = drawableToBitmap(drawable);

        // Si rebem dades extra del intent les descodifiquem i fiquem a bmp:;
        if(getIntent().hasExtra("byteArray")) {
            bmp = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("byteArray"), 0, getIntent().getByteArrayExtra("byteArray").length);
        }

        // Creem un nou array de bitmaps de 3x3
        b = new Bitmap[level][level];

        // Variables i doble bucle for per crear la matriu de subimatges
        int x = 0;
        int y = 0;
        int l = bmp.getWidth()/level;
        int h = bmp.getHeight()/level;

        for(int i = 0; i < level; i++){
            for(int j = 0; j < level; j++){
                // Si la suimatge no es la última de la matriu pinta subimatge de la riginal
                if(!(i == level-1 && j== level -1)) {
                    Bitmap bmp_chunk = Bitmap.createBitmap(bmp, x, y, l, h);
                    b[i][j] = bmp_chunk;
                    x = x + l;
                }
                // Si es la última, pinta subimatge buïda
                else b[i][j] = bmp_borrado;
            }
            x = 0;
            y = y + h;
        }
        // Crida a la funció Inicio()
        Inicio();
        G.setTablero(tablero);

        /*--------------------------------------------------------------------------------*/
        crono.setBase(SystemClock.elapsedRealtime());
        float px_c = crono.getTextSize();
        float scaledDensity_c = this.getResources().getDisplayMetrics().scaledDensity;
        px_c = px_c/scaledDensity;
        crono.setTextSize(TypedValue.COMPLEX_UNIT_SP, px_c);
        crono.start();
        /*---------------------------------------------------------------------------------*/

    }

    public void Inicio(){
        int nFil = b.length;    // El número de files es la longitud de la matriu 'b'
        int nCol = b[0].length; // El número de columnes es la longitud de una fila de la matriu 'b'
        tablero = new Imagen[nFil][nCol];

        for(int fil = 0; fil < level; fil++) {
            for (int col = 0; col < level; col++) {
                // Creem ID de la subimatge concatenant el numero de fila i columna
                String s  = Integer.toString(fil) + Integer.toString(col);
                // Es genera una nova imatge amb numero de fila, columna i ID
                Imagen imagen = new Imagen(b[fil][col], s); // Passem un bitmap de la matriu 'b' i el seu ID
                // s'inserta la imatge a la matriu 'imagenes'
                tablero[fil][col] = imagen;
                Log.i("tablero: ", "" + tablero[fil][col]);
            }
        }
    }

    public Bitmap drawableToBitmap(Drawable drawable){
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK && !GameView.win)) {

            crono.stop();
            timeWhenStopped = crono.getBase() - SystemClock.elapsedRealtime();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(R.string.d1)
                    .setPositiveButton(R.string.d2, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            change_level();
                        }
                    })
                    .setNegativeButton(R.string.d3, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            change_pic();

                        }
                    }).setNeutralButton(R.string.d4, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    crono.setBase(SystemClock.elapsedRealtime()+timeWhenStopped);
                    crono.start();
                    timeWhenStopped = 0;
                }
            }).setCancelable(false);

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            return false;
        }

        return (keyCode == KeyEvent.KEYCODE_BACK && GameView.win) || super.onKeyDown(keyCode, event);

    }

    public void change_level(){
        Intent intent = new Intent(this, LevelActivity.class);
        startActivity(intent);
        overridePendingTransition(R.animator.right_out, R.animator.right_in);
        crono.setBase(SystemClock.elapsedRealtime());
        crono.stop();
    }

    public void change_pic(){
        Intent intent = new Intent(this, ChoosePhotoActivity.class);
        startActivity(intent);
        overridePendingTransition(R.animator.right_out, R.animator.right_in);
        crono.setBase(SystemClock.elapsedRealtime());
        crono.stop();
    }

    public void onScore(View v){
        Intent intent = new Intent(this, ScoresActivity.class);
        String nom = et.getText().toString();
        long tiempo = GameView.milis;
        intent.putExtra("nom", nom);
        intent.putExtra("tiempo", tiempo);
        startActivity(intent);
        overridePendingTransition(R.animator.left_in, R.animator.left_out);
        GameView.win = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        Intent i = new Intent(this, AudioService.class);
        i.putExtra("action", AudioService.PAUSE);
        startService(i);
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent i = new Intent(this, AudioService.class);
        i.putExtra("action", AudioService.START);
        startService(i);
    }
}