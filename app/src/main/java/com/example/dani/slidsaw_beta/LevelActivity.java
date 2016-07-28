package com.example.dani.slidsaw_beta;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class LevelActivity extends AppCompatActivity implements View.OnClickListener{

    Button easy, medium, hard;
    TextView nivel;
    public static int level;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        Typeface dejavu = TypefacesUtils.get(this, "fonts/Roboto-Light.ttf");

        easy = (Button)findViewById(R.id.easy);
        medium = (Button)findViewById(R.id.medium);
        hard = (Button)findViewById(R.id.hard);
        nivel = (TextView) findViewById(R.id.LevelText);

        easy.setTypeface(dejavu);
        medium.setTypeface(dejavu);
        hard.setTypeface(dejavu);
        nivel.setTypeface(dejavu);

        easy.setOnClickListener(this);
        medium.setOnClickListener(this);
        hard.setOnClickListener(this);

    }

    public void onClick (View v){
        Intent intent = new Intent(this, ChoosePhotoActivity.class);
        switch (v.getId()) {
            case R.id.easy:
                level = 3;
                startActivity(intent);
                overridePendingTransition(R.animator.left_in, R.animator.left_out);
                break;
            case R.id.medium:
                level = 4;
                startActivity(intent);
                overridePendingTransition(R.animator.left_in, R.animator.left_out);
                break;
            case R.id.hard:
                level = 5;
                startActivity(intent);
                overridePendingTransition(R.animator.left_in, R.animator.left_out);
                break;
        }
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
