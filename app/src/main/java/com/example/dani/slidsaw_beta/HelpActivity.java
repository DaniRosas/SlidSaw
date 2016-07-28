package com.example.dani.slidsaw_beta;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class HelpActivity extends ActionBarActivity {

    TextView Pas1TV, Pas1bTV, Pas2TV, Pas2bTV, Pas3TV, Pas3bTV, Pas4TV, Pas4bTV, HelpTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Typeface dejavu = TypefacesUtils.get(this, "fonts/Roboto-Light.ttf");

        HelpTitle = (TextView) findViewById(R.id.HelpTitle);
        Pas1TV = (TextView) findViewById(R.id.Pas1TV);
        Pas1bTV = (TextView) findViewById(R.id.Pas1bTV);
        Pas2TV = (TextView) findViewById(R.id.Pas2TV);
        Pas2bTV = (TextView) findViewById(R.id.Pas2bTV);
        Pas3TV = (TextView) findViewById(R.id.Pas3TV);
        Pas3bTV = (TextView) findViewById(R.id.Pas3bTV);
        Pas4TV = (TextView) findViewById(R.id.Pas4TV);
        Pas4bTV = (TextView) findViewById(R.id.Pas4bTV);

        Pas1TV.setTypeface(dejavu);
        Pas1bTV.setTypeface(dejavu);
        Pas2TV.setTypeface(dejavu);
        Pas2bTV.setTypeface(dejavu);
        Pas3TV.setTypeface(dejavu);
        Pas3bTV.setTypeface(dejavu);
        Pas4TV.setTypeface(dejavu);
        Pas4bTV.setTypeface(dejavu);
        HelpTitle.setTypeface(dejavu);

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
}
