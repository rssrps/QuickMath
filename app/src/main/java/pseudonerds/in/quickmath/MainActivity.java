package pseudonerds.in.quickmath;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ImageView playBtn,rateBtn;
    private Animation in,out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playBtn = (ImageView) findViewById(R.id.playBtn);
        rateBtn = (ImageView) findViewById(R.id.rateBtn);

        in = new AlphaAnimation((float) 0.3, (float) 1);
        out = new AlphaAnimation((float) 1, (float)0.3);

        in.setDuration(200);
        out.setDuration(600);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                playBtn.setImageResource(R.drawable.play2);
                playBtn.startAnimation(out);
                playBtn.startAnimation(in);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent i = new Intent(MainActivity.this,Arena.class);
                        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),
                                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
                        startActivity(i,bundle);
                        playBtn.setImageResource(R.drawable.play1);
                    }
                }, 100);

            }
        });

        rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rateBtn.setImageResource(R.drawable.rate2);
                rateBtn.startAnimation(out);
                rateBtn.startAnimation(in);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        Uri uri = Uri.parse("market://details?id=" + "pseudonerds.in.quickmath");
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + "pseudonerds.in.quickmath")));
                        }


                        rateBtn.setImageResource(R.drawable.rate1);
                    }
                }, 100);



            }
        });

    }
}
