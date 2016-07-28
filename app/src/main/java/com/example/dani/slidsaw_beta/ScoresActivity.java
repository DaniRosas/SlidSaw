package com.example.dani.slidsaw_beta;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Level;


public class ScoresActivity extends AppCompatActivity{

    public static int tiempo;
    private String nombre;
    private static final String DATA_FILE = "PRUEBA.obj";
    private ListView list;
    private ArrayList<Item> items;
    private ItemAdapter adapter;

    TextView item_nombre, item_tiempo, titulo;
    Button button;

    private void dataChanged() {
        adapter.notifyDataSetChanged();
        saveToFile();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        item_nombre = (TextView) findViewById(R.id.item_name);
        item_tiempo = (TextView) findViewById(R.id.item_time);
        button = (Button) findViewById(R.id.ButtonFinalOK);
        titulo = (TextView) findViewById(R.id.ScoresTitulo);

        Typeface dejavu = TypefacesUtils.get(this, "fonts/Roboto-Light.ttf");

        list = (ListView) findViewById(R.id.list);

        nombre = getIntent().getStringExtra("nom");
        tiempo = (int) getIntent().getLongExtra("tiempo",0);

        button.setTypeface(dejavu);
        titulo.setTypeface(dejavu);

        try {
            Log.i("Hola", "Estic al try");
            restoreFromFile();
            Log.i("Hola",""+ items);
        } catch (IOException e) {
            Log.i("Hola", "Estic al catch");
            items = new ArrayList<Item>();
        }

        if(nombre != null){
            items.add(new Item(nombre, tiempo, LevelActivity.level));
        }

        adapter = new ItemAdapter(this, R.layout.item, items);
        list.setAdapter(adapter);
        Log.i("Hola", "Actualizar lista...");

        dataChanged();

      /* list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                dataChanged();
            }
        });*/

    }





    private void saveToFile() {
        try {
            FileOutputStream fos = openFileOutput(DATA_FILE, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(items);
        } catch (FileNotFoundException e) {
            Log.e("ScoreListActivity", "saveToFile: FileNotFoundException");
        } catch (IOException e) {
            Log.e("ScoreListActivity", "saveToFile: IOException");
        }
    }

    private void restoreFromFile() throws IOException {
        try {
            FileInputStream fis = openFileInput(DATA_FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            items = (ArrayList<Item>)ois.readObject();
        } catch (ClassNotFoundException e) {
            Log.e("ScoreListActivity", "restoreFromFile: ClassNotFoundException");
        } catch (OptionalDataException e) {
            Log.e("ScoreListActivity", "restoreFromFile: OptionalDataException");
        } catch (StreamCorruptedException e) {
            Log.e("ScoreListActivity", "restoreFromFile: StreamCorruptedException");
        }
    }
    public void onFinal(View view) {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
        overridePendingTransition(R.animator.left_in, R.animator.left_out);
        Log.i("tempsScores", ""+tiempo);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
            overridePendingTransition(R.animator.right_out, R.animator.right_in);
            return false;
        }
        return super.onKeyDown(keyCode, event);
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