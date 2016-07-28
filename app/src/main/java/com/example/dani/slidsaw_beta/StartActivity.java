package com.example.dani.slidsaw_beta;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    Button play, score, help;
    ImageView S,L,I,D,S2,A,W;
    LinearLayout linear;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Typeface dejavu = TypefacesUtils.get(this, "fonts/Roboto-Light.ttf");

        play = (Button)findViewById(R.id.play);
        score = (Button)findViewById(R.id.scores);
        help = (Button) findViewById(R.id.help);
        linear = (LinearLayout) findViewById(R.id.linearLayout);


        play.setTypeface(dejavu);
        score.setTypeface(dejavu);
        help.setTypeface(dejavu);

        S = (ImageView) findViewById(R.id.S);
        L = (ImageView) findViewById(R.id.L);
        I = (ImageView) findViewById(R.id.I);
        D = (ImageView) findViewById(R.id.D);
        S2 = (ImageView) findViewById(R.id.S2);
        A = (ImageView) findViewById(R.id.A);
        W = (ImageView) findViewById(R.id.W);

        S.setAlpha(0f);
        L.setAlpha(0f);
        I.setAlpha(0f);
        D.setAlpha(0f);
        S2.setAlpha(0f);
        A.setAlpha(0f);
        W.setAlpha(0f);


        final ObjectAnimator anS = ObjectAnimator.ofFloat(S, "alpha", 0f, 1f);
        final ObjectAnimator anL = ObjectAnimator.ofFloat(L, "alpha", 0f, 1f);
        final ObjectAnimator anI = ObjectAnimator.ofFloat(I, "alpha", 0f, 1f);
        final ObjectAnimator anD = ObjectAnimator.ofFloat(D, "alpha", 0f, 1f);
        final ObjectAnimator anS2 = ObjectAnimator.ofFloat(S2, "alpha", 0f, 1f);
        final ObjectAnimator anA = ObjectAnimator.ofFloat(A, "alpha", 0f, 1f);
        final ObjectAnimator anW = ObjectAnimator.ofFloat(W, "alpha", 0f, 1f);

        final AnimatorSet an = new AnimatorSet();
        an.playSequentially(anS,anL,anI,anD,anS2,anA,anW);
        an.setDuration(400);

        an.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        an.start();

        // Habilitem els listeners per cada botó
        play.setOnClickListener(this);
    }

    public void onClick (View v){
        switch (v.getId()){
            case R.id.play:
                Intent intent = new Intent(this, LevelActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.left_in, R.animator.left_out);
                break;
        }
    }

    public void onToScores(View view) {
        Intent intent = new Intent(this, ScoresActivity.class);
        startActivity(intent);
        overridePendingTransition(R.animator.left_in, R.animator.left_out);
    }

    public void onToHelp(View view){
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
        overridePendingTransition(R.animator.left_in, R.animator.left_out);
    }

    private static final String DATA_FILE = "score.obj";

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
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