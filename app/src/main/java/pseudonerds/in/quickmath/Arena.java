package pseudonerds.in.quickmath;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.CollapsibleActionView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

public class Arena extends AppCompatActivity {

    private ImageView tick,cross;
    private Animation in,out;
    private TextView operands,score,result,time;
    private ProgressBar mProgressBar;
    private String [] first,second;
    private int []third,sequence;
    private int x,currentScore;
    private double i;
    private View root;
    private CountDownTimer mCountDownTimer;
    private MediaPlayer correct,wrong;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arena);

        first = new String[70];
        second = new String[70];
        third = new int[70];
        sequence = new int[1000];
        initArrays();

        correct = MediaPlayer.create(this, R.raw.correct);
        wrong = MediaPlayer.create(this, R.raw.error);

        tick = (ImageView) findViewById(R.id.tick);
        cross = (ImageView) findViewById(R.id.cross);

        operands = (TextView) findViewById(R.id.operands);
        result = (TextView) findViewById(R.id.result);
        score = (TextView) findViewById(R.id.score);
        time = (TextView) findViewById(R.id.time);

        root = operands.getRootView();

        editor = getSharedPreferences("prefs", MODE_PRIVATE).edit();
        prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        int color = Integer.parseInt(prefs.getString("color","1"));
        setColor(color);
        color =  ( color % 5 ) + 1;
        editor.putString("color",String.valueOf(color));
        editor.commit();

        final Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/oswald.ttf");
        operands.setTypeface(custom_font);

        in = new AlphaAnimation((float) 0.3, (float) 1);
        out = new AlphaAnimation((float) 1, (float)0.3);

        in.setDuration(200);
        out.setDuration(500);
        currentScore = 0;

        x = getQuestion();

        operands.setText(first[x]);
        result.setText(second[x]);

        i=1;
        time.setText("Time: 1.0");
        mCountDownTimer=new CountDownTimer(1000,100) {

            @Override
            public void onTick(long millisUntilFinished) {
                i=i-0.1;
                String t = String.format("%.1f",i);
                time.setText("Time: " + t);
            }

            @Override
            public void onFinish() {
                //Do what you want
                i=1.1;
                time.setText("Time: 0.0");
                wrong.start();
                if(Integer.parseInt(prefs.getString("best","0")) < currentScore ) {
                    editor.putString("best", String.valueOf(currentScore));
                    editor.commit();
                }
                Intent over = new Intent(Arena.this,Gameover.class);
                over.putExtra("score",String.valueOf(currentScore));
                Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),
                        android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
                startActivity(over,bundle);
                finish();
            }
        };


        tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCountDownTimer.cancel();
                if(third[x]==0) {

                    wrong.start();
                    if(Integer.parseInt(prefs.getString("best","0")) < currentScore ) {
                        editor.putString("best", String.valueOf(currentScore));
                        editor.commit();
                    }
                    Intent over = new Intent(Arena.this,Gameover.class);
                    over.putExtra("score",String.valueOf(currentScore));
                    Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),
                            android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
                    startActivity(over,bundle);
                    finish();
                }

                else {

                    i=1.1;
                    correct.start();
                    currentScore++;
                    score.setText(String.valueOf(currentScore));
                    tick.setImageResource(R.drawable.tick2);
                    tick.startAnimation(out);
                    tick.startAnimation(in);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            x = getQuestion();
                            operands.setText(first[x]);
                            result.setText(second[x]);

                            operands.startAnimation(in);
                            result.startAnimation(in);

                            tick.setImageResource(R.drawable.tick1);
                            mCountDownTimer.start();
                        }
                    }, 50);

                }

            }
        });

        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCountDownTimer.cancel();
                if(third[x]==1) {

                    wrong.start();
                    if(Integer.parseInt(prefs.getString("best","0")) < currentScore ) {
                        editor.putString("best", String.valueOf(currentScore));
                        editor.commit();
                    }
                    Intent over = new Intent(Arena.this,Gameover.class);
                    over.putExtra("score",String.valueOf(currentScore));
                    Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),
                            android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
                    startActivity(over,bundle);
                    finish();
                }

                else {

                    i=1.1;
                    correct.start();
                    currentScore++;
                    score.setText(String.valueOf(currentScore));
                    cross.setImageResource(R.drawable.cross2);
                    cross.startAnimation(out);
                    cross.startAnimation(in);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            x = getQuestion();
                            operands.setText(first[x]);
                            result.setText(second[x]);

                            operands.startAnimation(in);
                            result.startAnimation(in);

                            cross.setImageResource(R.drawable.cross1);
                            mCountDownTimer.start();
                        }
                    }, 50);

                }

            }
        });


    }

    private void setColor(int c) {

        if(c==1)
         root.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color1));
        else if(c==2)
            root.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color2));
        else if(c==3)
            root.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color3));
        else if(c==4)
            root.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color4));
        else if(c==5)
            root.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color5));
    }

    private void initArrays() {

        first[0]="3+3";
        second[0]="=8";
        third[0]=0;

        first[1]="1+3";
        second[1]="=5";
        third[1]=0;

        first[2]="1+2";
        second[2]="=4";
        third[2]=0;

        first[3]="3+1";
        second[3]="=4";
        third[3]=1;

        first[4]="2+2";
        second[4]="=4";
        third[4]=1;

        first[5]="2+3";
        second[5]="=5";
        third[5]=1;

        first[6]="2+2";
        second[6]="=5";
        third[6]=0;

        first[7]="2+1";
        second[7]="=3";
        third[7]=1;

        first[8]="3+3";
        second[8]="=6";
        third[8]=1;

        first[9]="3+3";
        second[9]="=8";
        third[9]=0;

        first[10]="1+3";
        second[10]="=4";
        third[10]=1;

        first[11]="3+4";
        second[11]="=8";
        third[11]=0;

        first[12]="1+5";
        second[12]="=6";
        third[12]=1;

        first[13]="1+2";
        second[13]="=4";
        third[13]=0;

        first[14]="2+3";
        second[14]="=5";
        third[14]=1;

        first[15]="2+3";
        second[15]="=7";
        third[15]=0;

        first[16]="1+1";
        second[16]="=2";
        third[16]=1;

        first[17]="2+4";
        second[17]="=7";
        third[17]=0;

        first[18]="2+2";
        second[18]="=6";
        third[18]=0;

        first[19]="3+1";
        second[19]="=6";
        third[19]=0;

        first[20]="1+3";
        second[20]="=4";
        third[20]=1;

        first[21]="2+7";
        second[21]="=9";
        third[21]=1;

        first[22]="23>27";
        second[22]="?";
        third[22]=0;

        first[23]="2x4";
        second[23]="=8";
        third[23]=1;

        first[24]="3x2";
        second[24]="=5";
        third[24]=0;

        first[25]="9>8";
        second[25]="?";
        third[25]=1;

        first[26]="5+6";
        second[26]="=11";
        third[26]=1;

        first[27]="7+6";
        second[27]="=12";
        third[27]=0;

        first[28]="8+7";
        second[28]="=15";
        third[28]=1;

        first[29]="3+6";
        second[29]="=8";
        third[29]=0;

        first[30]="2+6";
        second[30]="=8";
        third[30]=1;

        first[31]="3x3";
        second[31]="=9";
        third[31]=1;

        first[32]="2+3";
        second[32]="=4";
        third[32]=0;

        first[33]="12>23";
        second[33]="?";
        third[33]=0;

        first[34]="5+8";
        second[34]="=13";
        third[34]=1;

        first[35]="9+2";
        second[35]="=11";
        third[35]=1;

        first[36]="14>12";
        second[36]="?";
        third[36]=1;

        first[37]="6+2";
        second[37]="=9";
        third[37]=0;

        first[38]="4+4";
        second[38]="=8";
        third[38]=1;

        first[39]="45>47";
        second[39]="?";
        third[39]=0;

        first[40]="2x3";
        second[40]="=6";
        third[40]=1;

        first[41]="3x4";
        second[41]="=14";
        third[41]=0;

        first[42]="4+3";
        second[42]="=7";
        third[42]=1;

        first[43]="4+0";
        second[43]="=4";
        third[43]=1;

        first[44]="8+9";
        second[44]="=17";
        third[44]=1;

        first[45]="7+4";
        second[45]="=9";
        third[45]=0;

        first[46]="6+7";
        second[46]="=13";
        third[46]=1;

        first[47]="2x6";
        second[47]="=12";
        third[47]=1;

        first[48]="3+5";
        second[48]="=7";
        third[48]=0;

        first[49]="2x5";
        second[49]="=10";
        third[49]=1;

        first[50]="8+8";
        second[50]="=16";
        third[50]=1;

        first[51]="9+7";
        second[51]="=18";
        third[51]=0;

        first[52]="11+13";
        second[52]="=22";
        third[52]=0;

        first[53]="7x3";
        second[53]="=21";
        third[53]=1;

        first[54]="29>26";
        second[54]="?";
        third[54]=1;

        first[55]="5x3";
        second[55]="=25";
        third[55]=0;

        first[56]="1+3";
        second[56]="=6";
        third[56]=0;

        first[57]="6+8";
        second[57]="=14";
        third[57]=1;

        first[58]="2+1";
        second[58]="=3";
        third[58]=1;

        first[59]="8<3";
        second[59]="?";
        third[59]=0;

        first[60]="7x8";
        second[60]="=54";
        third[60]=0;

        first[61]="6x4";
        second[61]="=24";
        third[61]=1;

        first[62]="3+8";
        second[62]="=11";
        third[62]=1;

        first[63]="8+5";
        second[63]="=13";
        third[63]=1;

        first[64]="9+6";
        second[64]="=17";
        third[64]=0;

        first[65]="15+9";
        second[65]="=26";
        third[65]=0;

        first[66]="49<63";
        second[66]="?";
        third[66]=1;

        first[67]="12+11";
        second[67]="=23";
        third[67]=1;

        first[68]="2+5";
        second[68]="=7";
        third[68]=1;

        first[69]="3+1";
        second[69]="=6";
        third[69]=0;

    }

    private int getQuestion(){
        Random r = new Random();
        int ans = r.nextInt(70);

        if(currentScore>1){
            if(sequence[currentScore-1]==sequence[currentScore-2]&&sequence[currentScore-1]==third[ans])
                while(sequence[currentScore-1]==third[ans])
                    ans = (ans+1)%70;
        }
        sequence[currentScore]=third[ans];
        return ans;
    }

    @Override public void onBackPressed() { super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}
