package pseudonerds.in.quickmath;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.IntDef;
import android.support.annotation.IntegerRes;
import android.support.annotation.StringDef;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

public class Gameover extends AppCompatActivity {


    private ImageView replay,share,level;
    private TextView score,best;
    private Animation in,out;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;
    private String packagename = "pseudonerds.in.quickmath";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);

        replay = (ImageView) findViewById(R.id.replay);
        share = (ImageView) findViewById(R.id.share);
        level = (ImageView) findViewById(R.id.level);
        score = (TextView) findViewById(R.id.score);
        best = (TextView) findViewById(R.id.best);

        editor = getSharedPreferences("prefs", MODE_PRIVATE).edit();
        prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        in = new AlphaAnimation((float) 0.3, (float) 1);
        out = new AlphaAnimation((float) 1, (float)0.3);

        in.setDuration(200);
        out.setDuration(500);

        Bundle b = getIntent().getExtras();

        score.setText(b.getString("score"));

        best.setText("Best: " + prefs.getString("best",b.getString("score")));

        int currScore = Integer.parseInt(b.getString("score"));
        if(currScore<15)
            level.setImageResource(R.drawable.beginner);
        else if (currScore>=15&&currScore<25)
            level.setImageResource(R.drawable.semipro);
        else
            level.setImageResource(R.drawable.genius);


        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                replay.setImageResource(R.drawable.play2);
                replay.startAnimation(out);
                replay.startAnimation(in);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent i = new Intent(Gameover.this,Arena.class);
                        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),
                                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
                        startActivity(i,bundle);
                        replay.setImageResource(R.drawable.play1);
                        finish();
                    }
                }, 30);

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                share.setImageResource(R.drawable.share2);
                share.startAnimation(out);
                share.startAnimation(in);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Intent i = new Intent(Intent.ACTION_SEND);
                            i.setType("text/plain");
                            i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                            String sAux = "How good are your Math skills?\n\n";
                            sAux = sAux + "https://play.google.com/store/apps/details?id=" + packagename;

                            i.putExtra(Intent.EXTRA_TEXT, sAux);
                            startActivity(Intent.createChooser(i, "choose one"));
                        } catch (Exception e) {
                            //e.toString();
                        }
                        share.setImageResource(R.drawable.share1);
                    }
                }, 30);

            }
        });

    }

    @Override public void onBackPressed() { super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
